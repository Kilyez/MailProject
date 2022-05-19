package Client;

import Messages.Mail;
import java.net.*;
import java.io.*;
import org.json.JSONObject;
import java.util.Random;


public class Client
{
    Socket client;
    String ip;

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

    public void SendMessage() throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        Mail mail = new Mail("ciao sono una prova","mattiamondino@gmail.com",this.ip,"io","oggi","tu");
        out.writeObject(mail);

    }

    public void reciveMessagge() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));


    }

}