package Messages;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang.StringEscapeUtils;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserMailManagement {

    HashMap<String,ArrayList<Mail>> allUsersMailsReceived;
    HashMap<String,ArrayList<Mail>> allUsersMailsSended;



    public UserMailManagement() {
        allUsersMailsReceived = new HashMap<>();
        allUsersMailsSended = new HashMap<>();
    }



    public synchronized void saveMail(Mail mail) throws IOException {

        saveMailToSender(mail);
        String userMail = mail.getReciver();

        String path = "C:\\Users\\matti\\MailProject\\Users\\Received\\" + userMail + ".json";
        File file = new File(path);
        JsonArray mailList = new JsonArray();
        String jsonMailString = new Gson().toJson(mail);
        if(file.createNewFile()){

            ArrayList<Mail> firstMail = new ArrayList<>();
            firstMail.add(mail);
            allUsersMailsReceived.put(userMail,firstMail);
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                mailList.add(jsonMailString);
                out.write(mailList.toString());
                out.flush();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else{
            allUsersMailsReceived.get(userMail).add(mail);
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

    public synchronized void saveMailToSender(Mail mail) throws IOException {


        String userMail = mail.getSender();

        String path = "C:\\Users\\matti\\MailProject\\Users\\Sended\\" + userMail + ".json";
        File file = new File(path);
        JsonArray mailList = new JsonArray();
        String jsonMailString = new Gson().toJson(mail);
        if(file.createNewFile()){

            ArrayList<Mail> firstMail = new ArrayList<>();
            firstMail.add(mail);
            allUsersMailsSended.put(userMail,firstMail);
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                mailList.add(jsonMailString);
                out.write(mailList.toString());
                out.flush();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else{
            allUsersMailsSended.get(userMail).add(mail);
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
        AtomicBoolean findend = new AtomicBoolean(false);
        Mail lastMail;
        ArrayList<Mail> newEmails = new ArrayList<>();
        ArrayList<Mail> userEmails;

        if(message.getMail() != null){
            lastMail = message.getMail();
            allUsersMailsReceived.get(lastMail.getReciver()).forEach(mail -> {
                if (mail.equals(lastMail)) {
                    findend.set(true);
                }else if(findend.get()){
                    newEmails.add(mail);
                }
            });

        }else{
            userEmails = allUsersMailsReceived.get(message.getSender());
            return userEmails;
        }


        return newEmails;

    }

    public ArrayList<Mail> getUserMailsSended(Message message) throws FileNotFoundException {

        ArrayList<Mail> userEmails = new ArrayList<>();
        if(allUsersMailsSended.containsKey(message.getSender())){
            userEmails = allUsersMailsSended.get(message.getSender());

        }
        return userEmails;

    }

    public void getALLUsersMailsRecived () throws FileNotFoundException {

        ArrayList<Mail> mailList;
        File directory = new File("C:\\Users\\matti\\MailProject\\Users\\Received");
        File[] listFile = directory.listFiles();

        for ( File file : listFile ){
            JsonArray List = JsonParser.parseReader(new JsonReader(new FileReader(file))).getAsJsonArray();
            mailList = new ArrayList<>();

            for (int i = 0; i < List.size(); i++) {

                mailList.add(parseMailobject(List.get(i)));
            }
            allUsersMailsReceived.put(removeExtension(file.getName()),mailList);

        }


    }

    public void getALLUsersMailsSended () throws FileNotFoundException {

        ArrayList<Mail> mailList;
        File directory = new File("C:\\Users\\matti\\MailProject\\Users\\Sended");
        File[] listFile = directory.listFiles();

        for ( File file : listFile ){
            JsonArray List = JsonParser.parseReader(new JsonReader(new FileReader(file))).getAsJsonArray();
            mailList = new ArrayList<>();

            for (int i = 0; i < List.size(); i++) {

                mailList.add(parseMailobject(List.get(i)));
            }
            allUsersMailsSended.put(removeExtension(file.getName()),mailList);

        }


    }

    public Mail parseMailobject(JsonElement mail){

        String cleanjson = removeQuotesAndUnescape(mail.getAsString());
        JsonObject obj = new Gson().fromJson(cleanjson, JsonObject.class);

        String text = obj.get("text").getAsString();

        String email = obj.get("email").getAsString();

        String sender = obj.get("sender").getAsString();

        String object = obj.get("object").getAsString();

        String reciver = obj.get("reciver").getAsString();

        String date = obj.get("date").getAsString();

        String time = obj.get("time").getAsString();

        Mail mailObj = new Mail(text,email,sender,reciver,object,date,time);

        return mailObj;


    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }

    private static String removeExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            fileName = fileName.substring(0, lastIndex);
        }
        return fileName;
    }



}
