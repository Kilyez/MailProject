package Server.Model;

import Messages.Mail;
import Messages.Message;
import Messages.UserMailManagement;
import Server.Model.ServerModel;
import javafx.beans.property.ListProperty;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private ServerModel serverModel;
    private UserMailManagement mailmanagement;
    private ArrayList<Mail> userMails;



    public ClientHandler(Socket client, UserMailManagement usermanagment, ServerModel model) throws IOException {
        this.client = client;
        this.input = new ObjectInputStream(client.getInputStream());
        this.out = new ObjectOutputStream(client.getOutputStream());
        this.serverModel = model;
        this.mailmanagement = usermanagment;
        this.userMails = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!client.isClosed() &&  client.isConnected())
        {
            try {

                Message message = receiveMessage();
                filterMessage(message);
                client.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
    public void filterMessage(Message message) throws IOException {

        if(Objects.equals(message.getType(), "LOGIN")){
            mailmanagement.checkLogInfo(message.getSender());
            serverModel.appendLogText("Un nuovo client si e` connesso!");

        }else if(Objects.equals(message.getType(), "SENDMAIL")){
            serverModel.appendLogText("Un client ha inviato una mail");
            Message response;
            String reciversNotFound = mailmanagement.saveMail(message.getMail());
            if (reciversNotFound.isEmpty()){
                response = new Message("OK","","SERVER",null);
                serverModel.appendLogText("Mail inviata con successo");
            }else{
                response = new Message("ERRNOUSER",reciversNotFound,"SERVER",null);
                serverModel.appendLogText("ERRORE: non esistono questi destinatari : " + reciversNotFound + "");
            }
            SendMessage(response);


        }else if(Objects.equals(message.getType(), "GETMAILS")){
            userMails = mailmanagement.getUserMails(message);
            serverModel.appendLogText("Richiesta di nuove Mail da un client");
            Message response = new Message("GETMAILS","","SERVER",userMails);
            if (userMails.isEmpty()){
                serverModel.appendLogText("Nessuna nuova mail per il client");
            }else{
                serverModel.appendLogText("Inviate nuove Mail al client");
            }
            SendMessage(response);


        }else if(Objects.equals(message.getType(), "GETSENDEDMAIL")){
            userMails = mailmanagement.getUserMailsSended(message);
            Message response = new Message("GETSENDEDMAIL","","SERVER",userMails);
            SendMessage(response);

        }else if (Objects.equals(message.getType(), "CANCSENDEDMAIL")){

            mailmanagement.cancelMail(message.getMail(),true, message.getSender());

        }else if (Objects.equals(message.getType(), "CANCRECIVEDMAIL")){
            serverModel.appendLogText("Un client ha richiesto di cancellare una Mail");
            mailmanagement.cancelMail(message.getMail(),false, message.getSender());
            serverModel.appendLogText("la mail e` stata cancellata");

        }
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {

        Message message = (Message) input.readObject();

        System.out.println("Got from client on port " + client.getPort() + " " );
        return message;

    }

    public void SendMessage(Message message) throws IOException {

        out.writeObject(message);
        out.flush();

    }

}
