import Client.Client;
import Server.Server;
import org.json.JSONObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(7777);
        server.RunServer();



    }
}
