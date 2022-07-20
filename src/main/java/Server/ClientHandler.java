package Server;

import Messages.Mail;
import Messages.Message;
import Messages.UserMailManagement;
import javafx.beans.property.ListProperty;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private int counter;
    private UserMailManagement mailmanagement;
    ArrayList<Mail> userMails;


    public ClientHandler(Socket client, UserMailManagement usermanagment) throws IOException {
        this.client = client;
        input = new ObjectInputStream(client.getInputStream());
        out = new ObjectOutputStream(client.getOutputStream());
        counter ++;
        mailmanagement = usermanagment;
        userMails = new ArrayList<>();
        usermanagment.getALLUsersMailsRecived();
        usermanagment.getALLUsersMailsSended();
    }

    @Override
    public void run() {
        while (!client.isClosed() &&  client.isConnected())
        {
            try {


                Message message = receiveMessage();
                filterMessage(message);
                client.close();
                /*ArrayList<Mail> mailList = recordMessagges.getALLUsersMails();
                mailList.forEach( m -> System.out.println(m.getText()));*/
               /* Message message = new Message("req", "","simone@gmail.com","","");
                ArrayList<Mail> mailList = recordMessagges.getUserMails(message);
                mailList.forEach( m -> System.out.println(m.getEmail()));
                System.out.println(mail.getEmail());*/


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
    public void filterMessage(Message message) throws IOException {

        if(Objects.equals(message.getType(), "SENDMAIL")){
            mailmanagement.saveMail(message.getMail());


        }else if(Objects.equals(message.getType(), "GETMAILS")){
            userMails = mailmanagement.getUserMails(message);
            Message response = new Message("GETMAILS","","SERVER",userMails);
            SendMessage(response);


        }else if(Objects.equals(message.getType(), "GETSENDEDMAIL")){
            userMails = mailmanagement.getUserMailsSended(message);
            Message response = new Message("GETSENDEDMAIL","","SERVER",userMails);
            SendMessage(response);

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
