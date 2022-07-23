package Client;

import Messages.Mail;
import java.net.*;
import java.io.*;

import Messages.Message;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;


public class Client
{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private ObservableList<Mail> obsRecivedMailList;
    private ArrayList<Mail> receivedMailList;
    private ObservableList<Mail> obsSendedMailList;
    private ArrayList<Mail> sendedMailList;
    private ArrayList<Mail> newMails;
    private String mail;
    private StringProperty erReciversNotFounded;
    private TimerTask refreshTimer = null;



    public Client(String email)
    {
        this.mail= email;
        receivedMailList = new ArrayList<>();
        sendedMailList = new ArrayList<>();
        erReciversNotFounded = new SimpleStringProperty();
        obsRecivedMailList = FXCollections.observableArrayList(receivedMailList);
        obsSendedMailList = FXCollections.observableArrayList(sendedMailList);
        try{
            StartClient();
            reciveMessage("GETSENDEDMAIL");

        }catch(IOException ex){

            System.err.println("Errore nella connessione ");

        }


    }

    public void StartClient() throws IOException{

        System.out.println("Apertura connessione...");
        this.client = new Socket("localhost", 7777);
        out = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());


    }

    public ObservableList<Mail> getObsRecivedMailList() {

        return obsRecivedMailList;
    }

    public ObservableList<Mail> getObsSendedMailList() {

        return obsSendedMailList;
    }

    public StringProperty getErReciversNotFounded() {

        return erReciversNotFounded;
    }

    public void filterMessage(Message message){
        if(Objects.equals(message.getType(), "GETMAILS")){
            newMails = message.getMailList();
            if (newMails.isEmpty()  ){

                System.out.println("no new recived mails");
            }else{
                Platform.runLater(() -> {
                    addToReceivedMailList(newMails);
                });


            }
        }else if(Objects.equals(message.getType(), "GETSENDEDMAIL")) {
            newMails = message.getMailList();
            if (newMails.isEmpty()) {
                System.out.println("no sended mails");
            } else {

                Platform.runLater(() -> {
                    addToSendedMailList(newMails);
                });
            }
        }else if(Objects.equals(message.getType(), "ERRNOUSER")){
            Platform.runLater(() -> {
                setErrorStringReciver(message.getText());
            });

        }
    }


   public void setErrorStringReciver(String reciversNotFound){
        erReciversNotFounded.set("ERRORE Destinatari non trovati:" + reciversNotFound);
   }

    public void addToReceivedMailList(ArrayList<Mail> newMails){
        for(Mail mail : newMails){
            receivedMailList.add(mail);

        }
        obsRecivedMailList.addAll(newMails);

    }

    public void cancelMail(Mail toSend, boolean sended){

        new Thread(()->{
            synchronized (this) {
                try{
                    if(client.isClosed()){
                    StartClient();
                    }
                    if(sended){
                        sendedMailList.remove(toSend);
                        Platform.runLater(() -> obsSendedMailList.remove(toSend));

                    }else{
                        receivedMailList.remove(toSend);
                        Platform.runLater(() -> obsRecivedMailList.remove(toSend));
                    }
                    Message message;
                    if(sended){
                        message = new Message("CANCSENDEDMAIL", "", toSend ,mail);
                    }else{
                        message = new Message("CANCRECIVEDMAIL", "", toSend ,mail);
                    }
                    SendMessage(message);
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public void addToSendedMailList(ArrayList<Mail> newMails){
        for(Mail mail : newMails){
            sendedMailList.add(mail);
            obsSendedMailList.add(mail);
        }


    }

    public void SendMessage(Message message) throws IOException {

        out.writeObject(message);
        out.flush();

    }
    public void getRecivedMail(){

       new Thread(()-> {

           reciveMessage("GETMAILS");
           System.out.println(receivedMailList.size());

        });
    }


    public void SendMail(Mail smail) throws IOException {
        new Thread(()->{
            synchronized (this) {
                try {
                    if(client.isClosed()){
                        StartClient();
                    }

                    Message message = new Message("SENDMAIL", "", smail ,mail);

                    SendMessage(message);
                    Message response = (Message) input.readObject();
                    System.out.println("Got from Server on port " + client.getPort() + " " );
                    filterMessage(response);
                    client.close();
                }catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public Mail reciveMail() throws ClassNotFoundException, IOException {

        Mail mail = (Mail) input.readObject();

        System.out.println("Got from Server on port " + client.getPort() + " " );
        return mail;

    }



    public void recivedMailRefresh() throws InterruptedException {
        if (refreshTimer == null){
            Timer periodicalRefresh = new Timer();
            refreshTimer = new TimerTask() {
                @Override
                public void run() {
                    reciveMessage("GETMAILS");
                    System.out.println(receivedMailList.size());

                  /*  System.out.println(obsRecivedMailList.size());
                    receivedMailList.forEach(mail1 -> {
                        System.out.println(mail1);
                    });*/
                }
            };
            periodicalRefresh.scheduleAtFixedRate(refreshTimer,0,20000);

        }

    }


    public void stopRefresh(){
        if (refreshTimer != null){
            refreshTimer.cancel();
            refreshTimer = null;
        }

    }



    public void reciveMessage(String type){

        try {
            if(client == null || client.isClosed()){
                StartClient();
            }
            Message message;
            if(type.equals("GETMAILS")){
                if(receivedMailList.isEmpty()){
                    message = new Message("GETMAILS","",null, mail );

                }else{
                    message = new Message("GETMAILS", "",  receivedMailList.get(receivedMailList.size()-1), mail);

                }

            }else{
                message = new Message("GETSENDEDMAIL","",null, mail );
            }
            SendMessage(message);
            Message response = (Message) input.readObject();
            System.out.println("Got from Server on port " + client.getPort() + " " );
            filterMessage(response);
            client.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void logIn(){

        new Thread(()->{
            try {
                if (client == null || client.isClosed()) {
                    StartClient();
                }
                Message message = new Message("LOGIN", "", null, mail);

                out.writeObject(message);
                out.flush();
                client.close();

            }catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public void closeClient() throws IOException {

        if (refreshTimer != null){
            refreshTimer.cancel();
        }
        if(client.isConnected()){
            client.close();
        }

    }

}