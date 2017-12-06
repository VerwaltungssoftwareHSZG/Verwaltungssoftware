/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verwaltungssoftware.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class PasswordChange {
    static Scene passChange;
    public static void display(){
        Stage popupStage = new Stage();
        
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Passwort ändern");
        
        Label old = new Label("Aktuelles Passwort");
        Label neu = new Label("Neues Passwort");
        Label neu2 = new Label("Neues Passwort bestätigen");
        
        PasswordField oldT = new PasswordField();
        PasswordField neuT = new PasswordField();
        PasswordField neu2T = new PasswordField();
        
        Button cancel = new Button("Abbrechen");
        Button confirm = new Button("Bestätigen");
        cancel.setOnAction(e -> popupStage.close());
        confirm.setOnAction(e -> popupStage.close());
        
        VBox left = new VBox();
        VBox right = new VBox();
        left.setPadding(new Insets(10));
        left.setSpacing(16);
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        
        left.getChildren().addAll(old, neu, neu2);
        right.getChildren().addAll(oldT, neuT, neu2T);
        
        HBox bottom = new HBox();
        bottom.getChildren().addAll(cancel, confirm);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setSpacing(8);
        bottom.setAlignment(Pos.CENTER);
        
        BorderPane pane = new BorderPane();
        pane.setLeft(left);
        pane.setCenter(right);
        pane.setBottom(bottom);
        
        
        passChange = new Scene(pane, 350, 150);
        popupStage.setScene(passChange);
        popupStage.show();
    }
}
