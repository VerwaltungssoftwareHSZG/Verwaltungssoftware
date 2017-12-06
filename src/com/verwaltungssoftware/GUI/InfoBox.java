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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class InfoBox {
    
    public static void display(){
        Stage popupStage = new Stage();
        
        //blockieren von aktionen
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Informationen ändern");
        popupStage.setMinWidth(300);
        
        Label nameUnternehmen = new Label("Name des Unternehmens");
        Label adresse = new Label("Adresse/Hauptsitz");
        Label ort = new Label("Ort");
        Label plz = new Label("Postleitzahl");
        Label inhaber = new Label("Inhaber");
        Label telefon = new Label("Telefon");
        Label fax = new Label("Fax");
        Label bank = new Label("Bankinstitut");
        Label kontonummer = new Label("Kontonummer");
        Label bic = new Label("BIC");
        Label iban = new Label("IBAN");
        Label pw = new Label("Platzhalter");
        Label pwconfirm = new Label("Platzhalter");
        Label pwconfirm2 = new Label("Aktuelles Passwort");
        
        TextField nameUF = new TextField();
        TextField adresseF = new TextField();
        TextField ortF = new TextField();
        TextField plzF = new TextField();
        TextField inhaberF = new TextField();
        TextField telefonF = new TextField();
        TextField faxF = new TextField();
        TextField bankF = new TextField();
        TextField bicF = new TextField();
        TextField ibanF = new TextField();
        TextField pwF = new TextField();
        TextField pwconfirmF = new TextField();
        TextField pwconfirmF2 = new TextField();
        
        Button confirm = new Button("Bestätigen");
        confirm.setOnAction(e -> popupStage.close());
        Button back = new Button("Abbrechen");
        back.setOnAction(e -> popupStage.close());
        
        BorderPane border = new BorderPane();
        VBox left = new VBox();
        left.setPadding(new Insets(10));
        left.setSpacing(16);
        VBox right = new VBox();
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setSpacing(8);
        bottom.setAlignment(Pos.CENTER);
        
        left.getChildren().addAll(nameUnternehmen, adresse, ort, plz, inhaber, telefon, fax, bank, bic, iban, pw, pwconfirm, pwconfirm2);
        right.getChildren().addAll(nameUF, adresseF, ortF, plzF, inhaberF, telefonF, faxF, bankF, bicF, ibanF, pwF, pwconfirmF, pwconfirmF2);
        bottom.getChildren().addAll(back, confirm);
        border.setLeft(left);
        border.setCenter(right);
        border.setBottom(bottom);
        
        Scene scene = new Scene(border, 300, 500);
        popupStage.setScene(scene);
        popupStage.show();
        
        
        
        
        
    }
    
}
