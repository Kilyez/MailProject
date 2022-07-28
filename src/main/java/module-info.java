module MailProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.google.gson;
    requires java.sql;
    requires org.apache.commons.text;


    opens ServerViewAndController to javafx.fxml;

    opens com.example.ClientViewAndController to javafx.fxml;

    opens Messages to com.google.gson;

    exports Messages;
    exports Client.Model;
    exports Server.Model;
    exports  ServerViewAndController;
    exports com.example.ClientViewAndController;

}