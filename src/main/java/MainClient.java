import Client.Client;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        Client client1 = new Client();
        client1.SendMessage();
        Client client2 = new Client();
        client2.SendMessage();

    }
}
