package Messages;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.*;


public class RecordMessagges {

    Message message;

    public RecordMessagges() {

    }

    public void filterMessage() throws IOException {
        if(message.getType() == "Mail"){
            saveMail(message.getMail());
        }

    }

    public synchronized void saveMail(Mail mail) throws IOException {


        String userMail = mail.getReciver();

        String path = "C:\\Users\\matti\\MailProject\\Users\\" + userMail + ".json";
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {

                String jsonMailString = new Gson().toJson(mail);
                out.write(jsonMailString);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Object obj = JsonParser.parse(new FileReader(path));

            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {

                String jsonMailString = new Gson().toJson(mail);
                out.append(jsonMailString);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


}
