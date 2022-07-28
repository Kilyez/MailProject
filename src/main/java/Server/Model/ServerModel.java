package Server.Model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServerModel {

    private StringProperty logProperty;

    public ServerModel() {
        logProperty = new SimpleStringProperty();
        logProperty.set("");
    }

    public StringProperty logProperty() {
        return logProperty;
    }

    synchronized public void appendLogText(String log) {
        Platform.runLater(() -> {
            logProperty.set(logProperty.get() + log + "\n");
        });
    }

    public void clearLogText() {
        logProperty.set("");
    }


}
