package Messages;

public class Message {

    String type;
    String text;
    Mail mail;

    public Message(String type, String text, Mail mail) {
        this.type = type;
        this.text = text;
        this.mail = mail;
    }

    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Mail getMail() {
        return mail;
    }
}
