package com.example.ClientViewAndController;

import Client.Model.Client;
import Messages.Mail;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

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
    private TextField forwardField;
    @FXML
    private TextArea mailText;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField mailObjectTxt;
    @FXML
    private TextField mailReciverTxt;
    @FXML
    private TextArea mailTextTxt;
    @FXML
    private AnchorPane switchPane;
    @FXML
    private Label senderFieldErr;
    @FXML
    private Label objectFieldErr;
    @FXML
    private Label textFieldErr;
    @FXML
    private Button replyBtn;
    @FXML
    private Button replyAllBtn;
    @FXML
    private TextField popUp;
    @FXML
    private Label erReciversNotFound;
    @FXML
    private Circle onlineCircle;
    @FXML
    private Label mailNotSended;
    @FXML
    private Label serverOnLable;

    private BooleanProperty isConnected;
    private String email;
    private Client clientModel;



    @FXML
    private void initialize() {
        // Your code goes here
        File file  = new File("C:\\Users\\matti\\MailProject\\image\\iconaTrial.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        isConnected = new SimpleBooleanProperty();
        ClientController c = this;
        MailListView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {
                CellMail cell = new CellMail(false,c);
                return cell;
            }
        });
        mailSendedView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {

                CellMail cell = new CellMail(true,c);
                return cell;
            }
        });



    }


    private void startCLient() throws InterruptedException {
        clientModel = new Client(email);
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(false);
        sendedMail.setVisible(false);
        clientModel.logIn();
        isConnected.bind(clientModel.isConnectedProperty());
        if (isConnected.get()){
            onlineCircle.setStyle("-fx-fill: green");
        }else{
            onlineCircle.setStyle("-fx-fill: red");

        }
        isConnected.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                onlineCircle.setStyle("-fx-fill: green");
                if(writeMail.isVisible()){
                    serverOnLable.setVisible(true);
                    PauseTransition visiblePause = new PauseTransition(
                            Duration.seconds(8)
                    );
                    visiblePause.setOnFinished(
                            event -> serverOnLable.setVisible(false)
                    );
                    visiblePause.play();
                }
            }else{
                onlineCircle.setStyle("-fx-fill: red");
            }
        });
        mailNotSended.textProperty().bindBidirectional(clientModel.getErMessageNotSend());
        mailNotSended.textProperty().addListener((ov, t, t1) -> {
            if (!t1.isEmpty()){
                mailNotSended.setVisible(true);
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(8)
                );
                visiblePause.setOnFinished(
                        event -> {
                            mailNotSended.setVisible(false);
                            mailNotSended.setText("");
                        }

                );
                visiblePause.play();
            }
        });
        erReciversNotFound.textProperty().bindBidirectional(clientModel.getErReciversNotFounded());
        erReciversNotFound.textProperty().addListener((ov, old, val) -> {
            if (!val.isEmpty()) {

                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(8)
                );
                visiblePause.setOnFinished(
                        event -> erReciversNotFound.setText("")
                );
                visiblePause.play();
            }
        });


    }

    public void handleButtonCancel(Mail mailSelected, Boolean sended){
        clientModel.cancelMail(mailSelected,sended);

    }

    public void closeWindow() throws IOException {
        clientModel.closeClient();
    }

    public void setEmail(String mail) throws InterruptedException, IOException {
        email = mail;
        maillbl.setText(email);
        startCLient();
    }

    public void openScriviPanel(ActionEvent actionEvent) {
        mailView.setVisible(false);
        displayMail.setVisible(false);
        if (isConnected.get()){
            writeMail.setVisible(true);
        }else{
            writeMail.setVisible(false);
        }
        sendedMail.setVisible(false);
        clearForm();
        clearErrorLable();
        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }


    }

    public void clearForm(){

        mailTextTxt.clear();
        mailObjectTxt.clear();
        mailReciverTxt.clear();

    }

    public void clearErrorLable(){

        senderFieldErr.setText("");
        objectFieldErr.setText("");
        textFieldErr.setText("");
    }

    public void openRicevutiPanel(ActionEvent actionEvent) throws InterruptedException {

        mailView.setVisible(true);
        displayMail.setVisible(false);
        writeMail.setVisible(false);
        sendedMail.setVisible(false);
        clientModel.recivedMailRefresh();
        MailListView.setItems((clientModel.getObsRecivedMailList()));
        MailListView.getSelectionModel().clearSelection();
        MailListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mail>() {
            @Override
            public void changed(ObservableValue<? extends Mail> observableValue, Mail mail, Mail t1) {
                Mail mailSelected = MailListView.getSelectionModel().getSelectedItem();
                if (mailSelected != null) {
                    mailView.setVisible(false);
                    displayMail.setVisible(true);
                    writeMail.setVisible(false);
                    sendedMail.setVisible(false);
                    if (!writeMail.isVisible()) {
                        clearForm();
                    }
                    clientModel.stopRefresh();
                    senderMail.setText(mailSelected.getSender());
                    objectMail.setText(mailSelected.getObject());
                    mailText.setText(mailSelected.getText());
                    toMails.setText(mailSelected.getReciver());
                    replyAllBtn.setVisible(true);
                    replyBtn.setVisible(true);
                }
            }
        });

        clientModel.getObsRecivedMailList().addListener(new ListChangeListener<Mail>() {
            @Override
            public void onChanged(Change<? extends Mail> change) {

                while (change.next()) {

                    if (clientModel.getObsRecivedMailList().size() > change.getAddedSize()) {
                        if (change.wasAdded()) {
                            poPupShow(change.getAddedSize());
                        }
                    }
                }
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
        mailSendedView.getSelectionModel().clearSelection();
        mailSendedView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mail>() {
            @Override
            public void changed(ObservableValue<? extends Mail> observableValue, Mail mail, Mail t1) {
                Mail mailSelected =  mailSendedView.getSelectionModel().getSelectedItem();
                if (mailSelected != null){
                    sendedMail.setVisible(false);
                    displayMail.setVisible(true);
                    writeMail.setVisible(false);
                    mailView.setVisible(false);
                    if (!writeMail.isVisible()){
                        clearForm();
                    }
                    senderMail.setText(mailSelected.getSender());
                    objectMail.setText(mailSelected.getObject());
                    mailText.setText(mailSelected.getText());
                    toMails.setText(mailSelected.getReciver());
                    replyBtn.setVisible(false);
                    replyAllBtn.setVisible(false);
                }
            }
        });


    }

    public void handleReload(){
        clientModel.getRecivedMail();
    }

    public void handleInviaBtn() throws IOException {
        String[] recivers;
        if (mailReciverTxt.getText() == ""){
            recivers = null;
        }else if(mailReciverTxt.getText().contains(";")){
            recivers = mailReciverTxt.getText().split(";");
        }else {
            recivers = new String[1];
            recivers[0] = mailReciverTxt.getText();
        }
        if(CheckValidField(recivers,mailObjectTxt.getText(),mailTextTxt.getText())){

            Mail mail = new Mail(mailTextTxt.getText(), email, mailReciverTxt.getText(), mailObjectTxt.getText());
            clientModel.SendMail(mail);

            if (isConnected.get()){
                clearForm();
                clearErrorLable();
            }
        }

    }

    public boolean CheckValidField(String[] reciver, String object, String text){
        boolean correctFields = true;
        if (reciver == null){
            senderFieldErr.setText("Inserire la mail del destinatario/i");
            correctFields = false;
        }else{
            for (String s : reciver) {
                if(!isValid(s)){
                    senderFieldErr.setText("La mail " + s + " non e` valida");
                    correctFields = false;
                }
            }
        }
        if (object == ""){
            objectFieldErr.setText("Inserire oggetto della mail");
            correctFields = false;
        }
        if (text == ""){
            textFieldErr.setText("Inserire il testo della mail");
            correctFields = false;
        }
        return correctFields;
    }

    private static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        System.out.println(email);
        return pat.matcher(email).matches();
    }

    public void handleReplyAllButton(){
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(true);
        sendedMail.setVisible(false);
        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }
        String[] recivers = toMails.getText().split(";");
        for(String r : recivers){
            if(!(r.equals(email))){
                mailReciverTxt.appendText(r + ";");
            }
        }
        mailObjectTxt.appendText("re:");
        mailReciverTxt.appendText(senderMail.getText() + ";");
        mailTextTxt.appendText("--------------------------------------------------------------"+
                "\n"+"DA:" + "\n" + senderMail.getText() + "\n" + "Testo:" + "\n" + mailText.getText() + "\n"
                + "--------------------------------------------------------------" + "\n\n" + "RISPOSTA:" + "\n");


    }

    public void handleReplyButton(){
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(true);
        sendedMail.setVisible(false);
        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }
        mailObjectTxt.appendText("re:");
        mailReciverTxt.appendText(senderMail.getText() + ";");
        mailTextTxt.appendText("--------------------------------------------------------------"+
                "\n"+"DA:" + "\n" + senderMail.getText() + "\n" + "Testo:" + "\n" + mailText.getText() + "\n"
                + "--------------------------------------------------------------" + "\n\n" + "RISPOSTA:" + "\n");

    }

    public void handleForwardButton(){
        mailView.setVisible(false);
        displayMail.setVisible(false);
        writeMail.setVisible(true);
        sendedMail.setVisible(false);
        if(!mailView.isVisible()){
            clientModel.stopRefresh();
        }
        mailReciverTxt.appendText(forwardField.getText());
        mailObjectTxt.appendText(objectMail.getText());
        mailTextTxt.appendText("FORWARDED\n" + "----------------------------------------------------------------\n" + mailText.getText()
                                + "\n----------------------------------------------------------------");
        forwardField.clear();

    }

    public void poPupShow(int i){
        if(i == 1){
            popUp.setText("è arrivata una nuova mail!");

        }else {
            popUp.setText("sono arrivate " + i + " nuove mail!");
        }
        popUp.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(8)
        );
        visiblePause.setOnFinished(
                event -> popUp.setVisible(false)
        );
        visiblePause.play();


    }


}
