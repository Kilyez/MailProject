package ServerViewAndController;

import Server.Model.Server;
import Server.Model.ServerModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ServerController {

    private Server server;

    private ServerModel serverModel;
    @FXML
    private TextArea logField;



    @FXML
    private void initialize() {
        // Your code goes here
        serverModel = new ServerModel();
        server = new Server(7777, serverModel);
        server.RunServer();
        logField.textProperty().bind(serverModel.logProperty());



    }

    public void handleClearButton(){
        serverModel.clearLogText();
    }

    public void closeServer() throws IOException {
        server.CloseServer();
    }


}
