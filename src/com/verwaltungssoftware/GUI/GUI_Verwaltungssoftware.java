/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forschungsprojekt;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class GUI_Verwaltungssoftware extends Application {
    Scene loginScreen, mainScreen;
    VBox kundenT = createTableKunde();
    VBox artikelT = createTableArtikel();
    VBox angebotT = createTableAngebot();
    VBox rechnungT = createTableRechnung();
    @Override
    public void start(Stage primaryStage) {
        
        Label benutzer = new Label("Benutzer:");
        Label passwort = new Label("Passwort:");
        //Label welcome = new Label("Willkommen! Bitte loggen sie sich ein.");
        
        TextField user = new TextField();
        PasswordField pass = new PasswordField();
        Button submit = new Button("Anmelden");
        user.setPromptText("Benutzername");
        pass.setPromptText("Passwort");

        submit.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                primaryStage.setScene(mainScreen);
              /*  if(user.getText().equals("testuser") && pass.getText().equals("test")){
                    primaryStage.setScene(mainScreen);
                }
                else{System.out.println(user.getText()+ pass.getText());}*/
            }
        });
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(8);
        grid.setHgap(8);
        
        grid.add(benutzer, 1, 1);
        grid.add(passwort, 1, 2);
        grid.add(user, 2, 1);
        grid.add(pass, 2, 2);
        grid.add(submit, 2, 3);

        grid.setAlignment(Pos.CENTER);

        loginScreen = new Scene(grid, 400, 400); //Login screen

        MenuBar menu = new MenuBar();
        Menu allgemein = new Menu("Allgemein");
        Menu kunde = new Menu("Kunde");
        Menu artikel = new Menu("Artikel");
        Menu angebot = new Menu("Angebot");
        Menu rechnung = new Menu("Rechnung");
        
        MenuItem infoChange = new MenuItem("Informationen ändern");
        MenuItem changeUser = new MenuItem("Ausloggen");
        MenuItem close = new MenuItem("Beenden");
        MenuItem passChange = new MenuItem("Passwort ändern");
        allgemein.getItems().addAll(infoChange, passChange, changeUser, close);
        
        //Lambda mit "mehreren" Ausdrücken
        infoChange.setOnAction(e -> {
            InfoBox.display();
        });
        passChange.setOnAction(e -> PasswordChange.display());
        //Lambda mit einem Ausdruck
        changeUser.setOnAction(e -> primaryStage.setScene(loginScreen));
        close.setOnAction(e -> {
            boolean test = ConfirmBox.display("Anwendung schließen", "Wollen Sie die Anwendung wirklich verlassen?");
            if(test == true){
                primaryStage.close();
            }
        });
        primaryStage.setOnCloseRequest(e ->{ 
            boolean test = ConfirmBox.display("Anwendung schließen", "Wollen Sie die Anwendung wirklich verlassen?");
            if(test == true){
                primaryStage.close();
            }else{e.consume();}
    });
        
        
        MenuItem addKunde = new MenuItem("Hinzufügen");
        MenuItem tableKunde = new MenuItem("Übersicht anzeigen");
        kunde.getItems().addAll(addKunde, tableKunde);
        
        addKunde.setOnAction(e -> KundenAdd.display());
        
        MenuItem addArtikel = new MenuItem("Hinzufügen");
        MenuItem addWarengruppe = new MenuItem("Neue Warengruppe");
        MenuItem tableArtikel = new MenuItem("Übersicht anzeigen");
        artikel.getItems().addAll(addArtikel, addWarengruppe, tableArtikel);
        
        addArtikel.setOnAction(e -> ArtikelAdd.display());
        addWarengruppe.setOnAction(e -> ArtikelAdd.display());
        
        MenuItem createAngebot = new MenuItem("Erstellen");
        MenuItem tableAngebot = new MenuItem("Übersicht anzeigen");
        angebot.getItems().addAll(createAngebot, tableAngebot);
        
        createAngebot.setOnAction(e -> AngebotAdd.display());
        
        MenuItem createRechnung = new MenuItem("Erstellen");
        MenuItem tableRechnung = new MenuItem("Übersicht anzeigen");
        rechnung.getItems().addAll(createRechnung, tableRechnung);
        
        menu.getMenus().addAll(allgemein, kunde, artikel, angebot, rechnung);
        BorderPane pane = new BorderPane();
        pane.setTop(menu);
        pane.setBottom(null);
        mainScreen = new Scene(pane, 700, 450);
        
        
        
        pane.setCenter(kundenT);
        tableArtikel.setOnAction(e -> pane.setCenter(artikelT));
        tableKunde.setOnAction(e -> pane.setCenter(kundenT));
        tableAngebot.setOnAction(e -> pane.setCenter(angebotT));
        tableRechnung.setOnAction(e -> pane.setCenter(rechnungT));
        
        
        primaryStage.setScene(loginScreen);
        primaryStage.setTitle("Verwaltungssoftware ");
        primaryStage.show();
    }
    
    public VBox createTableRechnung(){
        TableView rechnungT = new TableView();
        rechnungT.setEditable(true);
 
        TableColumn rNummer = new TableColumn("Rechnungsnummer");
        rNummer.setPrefWidth(130);
        TableColumn rVorname = new TableColumn("Vorname");
        TableColumn rName = new TableColumn("Nachname");
        TableColumn rDatum = new TableColumn("Datum");
        rechnungT.getColumns().addAll(rNummer, rVorname, rName, rDatum);
        
        VBox fAndT = createFilter(rechnungT);
        
        return fAndT;
    }
    
    public VBox createTableAngebot(){
        TableView angebotT = new TableView();
        angebotT.setEditable(true);
 
        TableColumn aNummer = new TableColumn("Angebotsnummer");
        aNummer.setPrefWidth(130);
        TableColumn aVorname = new TableColumn("Vorname");
        TableColumn aName = new TableColumn("Nachname");
        TableColumn datum = new TableColumn("Datum");
        TableColumn accept = new TableColumn("Akzeptiert");
        angebotT.getColumns().addAll(aNummer, aVorname, aName, datum, accept);
        
        VBox fAndT = createFilter(angebotT);
        
        return fAndT;
    }
    
    public VBox createTableArtikel(){
        TableView artikelT = new TableView();
        artikelT.setEditable(true);
 
        TableColumn name = new TableColumn("Bezeichnung");
        name.setPrefWidth(150);
        TableColumn nummer = new TableColumn("Artikelnummer");
        nummer.setPrefWidth(120);
        TableColumn preis = new TableColumn("Einzelpreis");
        TableColumn bestand = new TableColumn("Bestand");
        artikelT.getColumns().addAll(name, nummer, preis, bestand);
        
        VBox fAndT = createFilter(artikelT);
        
        return fAndT;
    }
    
    public VBox createTableKunde(){
        TableView kundenT = new TableView();
        kundenT.setEditable(true);
 
        TableColumn vorname = new TableColumn("Vorname");
        TableColumn nachname = new TableColumn("Nachname");
        TableColumn adresse = new TableColumn("Adresse");
        TableColumn ort = new TableColumn("Ort");
        TableColumn plz = new TableColumn("Postleitzahl");
        kundenT.getColumns().addAll(vorname, nachname, adresse, plz, ort);
        
        VBox fAndT = createFilter(kundenT);
        
        return fAndT;
    }
    
    public VBox createFilter(TableView t){
        Label filter = new Label("Filter :");
        TextField filterField = new TextField();
        
        HBox filterBox = new HBox();
        filterBox.setPadding(new Insets(5, 5, 5, 5));
        filterBox.setSpacing(8);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.getChildren().addAll(filter, filterField);
        
        VBox fAndT = new VBox();
        fAndT.getChildren().addAll(filterBox, t);
        return fAndT;
    }
    /**
     * @param args the command line arguments
     */
    public VBox getAngebotTable(){
        return angebotT;
    }
    public VBox getRechnungTable(){
        return rechnungT;
    }
    public VBox getKundenTable(){
        return kundenT;
    }
    public VBox getArtikelTable(){
        return artikelT;
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
