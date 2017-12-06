/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forschungsprojekt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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
public class KundenAdd {
   static Scene kundenauswahl, kundeninfoP, kundeninfoG;
    public static void display(){
        Stage popupStage = new Stage();
        
        
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Kunde hinzufügen");
        popupStage.setMinWidth(300);
        
        Button privatkunde = new Button("Privatkunde");
        Button geschKunde = new Button("Geschäftskunde");
        privatkunde.setOnAction(e -> popupStage.setScene(kundeninfoP));
        geschKunde.setOnAction(e -> popupStage.setScene(kundeninfoG));
        privatkunde.setPadding(new Insets(20, 20, 20, 20));
        geschKunde.setPadding(new Insets(20, 20, 20, 20));
        
        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(8);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(privatkunde, geschKunde);
        
        kundenauswahl = new Scene(box, 200, 150);
        
        Label anrede = new Label("Anrede");
        Label vorname = new Label("Vorname:");
        Label nachname = new Label("Nachname:");
        Label adresse = new Label("Adresse:");
        Label ort = new Label("Ort:");
        Label plz = new Label("Postleitzahl:");
        
        String h = "Herr";
        String f = "Frau";
        ChoiceBox anredeChoice = new ChoiceBox();
        anredeChoice.getItems().addAll(h, f);
        anredeChoice.setValue(h);
        
        TextField vornameT = new TextField();
        TextField nachnameT = new TextField();
        TextField adresseT = new TextField();
        TextField ortT = new TextField();
        TextField plzT = new TextField();
        
        Button confirm = new Button("Bestätigen");
        confirm.setOnAction(e -> popupStage.close());
        Button back = new Button("Abbrechen");
        back.setOnAction(e -> popupStage.setScene(kundenauswahl));
        
        BorderPane border = new BorderPane();
        VBox left = new VBox();
        left.setPadding(new Insets(10));
        left.setSpacing(16);
        VBox right = new VBox();
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(8);
        hbox.setAlignment(Pos.CENTER);
        
        hbox.getChildren().addAll(back, confirm);
        left.getChildren().addAll(anrede, vorname, nachname, adresse, ort, plz);
        right.getChildren().addAll(anredeChoice, vornameT, nachnameT, adresseT, ortT, plzT);
        border.setLeft(left);
        border.setCenter(right);
        border.setBottom(hbox);
        
        kundeninfoP = new Scene(border, 300, 250);
        
        Label unternehmen = new Label("Name des Unternehmen:");
        Label hauptsitz = new Label("Adresse des Hauptsitz:");
        Label ort2 = new Label("Ort:");
        Label plz2 = new Label("Postleitzahl:");
        
        TextField unternehmenT = new TextField();
        TextField hauptsitzT = new TextField();
        TextField ort2T = new TextField();
        TextField plz2T = new TextField();
        
        Button confirm2 = new Button("Bestätigen");
        confirm2.setOnAction(e -> popupStage.close());
        Button back2 = new Button("Abbrechen");
        back2.setOnAction(e -> popupStage.setScene(kundenauswahl));
        
        BorderPane border2 = new BorderPane();
        VBox left2 = new VBox();
        left2.setPadding(new Insets(10));
        left2.setSpacing(16);
        VBox right2 = new VBox();
        right2.setPadding(new Insets(10));
        right2.setSpacing(8);
        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(10, 10, 10, 10));
        hbox2.setSpacing(8);
        hbox2.setAlignment(Pos.CENTER);
        
        hbox2.getChildren().addAll(back2, confirm2);
        left2.getChildren().addAll(unternehmen, hauptsitz, ort2, plz2);
        right2.getChildren().addAll(unternehmenT, hauptsitzT, ort2T, plz2T);
        border2.setLeft(left2);
        border2.setCenter(right2);
        border2.setBottom(hbox2);
        
        kundeninfoG = new Scene(border2, 300, 200);
        
        popupStage.setScene(kundenauswahl);
        popupStage.show();
    }
}
