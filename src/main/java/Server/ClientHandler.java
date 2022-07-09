package Server;

import Messages.Mail;
import Messages.Message;
import Messages.RecordMessagges;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private int counter;
    private RecordMessagges recordMessagges;


    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        input = new ObjectInputStream(client.getInputStream());
        out = new ObjectOutputStream(client.getOutputStream());
        counter ++;
        recordMessagges = new RecordMessagges();
    }

    @Override
    public void run() {
        while (!client.isClosed() &&  client.isConnected())
        {
            try {

                Mail mail = receiveMessage();
                recordMessagges.saveMail(mail);
               /* Message message = new Message("req", "","simone@gmail.com","","");
                ArrayList<Mail> mailList = recordMessagges.getUserMails(message);
                mailList.forEach( m -> System.out.println(m.getEmail()));
                System.out.println(mail.getEmail());*/
                Mail mail1 = new Mail("ciao sono una prova","matti@gmail.com","io","","oggi");
                out.writeObject(mail1);
                client.close();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    public Mail receiveMessage() throws IOException, ClassNotFoundException {

        Mail mail = (Mail) input.readObject();

        System.out.println("Got from client on port " + client.getPort() + " " );
        return mail;

    }

    public void SendMessage() throws IOException {

        Mail mail = new Mail("ciao sono una prova","matti@gmail.com","io","","tu");
        out.writeObject(mail);
        out.flush();

    }

}
