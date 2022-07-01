import Client.Client;
import Messages.Mail;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client1 = new Client();
        Mail mail1 = new Mail("ciao sono una prova","mattiamondino@gmail.com","io","oggi","paolo@gmail.com");
        client1.SendMessage(mail1);
        Mail mailrecived = client1.receiveMessage();
        System.out.println(mailrecived.getEmail());
        Mail mail2 = new Mail("ciao sono una prova","mattiamondino@gmail.com","io","oggi","simone@gmail.com");
        Mail mail3 = new Mail("ciao ","mattiamondino@gmail.com","io","oggi","simone@gmail.com");
        Client client2 = new Client();
        client2.SendMessage(mail2);
        client2.SendMessage(mail3);


    }
}
