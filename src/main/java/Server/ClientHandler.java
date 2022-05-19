package Server;

import Messages.Mail;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream out;
    private int counter;


    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        input = new ObjectInputStream(client.getInputStream());
        out = new ObjectOutputStream(client.getOutputStream());
        counter ++;
    }

    @Override
    public void run() {
        while (!client.isClosed() &&  client.isConnected())
        {
            try {
                Mail mail = (Mail) input.readObject();
                System.out.println(mail.getEmail());
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
}
