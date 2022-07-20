package com.example.mailproject;

import Client.Client;
import Messages.Mail;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class ClientController {
    @FXML
    private Label maillbl;
    @FXML
    private ListView<Mail> MailListView;
    @FXML
    private ListView<Mail> mailSendedView;
    @FXML
    private Pane mailView;
    @FXML
    private Pane writeMail;
    @FXML
    private Pane displayMail;
    @FXML
    private Pane sendedMail;
    @FXML
    private TextField senderMail;
    @FXML
    private TextField objectMail;
    @FXML
    private TextField toMails;
    @FXML
    private TextArea mailText;
    @FXML
    private ImageView imageView;

    private String email;
    private Client clientModel;

    @FXML
    private void initialize() {
        // Your code goes here
        File file  = new File("C:\\Users\\matti\\MailProject\\image\\iconaTrial.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        MailListView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {
                CellMail cell = new CellMail(false);
                return cell;
            }
        });
        mailSendedView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {

                CellMail cell = new CellMail(true);
                return cell;
            }
        });

    }




    private void startCLient() throws InterruptedException, IOException {
        clientModel = new Client(email);
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(false);
        sendedMail.setVisible(false);


    }


    public void setEmail(String mail) throws InterruptedException, IOException {
        email = mail;
        maillbl.setText(email);
        startCLient();
    }

    public void openScriviPanel(ActionEvent actionEvent) {
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(true);
        sendedMail.setVisible(false);
        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }
    }

    public void openRicevutiPanel(ActionEvent actionEvent) throws InterruptedException {
        mailView.setVisible(true);
        displayMail.setVisible(false);
        writeMail.setVisible(false);
        sendedMail.setVisible(false);

        MailListView.setItems(clientModel.getObsRecivedMailList());
        clientModel.recivedMailRefresh();

        System.out.println(clientModel.getObsRecivedMailList().size());
        MailListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mail>() {
            @Override
            public void changed(ObservableValue<? extends Mail> observableValue, Mail mail, Mail t1) {
                ObservableList obsSelected =  MailListView.getSelectionModel().getSelectedItems();
                Mail mailSelected =(Mail) obsSelected.get(0);
                mailView.setVisible(false);
                displayMail.setVisible(true);
                senderMail.setText(mailSelected.getSender());
                objectMail.setText(mailSelected.getObject());
                mailText.setText(mailSelected.getText());
                toMails.setText(mailSelected.getReciver());

            }
        });
    }

    public void openInviatiPanel(ActionEvent actionEvent) {
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(false);
        sendedMail.setVisible(true);

        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }

        System.out.println(clientModel.getObsSendedMailList());

        mailSendedView.setItems(clientModel.getObsSendedMailList());

        mailSendedView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mail>() {
            @Override
            public void changed(ObservableValue<? extends Mail> observableValue, Mail mail, Mail t1) {
                ObservableList obsSelected =  mailSendedView.getSelectionModel().getSelectedItems();
                Mail mailSelected =(Mail) obsSelected.get(0);
                sendedMail.setVisible(false);
                displayMail.setVisible(true);
                senderMail.setText(mailSelected.getSender());
                objectMail.setText(mailSelected.getObject());
                mailText.setText(mailSelected.getText());
                toMails.setText(mailSelected.getReciver());

            }
        });

    }
}
