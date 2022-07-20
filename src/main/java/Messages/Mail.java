package Messages;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Mail implements Serializable {

    private String object;
    private String text;
    private String time;
    private String email;
    private String sender;
    private String date;
    private String reciver;

    public Mail(String text, String email, String sender, String reciver, String object) {
        this.text = text;
        this.email = email;
        this.sender = sender;
        String actual = new Timestamp(System.currentTimeMillis()).toString();
        this.date = actual.substring(0,10);
        this.time = actual.substring(11);
        this.reciver = reciver;
        this.object = object;
    }
    public Mail(String text, String email, String sender, String reciver, String object,String date, String time) {
        this.text = text;
        this.email = email;
        this.sender = sender;
        this.reciver = reciver;
        this.object = object;
        this.date = date;
        this.time = time;
    }

    public String getObject() { return object; }

    public String getTime() { return time; }

    public String getText() {
        return text;
    }

    public String getObjectPreview() {
        int i = 30 - object.length();
        String filled = StringUtils.repeat(" ", i);
        return  object.concat(filled);
    }
    public String listFormatter(){
        String format = getMailPreview() + getObjectPreview() + getTextPreview() + getDate();
        return format;
    }

    public String getTextPreview() {
        String preview;
        if(text.length() > 10) {
            preview = text.substring(0,10);

        }else{
            preview = text;
        }
        return preview;

    }

    public String getMailPreview(){
        int i = email.indexOf('@');
        String previewMail = email.substring(0,i);

        return previewMail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(time, mail.time) && Objects.equals(date, mail.date);
    }

}
