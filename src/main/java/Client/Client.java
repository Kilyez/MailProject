package Client;

import Messages.Mail;
import java.net.*;
import java.io.*;
import org.json.JSONObject;
import java.util.Random;


public class Client
{
    Socket client;
    ObjectInputStream ois;
    ObjectOutputStream out;
    String ip;

    public Client()
    {
        try
        {


            Random r = new Random();
            ip = r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
            System.out.println("Apertura connessione...");
            this.client = new Socket (ip, 7777);

            InputStream is = client.getInputStream();
            this.ois = new ObjectInputStream(is);
            OutputStream sout = client.getOutputStream();
            this.out = new ObjectOutputStream(sout);

        }
        catch (ConnectException connExc)
        {
            System.err.println("Errore nella connessione ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void SendMessage() throws IOException {
        Mail mail = new Mail("ciao sono una prova","mattiamondino@gmail.com",this.ip,"io","oggi","tu");
        JSONObject json  = new JSONObject();
        json.put("email",mail);
        out.writeObject(json.toString());
    }

    public void reciveMessagge(){

    }

}