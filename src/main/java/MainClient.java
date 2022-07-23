import Client.Client;
import Messages.Mail;

import java.io.IOException;
import java.util.Timer;

public class MainClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
       /* Client client1 = new Client();
        Mail mail1 = new Mail("ciao sono una prova","mattiamondino@gmail.com","io","oggi","paolo@gmail.com");
        client1.SendMessage(mail1);
        Mail mailrecived = client1.receiveMessage();
        System.out.println(mailrecived.getEmail());
        Mail mail2 = new Mail("ciao sono una prova","mattiamondino@gmail.com","io","oggi","simone@gmail.com");
        Mail mail3 = new Mail("ciao ","luca@gmail.com","io","oggi","simone@gmail.com");
        Client client2 = new Client();
        client2.SendMessage(mail2);
        Mail mailrecived1 = client2.receiveMessage();
         Mail mail1 = new Mail("ciao sono un nuovo messagio","mattiamondino@gmail.com","io","simone@gmail.com","test");
        Mail mail2 = new Mail("ciao sono una prova","mattiamondino@gmail.com","io","paolo@gmail.com","test");
        Mail mail4 = new Mail("ciao","mattiamondino@gmail.com","io","paolo@gmail.com","test");
        Mail mail3 = new Mail("ciao ","luca@gmail.com","io","simone@gmail.com","test");
        client1.SendMail(mail1);
        client1.SendMail(mail2);
        client1.SendMail(mail3);
        client1.SendMail(mail4);*/
        /*Mail mail3 = new Mail("ciao ","luca@gmail.com","simone@gmail.com","test");
        Mail mail2 = new Mail("ciao sono una prova","luca@gmail.com","paolo@gmail.com","test");
        Mail mail4 = new Mail("ciao","simone@gmail.com","paolo@gmail.com","test");
        Mail mail1 = new Mail("ciao sono un nuovo messagio","simone@gmail.com","simone@gmail.com","test");

        Client client1 = new Client("luca@gmail.com");
        client1.SendMail(mail4);

        client1.SendMail(mail1);

        client1.SendMail(mail2);

        client1.SendMail(mail3);
         Mail mail1 = new Mail("ciao sono un nuovo messagio ti e` arrivato il popUp?","luca@gmail.com","simone@gmail.com","test popUp");
        Client client1 = new Client("luca@gmail.com");
        client1.SendMail(mail1);
         */

        String[] reciver = new String[4];
        reciver[0] = "simone";
        reciver[1] = "mattia";
        reciver[2] = "francesco";
        reciver[3] = "francesco";

        String reciversNotFound = new String();

        System.out.println(reciversNotFound.isEmpty());
    }
}
