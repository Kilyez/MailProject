package Messages;

public class Message {

    String type;
    String text;
    String sender;
    Mail mail;
    String date;
    String time;

    public Message(String type, String text, Mail mail, String sender, String date, String time) {
        this.type = type;
        this.text = text;
        this.mail = mail;
        this.sender = sender;
        this.date = date;
        this.time = time;
    }

    public Message(String type, String text, String sender, String date, String time) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.date = date;
        this.time = time;
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
