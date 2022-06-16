import Client.Client;
import Messages.Mail;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client1 = new Client();
        client1.SendMessage();
        Mail mailrecived = client1.receiveMessage();
        System.out.println(mailrecived.getEmail());
        Client client2 = new Client();
        client2.SendMessage();

    }
}
