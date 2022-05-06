import Client.Client;
import Server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(7777);
        Client client = new Client();
        server.RunServer();
        client.SendMessage();

    }
}
