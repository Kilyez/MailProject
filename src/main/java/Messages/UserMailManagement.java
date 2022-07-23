package Messages;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang.StringEscapeUtils;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserMailManagement {

    HashMap<String, ArrayList<Mail>> allUsersMailsReceived;
    HashMap<String, ArrayList<Mail>> allUsersMailsSended;


    public UserMailManagement() {
        allUsersMailsReceived = new HashMap<>();
        allUsersMailsSended = new HashMap<>();
    }


    public synchronized String saveMail(Mail mail) throws IOException {
        String[] reciver = mail.getReciver().split(";");
        String reciversNotFound = new String();
        String reciversFounded = new String();

        for(String r : reciver){
            String userMail = r;
            String path = "C:\\Users\\matti\\MailProject\\Users\\Received\\" + userMail + ".json";
            File file = new File(path);
            JsonArray mailList = new JsonArray();
            String jsonMailString = new Gson().toJson(mail);
            if (!file.exists()) {
                reciversNotFound = reciversNotFound.concat(r+";");

            } else {
                reciversFounded = reciversFounded.concat(r+"");
                if (allUsersMailsReceived.containsKey(userMail)){
                    allUsersMailsReceived.get(userMail).add(mail);
                }else{
                    ArrayList<Mail> firstMail = new ArrayList<>();
                    firstMail.add(mail);
                    allUsersMailsReceived.put(userMail,firstMail);
                }
                if (file.length() == 0){
                    try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                        mailList.add(jsonMailString);
                        out.write(mailList.toString());
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
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
        }
        if (!reciversFounded.isEmpty()){
            mail.setReciver(reciversFounded);
            saveMailToSender(mail);
        }
        return reciversNotFound;
    }

    public synchronized void saveMailToSender(Mail mail) throws IOException {


        String userMail = mail.getSender();
        if (!allUsersMailsSended.containsKey(userMail)){
            ArrayList<Mail> firstMail = new ArrayList<>();
            firstMail.add(mail);
            allUsersMailsSended.put(userMail, firstMail);
        }else{
            allUsersMailsSended.get(userMail).add(mail);
        }
        String path = "C:\\Users\\matti\\MailProject\\Users\\Sended\\" + userMail + ".json";
        File file = new File(path);
        JsonArray mailList = new JsonArray();
        String jsonMailString = new Gson().toJson(mail);
        if (file.length() == 0) {
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                mailList.add(jsonMailString);
                out.write(mailList.toString());
                out.flush();
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {

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

    public synchronized ArrayList<Mail> getUserMails(Message message) throws FileNotFoundException {
        AtomicBoolean findend = new AtomicBoolean(false);
        Mail lastMail;
        ArrayList<Mail> newEmails = new ArrayList<>();
        ArrayList<Mail> userEmails = new ArrayList<>();;

        if (message.getMail() != null) {
            lastMail = message.getMail();
            allUsersMailsReceived.get(message.getSender()).forEach(mail -> {
                if (mail.equals(lastMail)) {
                    findend.set(true);
                } else if (findend.get()) {
                    newEmails.add(mail);
                }
            });
            userEmails = newEmails;

        } else {
            if(allUsersMailsReceived.containsKey(message.getSender())){
                userEmails = allUsersMailsReceived.get(message.getSender());
            }


        }

        return userEmails;

    }

    public ArrayList<Mail> getUserMailsSended(Message message) throws FileNotFoundException {

        ArrayList<Mail> userEmails = new ArrayList<>();
        if (allUsersMailsSended.containsKey(message.getSender())) {
            userEmails = allUsersMailsSended.get(message.getSender());

        }
        return userEmails;

    }

    public void getALLUsersMailsRecived() throws FileNotFoundException {

        ArrayList<Mail> mailList;
        File directory = new File("C:\\Users\\matti\\MailProject\\Users\\Received");
        File[] listFile = directory.listFiles();

        for (File file : listFile) {
            if (file.length() != 0) {

                JsonArray List = JsonParser.parseReader(new JsonReader(new FileReader(file))).getAsJsonArray();
                mailList = new ArrayList<>();

                for (int i = 0; i < List.size(); i++) {

                    mailList.add(parseMailobject(List.get(i)));
                }
                allUsersMailsReceived.put(removeExtension(file.getName()), mailList);
            }
        }
    }

    public void getALLUsersMailsSended() throws FileNotFoundException {

        ArrayList<Mail> mailList;
        File directory = new File("C:\\Users\\matti\\MailProject\\Users\\Sended");
        File[] listFile = directory.listFiles();

        for (File file : listFile) {
            if (file.length() != 0){
                JsonArray List = JsonParser.parseReader(new JsonReader(new FileReader(file))).getAsJsonArray();
                mailList = new ArrayList<>();
                for (int i = 0; i < List.size(); i++) {
                    mailList.add(parseMailobject(List.get(i)));
                }
                allUsersMailsSended.put(removeExtension(file.getName()), mailList);
            }
        }

    }




    public synchronized void checkLogInfo(String userName) throws IOException {
        String pathR = "C:\\Users\\matti\\MailProject\\Users\\Received\\" + userName + ".json";
        String pathS = "C:\\Users\\matti\\MailProject\\Users\\Sended\\" + userName + ".json";
        File logFile = new File(pathR);
        if (!logFile.exists()){
            logFile.createNewFile();
            logFile = new File(pathS);
            logFile.createNewFile();
            allUsersMailsReceived.put(userName,new ArrayList<Mail>());
            allUsersMailsSended.put(userName,new ArrayList<Mail>());

        }

    }

    public synchronized void cancelMail(Mail mail, boolean sended, String userMail) throws FileNotFoundException {


        String path;
        ArrayList<Mail> mailList;
        cancelMailMap(sended, userMail, mail);
        if (sended) {
            path = "C:\\Users\\matti\\MailProject\\Users\\Sended\\" + userMail + ".json";

        } else {
            path = "C:\\Users\\matti\\MailProject\\Users\\Received\\" + userMail + ".json";

        }
        File file = new File(path);
        JsonArray List = JsonParser.parseReader(new JsonReader(new FileReader(file))).getAsJsonArray();
        mailList = new ArrayList<>();

        for (int i = 0; i < List.size(); i++) {

            mailList.add(parseMailobject(List.get(i)));

        }

        searchInArray(mailList, mail);
        JsonArray jsonArray = new JsonArray();
        String jsonMail;
        for(Mail mail1 : mailList){
            jsonMail = new Gson().toJson(mail1);
            jsonArray.add(jsonMail);
        }
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {

            out.write(jsonArray.toString());
            out.flush();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void cancelMailMap(boolean sended, String userMail, Mail mail) {
        ArrayList<Mail> mailList;
        if (sended) {
            mailList = allUsersMailsSended.get(userMail);
        } else {
            mailList = allUsersMailsReceived.get(userMail);
        }

        searchInArray(mailList, mail);

    }

    public void searchInArray(ArrayList<Mail> mailList, Mail mail) {
        boolean trovato = false;
        int i = 0;
        while (!trovato && i < mailList.size()) {
            if (mailList.get(i).equals(mail)) {
                mailList.remove(i);
                trovato = true;
            }
            i++;
        }
    }

    public Mail parseMailobject(JsonElement mail) {

        String cleanjson = removeQuotesAndUnescape(mail.getAsString());
        JsonObject obj = new Gson().fromJson(cleanjson, JsonObject.class);
        String text = obj.get("text").getAsString();
        String sender = obj.get("sender").getAsString();
        String object = obj.get("object").getAsString();
        String reciver = obj.get("reciver").getAsString();
        String date = obj.get("date").getAsString();
        String time = obj.get("time").getAsString();
        Mail mailObj = new Mail(text, sender, reciver, object, date, time);
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
