module com.example.mailproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.mailproject to javafx.fxml;
    exports com.example.mailproject;
}