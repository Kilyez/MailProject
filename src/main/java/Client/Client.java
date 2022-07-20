package Client;

import Messages.Mail;
import java.net.*;
import java.io.*;

import Messages.Message;
import javafx.application.Platform;
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
    private TimerTask refreshTimer;



    public Client(String email)
    {

        StartClient();
        this.mail= email;
        receivedMailList = new ArrayList<>();
        sendedMailList = new ArrayList<>();
        obsRecivedMailList = FXCollections.observableArrayList(receivedMailList);
        obsSendedMailList = FXCollections.observableArrayList(sendedMailList);
        reciveMessage("GETSENDEDMAIL");


    }

    public void StartClient(){
        try {
            System.out.println("Apertura connessione...");
            this.client = new Socket("localhost", 7777);
            out = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

        } catch (IOException ex) {

            System.err.println("Errore nella connessione ");
        }

    }

    public ObservableList<Mail> getObsRecivedMailList() {
        return obsRecivedMailList;
    }

    public ObservableList<Mail> getObsSendedMailList() {
        return obsSendedMailList;
    }


    public void filterMessage(Message message){
        if(Objects.equals(message.getType(), "GETMAILS")){
            newMails = message.getMailList();
            if (newMails.isEmpty()  ){
                System.out.println("no new recived mails");
            }else{
                System.out.println();
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
        }

    }



    public void addToReceivedMailList(ArrayList<Mail> newMails){
        for(Mail mail : newMails){
            receivedMailList.add(mail);
            obsRecivedMailList.add(mail);

        }
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

    public void SendMail(Mail smail) throws IOException {
        new Thread(()->{
            synchronized (this) {
                if(client.isClosed()){
                    StartClient();
                }
                Message message = new Message("SENDMAIL", "", smail ,mail);
                try {
                    out.writeObject(message);
                    out.flush();
                    client.close();
                } catch (IOException e) {
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
                    System.out.println(obsRecivedMailList.size());
                    receivedMailList.forEach(mail1 -> {
                        System.out.println(mail1);
                    });
                }
            };
            periodicalRefresh.scheduleAtFixedRate(refreshTimer,0,5000);
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
            if(client.isClosed()){
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
            System.out.println(response.getMailList());
            filterMessage(response);
            client.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}