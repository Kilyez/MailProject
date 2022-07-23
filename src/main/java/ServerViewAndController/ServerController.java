package ServerViewAndController;

import Server.Model.Server;
import Server.Model.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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
}
