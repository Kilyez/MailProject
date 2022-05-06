package Server;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private ServerSocket server;
    private Socket s;

    public Server(int port){
        this.port = port;
        if(!startServer()){
            System.out.println("Errore nella creazione del server");
        }
    }

    private Boolean startServer(){
        try {
            server = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
            return false;

        }
        System.out.println("Server creato con successo");
        return true;
    }

    public void RunServer(){
        while (true)
        {
            try
            {
                System.out.println("Server in attesa di richieste...");
                s = server.accept();


                System.out.println("Un client si e' connesso...");
                JSONObject json = receiveJSON();
                System.out.println(json);
                OutputStream sOut = s.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sOut));

                // Il server invia la risposta al client
                bw.write("Benvenuto sul server!n");

                // Chiude lo stream di output e la connessione
                bw.close();

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    public void CloseServer() throws IOException {
        try {
            s.close();
            server.close();
            System.out.println("Chiusura connessione effettuata");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public JSONObject receiveJSON() throws IOException {
        InputStream in = s.getInputStream();
        ObjectInputStream i = new ObjectInputStream(in);
        JSONObject line = null;
        try {
            line = (JSONObject) i.readObject();

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        JSONObject jsonObject = new JSONObject(line);
        System.out.println("Got from client on port " + s.getPort() + " " + jsonObject.get("key").toString());
        return jsonObject;
    }

}
