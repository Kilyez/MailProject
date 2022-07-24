package ServerViewAndController;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("ServerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 385, 320);
        ServerController controller = fxmlLoader.getController();
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    controller.closeServer();
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("Impossibile chiudere il client");
                }

            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}