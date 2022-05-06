package Server;

import java.net.Socket;

public class ClientHandler extends Thread{
    Socket client;

    public ClientHandler(Socket s){
        this.client = s;
    }
}
