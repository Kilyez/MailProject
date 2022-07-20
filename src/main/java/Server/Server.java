package Server;

import Messages.Mail;
import Messages.UserMailManagement;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ServerSocket server;
    private Socket s;
    private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private ExecutorService pool = Executors.newFixedThreadPool(4);
    private UserMailManagement usermanagment = new UserMailManagement();

    public Server(int port){
        this.port = port;
        if(!startServer()){
            System.out.println("Errore nella creazione del server");
        }
    }

    private Boolean startServer(){
        try {
            server = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
            return false;

        }
        System.out.println("Server creato con successo");
        return true;
    }

    public void RunServer(){
        while (true)
        {
            try
            {

                System.out.println("Server in attesa di richieste...");
                s = server.accept();
                System.out.println("Connected to Client");
                ClientHandler clientThread = new ClientHandler(s,usermanagment);
                clients.add(clientThread);
                pool.execute(clientThread);

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    public void CloseServer() throws IOException {
        try {
            s.close();
            server.close();
            System.out.println("Chiusura connessione effettuata");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
