package Server.Model;

import Messages.UserMailManagement;
import Server.Model.ClientHandler;
import Server.Model.ServerModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ServerSocket socketServer;
    private Socket s;
    private final ServerModel model;
    private ExecutorService pool;
    private UserMailManagement usermanagment;

    public Server(int port,ServerModel model)
    {
        this.pool = Executors.newFixedThreadPool(10);
        this.usermanagment = new UserMailManagement();
        this.model = model;
        this.port = port;
        if(!startServer()){
            System.out.println("Errore nella creazione del server");
        }
    }

    private Boolean startServer(){
        try {
            socketServer = new ServerSocket(port);
            usermanagment.getALLUsersMailsSended();
            usermanagment.getALLUsersMailsRecived();
        }catch (IOException e){
            e.printStackTrace();
            return false;

        }
        System.out.println("Server creato con successo");
        return true;
    }

    public void RunServer(){
        new Thread(() -> {
            while (!socketServer.isClosed())
            {
                try
                {

                    System.out.println("Server in attesa di richieste...");
                    s = socketServer.accept();
                    ClientHandler clientThread = new ClientHandler(s,usermanagment,model);
                    pool.execute(clientThread);


                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    public void CloseServer() throws IOException {
        try {
            s.close();
            socketServer.close();
            System.out.println("Chiusura connessione effettuata");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
