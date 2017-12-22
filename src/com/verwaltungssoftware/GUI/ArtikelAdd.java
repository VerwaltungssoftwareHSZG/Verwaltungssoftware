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
public class ArtikelAdd {
    static Scene artikelInfo;
    public static void display(){
        Stage popupStage = new Stage();
        
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Artikel hinzufügen");
        
        Label name = new Label("Bezeichnung");
        Label ztext = new Label("Zusatztext");
        Label artNr = new Label("Artikelnummer");
        Label gruppeL = new Label("Warengruppe");
        Label preis = new Label("Einkaufspreis");
        Label preisV = new Label("Verkaufspreis");
        Label bestand = new Label("Bestand");
        
        
        
        ChoiceBox gruppe = new ChoiceBox();
        gruppe.getItems().addAll("Steine", "Rohre", "Zäune", "Kabel");
        gruppe.setValue("Steine");
        
        TextField nameT = new TextField();
        TextField ztextT = new TextField();
        TextField artNrT = new TextField();
        TextField preisET = new TextField();
        TextField preisVT = new TextField();
        TextField bestandT = new TextField();
        
        Button cancel = new Button("Abbrechen");
        cancel.setOnAction(e -> popupStage.close());
        Button confirm = new Button("Bestätigen");
        confirm.setOnAction(e -> popupStage.close());
        
        VBox left = new VBox();
        VBox right = new VBox();
        left.setPadding(new Insets(10));
        left.setSpacing(16);
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        
        left.getChildren().addAll(name, ztext, artNr, gruppeL, preis, preisV, bestand);
        right.getChildren().addAll(nameT, ztextT, artNrT, gruppe, preisET, preisVT, bestandT);
        
        HBox bottom = new HBox();
        bottom.getChildren().addAll(cancel, confirm);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setSpacing(8);
        bottom.setAlignment(Pos.CENTER);
        
        BorderPane pane = new BorderPane();
        pane.setLeft(left);
        pane.setCenter(right);
        pane.setBottom(bottom);
        
        artikelInfo = new Scene(pane, 350, 300);
        popupStage.setScene(artikelInfo);
        popupStage.show();
    }
}
