package Messages;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;


import java.io.*;
import java.util.ArrayList;


public class RecordMessagges {

    Message message;

    public RecordMessagges() {

    }

    public void filterMessage() throws IOException {
        if(message.getType() == "Mail"){
            saveMail(message.getMail());
        }else if(message.getType() == "ReqMail"){

        }

    }

    public synchronized void saveMail(Mail mail) throws IOException {


        String userMail = mail.getReciver();
        String path = "C:\\Users\\matti\\MailProject\\Users\\" + userMail + ".json";
        File file = new File(path);

        JsonArray mailList = new JsonArray();
        String jsonMailString = new Gson().toJson(mail);
        if(!file.exists()){
            file.createNewFile();
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                mailList.add(jsonMailString);
                out.write(mailList.toString());
                out.flush();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else{
            mailList = JsonParser.parseReader(new JsonReader(new FileReader(path))).getAsJsonArray();

            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {

                mailList.add(jsonMailString);
                out.write(mailList.toString());
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public ArrayList<Mail> getUserMails(Message message) throws FileNotFoundException {
        ArrayList<Mail> userMails = new ArrayList<>();
        String path = "C:\\Users\\matti\\MailProject\\Users\\" + message.getSender() + ".json";
        File file = new File(path);
        if(file.exists()) {
            JsonArray mailList = JsonParser.parseReader(new JsonReader(new FileReader(path))).getAsJsonArray();

            for (int i = 0; i < mailList.size(); i++) {

                userMails.add(parseMailobject(mailList.get(i)));
            }

        }else{

            System.out.println("non sono presenti mail dell'utente");

        }

        return userMails;
    }

    public Mail parseMailobject(JsonElement mail){

        String cleanjson = removeQuotesAndUnescape(mail.getAsString());
        JsonObject obj = new Gson().fromJson(cleanjson, JsonObject.class);

        String text = obj.get("text").getAsString();

        String email = obj.get("email").getAsString();

        String sender = obj.get("sender").getAsString();

        String date = obj.get("date").getAsString();

        String reciver = obj.get("reciver").getAsString();

        Mail mailObj = new Mail(text,email,sender,date,reciver);

        return mailObj;


    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }


}
