package com.verwaltungssoftware.main;

import com.verwaltungssoftware.pdf.PdfCreator;
import com.verwaltungssoftware.database.SqlConnector;
import com.verwaltungssoftware.objects.User;
import com.verwaltungssoftware.objects.Kunde;
import com.verwaltungssoftware.objects.Artikel;
import com.verwaltungssoftware.objects.Angebot;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Gui extends Application {

    private final User user;
    private final PdfCreator pdf;
    private final SqlConnector sql;

    private TableView<Artikel> tableArtikel;

    private TableView<Kunde> tableKunde;

    private TableView<Angebot> tableAngebot;

    private TableView<Artikel> tableArtikelInAngebot;

    public Gui() {
        user = new User("Sparkasse Oberlausitz Niederschlesien", "3100011383", "85050100", "207/146/00045", "DE 251622660", "Baustoffhandel TONAS Limited", "69 Great Hamton Street", "B18 6EW Birmingham",
                "Registered in England and Wales", "5802531", "Urs", "Kohler",
                "Jahnring 6a", "02959", "Deutschland", "Schleife", "0152-56840139", "035773-73297", "Sparkasse Oberlausitz Niederschlesien", "WELADED1GRL", "DE37 8505 0100 3100 0113 83", "Amtsgericht Dresden", "HRB 24647");
        sql = new SqlConnector();
        pdf = new PdfCreator(user, this, sql);
        tableArtikel = new TableView<>();
        tableKunde = new TableView<>();
        tableAngebot = new TableView<>();
        tableArtikelInAngebot = new TableView<>();
        
        /*UUID test = randomUUID();
        System.out.println(test);

        DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        System.out.println(dateF.format(date));
        
        //Wichtig
        DateTimeFormatter dateTf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate ld = LocalDate.parse("2017-03-03");
        System.out.println(ld);
        System.out.println(ld.format(dateTf));
        ld = ld.plusDays(1);
        System.out.println(ld.format(dateTf));
        LocalDate ld2 = LocalDate.now();
        ld2 = ld2.plusDays(1);
        System.out.println(ld2);*/
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            sql.loadDataKunde();
        } catch (SQLException exc) {
            System.out.println("Fehler beim laden der Kunden: " + exc.getMessage());
        }
        try {
            sql.loadDataArtikel();
        } catch (SQLException exc) {
            System.out.println("Fehler beim laden der Artikel: " + exc.getMessage());
        }
        try {
            sql.loadDataAngebot();
        } catch (SQLException exc) {
            System.out.println("Fehler beim laden der Angebote: " + exc.getMessage());
        }
        defineTableArtikel();
        defineTableKunde();
        defineTableAngebot();

        TextField tf = new TextField();
        tf.setOnKeyReleased((KeyEvent ke) -> {
            try {
                if (tf.getText().isEmpty()) {
                    sql.loadDataKunde();
                    tableKunde.setItems(sql.getDataKunde());
                } else {
                    sql.loadFilteredKunden(tf.getText());
                    tableKunde.setItems(sql.getDataFilteredKunde());
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        });
        VBox vb = new VBox();
        vb.getChildren().addAll(tf, tableKunde);
        TabPane tb = new TabPane();
        Tab tArtikel = new Tab("Artikel");
        Tab tKunde = new Tab("Kunden");
        Tab tAngebot = new Tab("Angebote");
        tArtikel.setContent(tableArtikel);
        tArtikel.closableProperty().set(false);
        tKunde.setContent(vb);
        tKunde.closableProperty().set(false);
        tAngebot.setContent(tableAngebot);
        tAngebot.closableProperty().set(false);
        tb.getTabs().addAll(tArtikel, tKunde, tAngebot);

        Scene scene = new Scene(tb);

        primaryStage.setTitle("Verwaltungssoftware");
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void defineTableArtikel() {
        TableColumn artikelnummer = new TableColumn("Artikelnummer");
        artikelnummer.setCellValueFactory(
                new PropertyValueFactory<>("artikelnummer"));

        TableColumn bezeichnung = new TableColumn("Bezeichnung");
        bezeichnung.setCellValueFactory(
                new PropertyValueFactory<>("bezeichnung"));

        TableColumn zusatztext = new TableColumn("Zusatztext");
        zusatztext.setCellValueFactory(
                new PropertyValueFactory<>("zusatztext"));

        TableColumn rabatt = new TableColumn("Rabatt");
        rabatt.setCellValueFactory(
                new PropertyValueFactory<>("rabatt"));

        TableColumn skonto = new TableColumn("Skonto");
        skonto.setCellValueFactory(
                new PropertyValueFactory<>("skonto"));

        TableColumn zuschlag = new TableColumn("Zuschlag");
        zuschlag.setCellValueFactory(
                new PropertyValueFactory<>("zuschlag"));

        TableColumn einkaufspreis = new TableColumn("Einkaufspreis");
        einkaufspreis.setCellValueFactory(
                new PropertyValueFactory<>("einkaufspreis"));

        TableColumn verkaufspreis = new TableColumn("Verkaufspreis");
        verkaufspreis.setCellValueFactory(
                new PropertyValueFactory<>("verkaufspreis"));

        TableColumn mwst = new TableColumn("MwSt.");
        mwst.setCellValueFactory(
                new PropertyValueFactory<>("mwst"));

        TableColumn menge = new TableColumn("Menge");
        menge.setCellValueFactory(
                new PropertyValueFactory<>("menge"));

        TableColumn datum = new TableColumn("Datum");
        datum.setCellValueFactory(
                new PropertyValueFactory<>("datum"));

        tableArtikel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableArtikel.setItems(sql.getDataArtikel());
        tableArtikel.getColumns().addAll(artikelnummer, bezeichnung, zusatztext, rabatt, skonto, zuschlag, einkaufspreis, verkaufspreis, mwst, menge, datum);
    }

    public void defineTableKunde() {
        TableColumn kundennummer = new TableColumn("Kundennnummer");
        kundennummer.setCellValueFactory(
                new PropertyValueFactory<>("kundennummer"));
        //kundennummer.setMaxWidth( 1f * Integer.MAX_VALUE * 12 );

        TableColumn anrede = new TableColumn("Anrede");
        anrede.setCellValueFactory(
                new PropertyValueFactory<>("anrede"));

        TableColumn vorname = new TableColumn("Vorname");
        vorname.setCellValueFactory(
                new PropertyValueFactory<>("vorname"));

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn straße = new TableColumn("Straße");
        straße.setCellValueFactory(
                new PropertyValueFactory<>("straße"));

        TableColumn hausnummer = new TableColumn("Hausnummer");
        hausnummer.setCellValueFactory(
                new PropertyValueFactory<>("hausnummer"));

        TableColumn plz = new TableColumn("Postleitzahl");
        plz.setCellValueFactory(
                new PropertyValueFactory<>("plz"));

        TableColumn ort = new TableColumn("Ort");
        ort.setCellValueFactory(
                new PropertyValueFactory<>("ort"));

        TableColumn land = new TableColumn("Land");
        land.setCellValueFactory(
                new PropertyValueFactory<>("land"));

        tableKunde.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableKunde.setItems(sql.getDataKunde());
        tableKunde.getColumns().addAll(kundennummer, vorname, name, straße, hausnummer, plz, ort, land);
    }

    public void defineTableAngebot() {
        TableColumn angebotsnummer = new TableColumn("Angebotsnummer");
        angebotsnummer.setCellValueFactory(
                new PropertyValueFactory<>("angebotsnummer"));

        TableColumn kunde = new TableColumn("Kunde");
        kunde.setCellValueFactory(
                new PropertyValueFactory<>("kunde"));

        TableColumn datum = new TableColumn("Datum");
        datum.setCellValueFactory(
                new PropertyValueFactory<>("datum"));

        TableColumn akzeptiert = new TableColumn("Akzeptiert?");
        akzeptiert.setCellValueFactory(
                new PropertyValueFactory<>("akzeptiert"));

        tableAngebot.setOnMouseClicked((MouseEvent me) -> {
            if (me.getClickCount() == 2) {
                String nummer = tableAngebot.getSelectionModel().getSelectedItems().get(0).getAngebotsnummer();
                System.out.println(nummer);
                try {
                    sql.loadArtikelFromAngebot(nummer);
                } catch (SQLException exc) {
                    System.out.println(exc.getMessage());
                }

                TableColumn artikelnummer = new TableColumn("Artikelnummer");
                artikelnummer.setCellValueFactory(
                        new PropertyValueFactory<>("artikelnummer"));

                TableColumn bezeichnung = new TableColumn("Bezeichnung");
                bezeichnung.setCellValueFactory(
                        new PropertyValueFactory<>("bezeichnung"));

                TableColumn zusatztext = new TableColumn("Zusatztext");
                zusatztext.setCellValueFactory(
                        new PropertyValueFactory<>("zusatztext"));

                TableColumn rabatt = new TableColumn("%");
                rabatt.setCellValueFactory(
                        new PropertyValueFactory<>("rabattmenge"));

                TableColumn skonto = new TableColumn("Skonto");
                skonto.setCellValueFactory(
                        new PropertyValueFactory<>("skonto"));

                TableColumn zuschlag = new TableColumn("Zuschlag");
                zuschlag.setCellValueFactory(
                        new PropertyValueFactory<>("zuschlag"));

                TableColumn einkaufspreis = new TableColumn("Einkaufspreis");
                einkaufspreis.setCellValueFactory(
                        new PropertyValueFactory<>("einkaufspreis"));

                TableColumn verkaufspreis = new TableColumn("Verkaufspreis");
                verkaufspreis.setCellValueFactory(
                        new PropertyValueFactory<>("verkaufspreis"));

                TableColumn mwst = new TableColumn("MwSt.");
                mwst.setCellValueFactory(
                        new PropertyValueFactory<>("mwst"));

                TableColumn menge = new TableColumn("Menge");
                menge.setCellValueFactory(
                        new PropertyValueFactory<>("menge"));

                TableColumn datumA = new TableColumn("Datum");
                datumA.setCellValueFactory(
                        new PropertyValueFactory<>("datum"));

                tableArtikelInAngebot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableArtikelInAngebot.setItems(sql.getDataArtikelInAngebot());
                tableArtikelInAngebot.getColumns().addAll(artikelnummer, bezeichnung, zusatztext, rabatt, skonto, zuschlag, einkaufspreis, verkaufspreis, mwst, menge, datumA);

                Stage stage = new Stage();

                //noch ausbaufähig
                if (tableArtikelInAngebot.getScene() == null) {
                    VBox vb = new VBox();
                    Button add = new Button();
                    add.setOnAction((ActionEvent) -> {
                        FileChooser fc = new FileChooser();
                        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*pdf"));
                        File f = fc.showSaveDialog(new Stage());
                        System.out.println(f);
                        if (f != null && !f.getName().contains(".")) {
                            f = new File(f.getAbsolutePath() + ".pdf");
                        }
                        if (f != null) {
                            try {
                                pdf.createDocument(tableAngebot.getSelectionModel().getSelectedItems().get(0).getKunde(), 
                                        tableAngebot.getSelectionModel().getSelectedItems().get(0).getAngebotsnummer(),
                                        "testhinweis ksdhflnlGNlrnnbnBBLAEOL NLLABOnNHhbtniobntrsbb nönbösrbbsrnlbibrhöbrböbsrbb",
                                        12,
                                        7,
                                        2,
                                        f);
                            } catch (DocumentException | IOException | SQLException exc) {
                                System.out.println(exc.getMessage());
                            }
                        }
                    });
                    vb.getChildren().addAll(add, tableArtikelInAngebot);
                    Scene scene = new Scene(vb);
                    stage.setScene(scene);
                } else {
                    stage.setScene(tableArtikelInAngebot.getScene());
                }

                stage.setOnCloseRequest((CloseEvent) -> {
                    tableArtikelInAngebot.getColumns().clear();
                });
                stage.show();
            }
        });

        tableAngebot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableAngebot.setItems(sql.getDataAngebot());
        tableAngebot.getColumns().addAll(angebotsnummer, kunde, datum, akzeptiert);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
