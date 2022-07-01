package Messages;

import java.io.Serializable;

public class Mail implements Serializable {

    private String object;
    private String text;
    private String email;
    private String sender;
    private String date;
    private String reciver;

    public Mail(String text, String email, String sender, String date, String reciver) {
        this.text = text;
        this.email = email;
        this.sender = sender;
        this.date = date;
        this.reciver = reciver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }
}
