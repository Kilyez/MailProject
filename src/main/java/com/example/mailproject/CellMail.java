package com.example.mailproject;

import Messages.Mail;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class CellMail extends ListCell<Mail> {
    Boolean mailsended;
    public CellMail(Boolean sended){
        mailsended = sended;
    }

    @Override
    protected void updateItem(Mail item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            Label mail;
            if (mailsended){
                mail = new Label(item.getReciver());
            }else{
                mail = new Label(item.getMailPreview());
            }

            Label object = new Label(item.getObject());
            Label date = new Label(item.getDate());
            Button cancel = new Button("x");
            cancel.setStyle("-fx-background-color: rgba(190,0,0,0.92)");
            GridPane gridPane = new GridPane();
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(30);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(40);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(15);
            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPercentWidth(5);
            gridPane.getColumnConstraints().addAll(col,col2,col3,col4);
            gridPane.add(mail, 0, 0, 1, 1);
            gridPane.add(object, 1, 0, 1, 1);
            gridPane.add(date, 2, 0, 1, 1);
            gridPane.add(cancel, 3, 0, 1, 1);

            setGraphic(gridPane);

        } else {
            setGraphic(null);
        }
    }
}

