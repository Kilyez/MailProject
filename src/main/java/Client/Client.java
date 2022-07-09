package Client;

import Messages.Mail;
import java.net.*;
import java.io.*;
import org.json.JSONObject;
import java.util.Random;


public class Client
{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private String ip;

    public Client()
    {
        try
        {
            Random r = new Random();
            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
            System.out.println("Apertura connessione...");
            this.client = new Socket ("localhost", 7777);

        }
        catch (IOException ex)
        {
            System.err.println("Errore nella connessione ");
        }

    }

    public void SendMessage(Mail mail) throws IOException {
        out = new ObjectOutputStream(client.getOutputStream());

        out.writeObject(mail);
        out.flush();

    }

    public Mail receiveMessage() throws IOException, ClassNotFoundException {

        input = new ObjectInputStream(client.getInputStream());
        Mail mail = (Mail) input.readObject();

        System.out.println("Got from Server on port " + client.getPort() + " " );
        return mail;

    }

}