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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class AngebotAdd {
   static Scene kundenInfo, übernahme, posten, summe;
     public static void display(){
         String titleK = "Angebot erstellen: Kundendaten";
         String titleÜ = "Angebot erstellen: Übernahme";
         String titleP = "Angebot erstellen: Posten hinzufügen";
         String titleS = "Angebot erstellen: Summe";
         
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(titleK);
        
        Label aNr = new Label("Angebotsnummer");
        Label kNr = new Label("Kundennummer");
        Label anredeL = new Label("Anrede");
        ChoiceBox anrede = new ChoiceBox();
        Label vorname = new Label("Vorname");
        Label name = new Label("Nachname");
        Label straße = new Label("Straße");
        Label plz = new Label("PLZ");
        Label ort = new Label("Ort");
        Label rabatt = new Label("Rabatt");
        Label datum = new Label("Datum");
        Label kundenname = new Label("Kundenname");
        
        String h = "Herr";
        String f = "Frau";
        anrede.getItems().addAll(h, f);
        anrede.setValue(h);
        
        TextField aNRT = new TextField();
        TextField kNRT = new TextField();
        TextField vornameT = new TextField();
        TextField nameT = new TextField();
        TextField straßeT = new TextField();
        TextField plzT = new TextField();
        TextField ortT = new TextField();
        TextField rabattT = new TextField();
        TextField datumT = new TextField();
        TextField kundennameT = new TextField();
        
        Button cancel = new Button("Abbrechen");
        cancel.setOnAction(e -> popupStage.close());
        Button cont = new Button("Weiter");
        cont.setOnAction(e -> {
            popupStage.setScene(übernahme);
            popupStage.setTitle(titleÜ);
        });
        
        VBox sumL = new VBox();
        sumL.getChildren().addAll(aNr, kNr, anredeL, vorname, name, straße, plz, ort, rabatt, datum, kundenname);
        sumL.setPadding(new Insets(10));
        sumL.setSpacing(16);
        
        VBox sumT = new VBox();
        sumT.getChildren().addAll(aNRT, kNRT, anrede, vornameT, nameT, straßeT, plzT, ortT, rabattT, datumT, kundennameT);
        sumT.setPadding(new Insets(10));
        sumT.setSpacing(8);
        
        HBox buttons = new HBox();
        buttons.getChildren().addAll(cancel, cont);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(8);
        buttons.setAlignment(Pos.CENTER);
        
        BorderPane pane = new BorderPane();
        pane.setLeft(sumL);
        pane.setCenter(sumT);
        pane.setBottom(buttons);
        kundenInfo = new Scene(pane, 350, 450); //KUNDENINFO ENDE
        
        TableView aAndR = new TableView();
        TableView aFromAR = new TableView();
        aAndR.setEditable(true);
        aFromAR.setEditable(true);
        TableColumn artTC = new TableColumn("Art");
        TableColumn nummerTC = new TableColumn("Nummer");
        TableColumn kNrTC = new TableColumn("Kundennummer");
        TableColumn datumTC = new TableColumn("Datum");
        TableColumn kundennameTC = new TableColumn("Kundenname");
        aAndR.getColumns().addAll(artTC, nummerTC, kNrTC, datumTC, kundennameTC);
        
        TableColumn posTC = new TableColumn("Position");
        TableColumn artTC2 = new TableColumn("Artikelnummer");
        TableColumn bezeichnungTC = new TableColumn("Bezeichnung");
        TableColumn preisTC = new TableColumn("Preis");
        aFromAR.getColumns().addAll(posTC, artTC2, bezeichnungTC, preisTC);
        
        Button add = new Button("Hinzufügen");
        Button add2 = new Button("Hinzufügen");
        Button back = new Button("Zurück");
        back.setOnAction(e -> {
            popupStage.setScene(kundenInfo);
            popupStage.setTitle(titleK);
                    
                    });
        Button conti = new Button("Weiter");
        conti.setOnAction(e -> {
            popupStage.setScene(posten);
            popupStage.setTitle(titleP);
        });
        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(aAndR);
        scroll.setPrefSize(320, 180);
        ScrollPane scroll2 = new ScrollPane();
        scroll2.setContent(aFromAR);
        scroll2.setPrefSize(320, 180);
        
        HBox t1 = new HBox();
        t1.setPadding(new Insets(10));
        t1.setSpacing(8);
        HBox t2 = new HBox();
        t2.setPadding(new Insets(10));
        t2.setSpacing(8);
        t1.getChildren().addAll(scroll, add);
        t2.getChildren().addAll(scroll2, add2);
        

        
        VBox tables = new VBox();
        tables.getChildren().addAll(t1, t2);
        tables.setSpacing(20);
        
        HBox buttons2 = new HBox();
        buttons2.getChildren().addAll(back, conti);
        buttons2.setPadding(new Insets(10, 10, 10, 10));
        buttons2.setSpacing(8);
        buttons2.setAlignment(Pos.CENTER);
        
        BorderPane pane2 = new BorderPane();
        pane2.setCenter(tables);
        pane2.setBottom(buttons2);
        
        übernahme = new Scene(pane2, 500, 500); //ÜBERNAHME ENDE
        
        TableView angebotEntwurf = new TableView();
        TableColumn posiTC = new TableColumn("Position");
        TableColumn art2TC = new TableColumn("Art");
        TableColumn artikelnrTC = new TableColumn("Artikelnummer");
        TableColumn bezeichTC = new TableColumn("Bezeichnung");
        TableColumn zusatztextTC = new TableColumn("Zusatztext");
        TableColumn anzahlTC = new TableColumn("Anzahl");
        TableColumn einzelpreisTC = new TableColumn("Einzelpreis");
        TableColumn bruttopreisTC = new TableColumn("Bruttoeinzelpreis");
        TableColumn gesamtTC = new TableColumn("Gesamtpreis");
        angebotEntwurf.getColumns().addAll(posiTC, art2TC, artikelnrTC, bezeichTC, zusatztextTC, anzahlTC, einzelpreisTC, bruttopreisTC, gesamtTC);
        
        ScrollPane entwurfScroll = new ScrollPane();
        entwurfScroll.setContent(angebotEntwurf);
        
        Label artNr = new Label("Artikelnummer");
        Label anzahl = new Label("Anzahl");
        Label nettopreis = new Label("Nettopreis");
        Label summe = new Label("Summe");
        Label rabatt2 = new Label("Rabatt");
        Label zusatztext = new Label("Zusatztext");
        
        CheckBox alternativ = new CheckBox("Alternativ");
        
        TextField artNr2 = new TextField();
        TextField anzahl2 = new TextField();
        TextField nettopreis2 = new TextField();
        TextField summe2 = new TextField();
        TextField rabatt3 = new TextField();
        TextField zusatztext2 = new TextField();
        
        Button search = new Button("Suchen");
        Button back2 = new Button("Zurück");
        back2.setOnAction(e -> {
            popupStage.setScene(übernahme);
            popupStage.setTitle(titleÜ);
        });
        Button add3 = new Button("Hinzufügen");
        Button con = new Button("Weiter");
        
       VBox labelsLeft = new VBox();
       labelsLeft.getChildren().addAll(artNr, anzahl, nettopreis, summe);
       labelsLeft.setPadding(new Insets(10));
       labelsLeft.setSpacing(16);
       VBox textLeft = new VBox();
       textLeft.getChildren().addAll(artNr2, anzahl2, nettopreis2, summe2);
       textLeft.setPadding(new Insets(10));
       textLeft.setSpacing(8);
       
       VBox labelsRight = new VBox();
       labelsRight.getChildren().addAll(alternativ, rabatt2, zusatztext);
       labelsRight.setPadding(new Insets(10));
       labelsRight.setSpacing(16);
       VBox textRight = new VBox();
       textRight.getChildren().addAll(search, rabatt3, zusatztext2);
       textRight.setPadding(new Insets(10));
       textRight.setSpacing(8);
       
       HBox leftRight = new HBox();
       leftRight.getChildren().addAll(labelsLeft, textLeft, labelsRight, textRight);
        
        HBox buttons3 = new HBox();
        buttons3.getChildren().addAll(back2, add3, con);
        buttons3.setPadding(new Insets(10, 10, 10, 10));
        buttons3.setSpacing(8);
        buttons3.setAlignment(Pos.CENTER);
        
        VBox tablePosten = new VBox();
        tablePosten.getChildren().addAll(entwurfScroll, leftRight);
        
        BorderPane pane3 = new BorderPane();
        pane3.setCenter(tablePosten);
        pane3.setBottom(buttons3);
        
        posten = new Scene(pane3, 700, 500); //ENDE POSTEN
        
        popupStage.setScene(kundenInfo);
        popupStage.show();
        
     }
}
