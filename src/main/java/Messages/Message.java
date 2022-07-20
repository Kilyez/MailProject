package Messages;

import javax.swing.table.TableStringConverter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Message implements Serializable {

    String type;
    String text;
    String sender;
    Mail mail;
    String date;
    String time;
    ArrayList<Mail> mailList;

    public Message(String type, String text, Mail mail, String sender) {
        this.type = type;
        this.text = text;
        this.mail = mail;
        this.sender = sender;
        String actual = new Timestamp(System.currentTimeMillis()).toString();
        this.date = actual.substring(0,10);
        this.time = actual.substring(11);
    }

    public Message(String type, String text, String sender) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        String actual = new Timestamp(System.currentTimeMillis()).toString();
        this.date = actual.substring(0,10);
        this.time = actual.substring(11);
    }
    public Message(String type, String text, String sender,ArrayList<Mail> mails) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        String actual = new Timestamp(System.currentTimeMillis()).toString();
        this.date = actual.substring(0,10);
        this.time = actual.substring(11);
        this.mailList = mails;
    }

    public ArrayList<Mail> getMailList() {
        return mailList;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public Mail getMail() {
        return mail;
    }
}
