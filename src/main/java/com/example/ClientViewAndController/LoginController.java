package com.example.ClientViewAndController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    public TextField emailTxt;
    @FXML
    private Label errlbl;

    @FXML
    private void initialize() {
        // Your code goes here

    }

    @FXML
    private void handleButtonEvent(ActionEvent  e) throws IOException, InterruptedException {
        if (emailTxt.getText() == "" ){
            errlbl.setText("Mail field is empty");
        }else if(!isValid(emailTxt.getText())){
            errlbl.setText("Mail not valid");
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientView.fxml"));
            Parent newRoot = loader.load();
            ClientController clientController = loader.getController();
            clientController.setEmail(emailTxt.getText());
            Stage primaryStage = (Stage) emailTxt.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            primaryStage.setScene(scene);


        }
    }


    private static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        System.out.println(email);
        return pat.matcher(email).matches();
    }
}
