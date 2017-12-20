package com.verwaltungssoftware.database;

import com.verwaltungssoftware.objects.Kunde;
import com.verwaltungssoftware.objects.Artikel;
import com.verwaltungssoftware.objects.Angebot;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SqlConnector implements ISql {

    Properties userInfo;
    private ObservableList<Artikel> dataArtikel;
    private ObservableList<Kunde> dataKunde;
    private ObservableList<Angebot> dataAngebot;
    private ObservableList<Angebot> dataRechnung;
    private ObservableList<Artikel> dataArtikelInAngebot;
    private ObservableList<Kunde> dataFilteredKunde;
    private ObservableList<Angebot> dataFilteredAngebot;
    private ObservableList<Angebot> dataFilteredRechnung;
    private ObservableList<Artikel> dataFilteredArtikel;

    public SqlConnector() {
        userInfo = new Properties();
        userInfo.put("user", "root");
        userInfo.put("password", "databasemarcel");
        dataArtikel = FXCollections.observableArrayList();
        dataKunde = FXCollections.observableArrayList();
        dataAngebot = FXCollections.observableArrayList();
        dataRechnung = FXCollections.observableArrayList();
        dataArtikelInAngebot = FXCollections.observableArrayList();
        dataFilteredKunde = FXCollections.observableArrayList();
        dataFilteredAngebot = FXCollections.observableArrayList();
        dataFilteredRechnung = FXCollections.observableArrayList();
        dataFilteredArtikel = FXCollections.observableArrayList();
    }

    public ObservableList<Artikel> getDataArtikel() {
        return dataArtikel;
    }

    public ObservableList<Kunde> getDataKunde() {
        return dataKunde;
    }

    public ObservableList<Angebot> getDataRechnung() {
        return dataRechnung;
    }

    public ObservableList<Angebot> getDataAngebot() {
        return dataAngebot;
    }

    public ObservableList<Artikel> getDataArtikelInAngebot() {
        return dataArtikelInAngebot;
    }

    public ObservableList<Kunde> getDataFilteredKunde() {
        return dataFilteredKunde;
    }

    public ObservableList<Angebot> getDataFilteredAngebot() {
        return dataFilteredAngebot;
    }

    public ObservableList<Angebot> getDataFilteredRechnung() {
        return dataFilteredRechnung;
    }

    public ObservableList<Artikel> getDataFilteredArtikel() {
        return dataFilteredArtikel;
    }

    //statements und resultssets schließen
    @Override
    public void loadDataArtikel() throws SQLException {

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtArtikel = myConn.createStatement();
                ResultSet rsArtikel = stmtArtikel.executeQuery("select * from artikel")) {

            while (rsArtikel.next()) {

                dataArtikel.add(new Artikel(
                        rsArtikel.getString("Artikelnummer"),
                        rsArtikel.getString("Bezeichnung"),
                        rsArtikel.getString("Zusatztext"),
                        rsArtikel.getString("Rabatt"),
                        rsArtikel.getString("Skonto"),
                        rsArtikel.getString("Zuschlag"),
                        rsArtikel.getString("Einkaufspreis"),
                        rsArtikel.getString("Verkaufspreis"),
                        rsArtikel.getString("Mwst"),
                        rsArtikel.getString("Menge"),
                        rsArtikel.getString("Datum"),
                        null,
                        null));
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadDataKunde() throws SQLException {

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtKunde = myConn.createStatement();
                ResultSet rsKunde = stmtKunde.executeQuery("select * from kunde join postleitzahl on kunde.postleitzahl = postleitzahl.plz order by kundennummer asc")) {

            dataKunde.clear();

            while (rsKunde.next()) {
                dataKunde.add(new Kunde(
                        rsKunde.getString("kundennummer"),
                        rsKunde.getString("anrede"),
                        rsKunde.getString("vorname"),
                        rsKunde.getString("name"),
                        rsKunde.getString("straße"),
                        rsKunde.getString("hausnummer"),
                        rsKunde.getString("plz"),
                        rsKunde.getString("ort"),
                        rsKunde.getString("land")));
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadDataAngebot() throws SQLException {

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtAngebot = myConn.createStatement();
                ResultSet rsAngebot = stmtAngebot.executeQuery("select * from angebot order by angebotsnummer asc")) {

            while (rsAngebot.next()) {
                if (rsAngebot.getString("akzeptiert").equals("0")) {
                    dataAngebot.add(new Angebot(
                            rsAngebot.getString("angebotsnummer"),
                            rsAngebot.getString("kunde"),
                            rsAngebot.getString("datum"),
                            "noch ausstehend/nein"));
                } else {
                    dataAngebot.add(new Angebot(
                            rsAngebot.getString("angebotsnummer"),
                            rsAngebot.getString("kunde"),
                            rsAngebot.getString("datum"),
                            "ja/Rechnung erstellt"));
                }
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadDataRechnung() throws SQLException {
        try {

            loadDataAngebot();
            for (Angebot a : dataAngebot) {
                if (a.getAkzeptiert().equals("1")) {
                    dataRechnung.add(new Angebot(
                            a.getAngebotsnummer(),
                            a.getKunde(),
                            a.getDatum(),
                            "ja/Rechnung erstellt"));
                }
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadArtikelFromAngebot(String nummer) throws SQLException {
        String searchArtikelString = "select * from artikel join artikelinangebot as aia on artikel.artikelnummer = aia.artikel where aia.angebot = '" + nummer + "' ;";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtSearchArtikel = myConn.createStatement();
                ResultSet rsSearchArtikel = stmtSearchArtikel.executeQuery(searchArtikelString);) {

            dataArtikelInAngebot.clear();

            while (rsSearchArtikel.next()) {
                dataArtikelInAngebot.add(new Artikel(
                        rsSearchArtikel.getString("Artikelnummer"),
                        rsSearchArtikel.getString("Bezeichnung"),
                        rsSearchArtikel.getString("Zusatztext"),
                        rsSearchArtikel.getString("Artikel.Rabatt"),
                        rsSearchArtikel.getString("Skonto"),
                        rsSearchArtikel.getString("Zuschlag"),
                        rsSearchArtikel.getString("Einkaufspreis"),
                        rsSearchArtikel.getString("Verkaufspreis"),
                        rsSearchArtikel.getString("Mwst"),
                        rsSearchArtikel.getString("Menge"),
                        rsSearchArtikel.getString("Datum"),
                        rsSearchArtikel.getString("Alternativ"),
                        rsSearchArtikel.getString("aia.rabatt")));
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadFilteredKunden(String filter) throws SQLException {
        String searchKundeString = "select * from kunde join postleitzahl on kunde.postleitzahl = postleitzahl.plz where vorname like ? or name like ? or kundennummer like ? or "
                + "straße like ? or kunde.postleitzahl like ? or ort like ? or land like ?;";
        ResultSet rsSearchKunde = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtSearchKunde = myConn.prepareStatement(searchKundeString)) {

            dataFilteredKunde.clear();

            stmtSearchKunde.setString(1, "%" + filter + "%");
            stmtSearchKunde.setString(2, "%" + filter + "%");
            stmtSearchKunde.setString(3, "%" + filter + "%");
            stmtSearchKunde.setString(4, "%" + filter + "%");
            stmtSearchKunde.setString(5, "%" + filter + "%");
            stmtSearchKunde.setString(6, "%" + filter + "%");
            stmtSearchKunde.setString(7, "%" + filter + "%");
            rsSearchKunde = stmtSearchKunde.executeQuery();

            while (rsSearchKunde.next()) {
                dataFilteredKunde.add(new Kunde(
                        rsSearchKunde.getString("Kundennummer"),
                        rsSearchKunde.getString("Anrede"),
                        rsSearchKunde.getString("Vorname"),
                        rsSearchKunde.getString("Name"),
                        rsSearchKunde.getString("Straße"),
                        rsSearchKunde.getString("Hausnummer"),
                        rsSearchKunde.getString("postleitzahl"),
                        rsSearchKunde.getString("ort"),
                        rsSearchKunde.getString("land")));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsSearchKunde != null) {
                rsSearchKunde.close();
            }
        }
    }

    @Override
    public void loadFilteredAngebote(String filter) throws SQLException {
        String searchAngebotString = "select * from angebot where angebotsnummer like ? or kunde like ? or datum like ? or akzeptiert like ?;";
        ResultSet rsSearchAngebot = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtSearchAngebot = myConn.prepareStatement(searchAngebotString)) {

            dataFilteredAngebot.clear();

            stmtSearchAngebot.setString(1, "%" + filter + "%");
            stmtSearchAngebot.setString(2, "%" + filter + "%");
            stmtSearchAngebot.setString(3, "%" + filter + "%");
            stmtSearchAngebot.setString(4, "%" + filter + "%");
            rsSearchAngebot = stmtSearchAngebot.executeQuery();

            while (rsSearchAngebot.next()) {
                if (rsSearchAngebot.getString("akzeptiert").equals("0")) {
                    dataFilteredAngebot.add(new Angebot(
                            rsSearchAngebot.getString("angebotsnummer"),
                            rsSearchAngebot.getString("kunde"),
                            rsSearchAngebot.getString("datum"),
                            "noch ausstehend/nein"));
                } else {
                    dataFilteredAngebot.add(new Angebot(
                            rsSearchAngebot.getString("angebotsnummer"),
                            rsSearchAngebot.getString("kunde"),
                            rsSearchAngebot.getString("datum"),
                            "ja/Rechnung erstellt"));
                }
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsSearchAngebot != null) {
                rsSearchAngebot.close();
            }
        }
    }

    @Override
    public void loadFilteredRechnung(String filter) throws SQLException {
        String searchRechnungString = "select * from angebot where angebotsnummer like ? or kunde like ? or datum like ?;";
        ResultSet rsSearchRechnung = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtSearchRechnung = myConn.prepareStatement(searchRechnungString)) {

            dataFilteredRechnung.clear();

            stmtSearchRechnung.setString(1, "%" + filter + "%");
            stmtSearchRechnung.setString(2, "%" + filter + "%");
            stmtSearchRechnung.setString(3, "%" + filter + "%");
            rsSearchRechnung = stmtSearchRechnung.executeQuery();

            while (rsSearchRechnung.next()) {
                dataFilteredAngebot.add(new Angebot(
                        rsSearchRechnung.getString("angebotsnummer"),
                        rsSearchRechnung.getString("kunde"),
                        rsSearchRechnung.getString("datum"),
                        "ja/Rechnung erstellt"));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsSearchRechnung != null) {
                rsSearchRechnung.close();
            }
        }
    }

    @Override
    public void loadFilteredArtikel(String filter) throws SQLException {
        String searchArtikelString = "select * from artikel where artikelnummer like ? or bezeichnung like ?";
        ResultSet rsSearchArtikel = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtSearchArtikel = myConn.prepareStatement(searchArtikelString)) {

            dataFilteredArtikel.clear();

            stmtSearchArtikel.setString(1, "%" + filter + "%");
            stmtSearchArtikel.setString(2, "%" + filter + "%");
            rsSearchArtikel = stmtSearchArtikel.executeQuery();

            while (rsSearchArtikel.next()) {
                dataFilteredArtikel.add(new Artikel(
                        rsSearchArtikel.getString("Artikelnummer"),
                        rsSearchArtikel.getString("Bezeichnung"),
                        rsSearchArtikel.getString("Zusatztext"),
                        rsSearchArtikel.getString("Artikel.Rabatt"),
                        rsSearchArtikel.getString("Skonto"),
                        rsSearchArtikel.getString("Zuschlag"),
                        rsSearchArtikel.getString("Einkaufspreis"),
                        rsSearchArtikel.getString("Verkaufspreis"),
                        rsSearchArtikel.getString("Mwst"),
                        rsSearchArtikel.getString("Menge"),
                        rsSearchArtikel.getString("Datum"),
                        null,
                        null));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsSearchArtikel != null) {
                rsSearchArtikel.close();
            }
        }
    }

    @Override
    public void safeNewKunde(String a, String vn, String n, String s, String h, String z, String p, String o, String l) throws SQLException {
        String addStringKunde = "insert into kunde(kundennummer, anrede, vorname, name, straße, hausnummer, zusatz, postleitzahl) values(?, ?, ?, ?, ?, ?, ?, ?);";
        
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement addKunde = myConn.prepareStatement(addStringKunde)) {
 
            //gewährleistet, dass Postleitzahl vorhanden ist
            safeNewPlz(p, o, l);
            String kNumber = null;
            loadDataKunde();
            for(Kunde k: dataKunde){
                kNumber = k.getKundennummer();
            }
            kNumber = generateRandomClientNumber(kNumber);
            String[] parameter = {kNumber, a, vn, n, s, h, z, p, o, l};
            for (int i = 0; i < 8; i++) {
                addKunde.setString(i + 1, parameter[i]);
            }
            addKunde.executeUpdate();
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void safeNewPlz(String p, String o, String l) throws SQLException {

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtExistPlz = myConn.createStatement();
                ResultSet rsExistPlz = stmtExistPlz.executeQuery("select plz from postleitzahl;")) {

            //Überprüfung ob die Postleitzahl bereits existiert
            boolean plzExists = false;
            while (rsExistPlz.next()) {
                String test = rsExistPlz.getString("plz");
                plzExists = test.equals(p);
            }
            if (plzExists == false) {
                String addStringPlz = "insert into postleitzahl(plz, ort, land) values(?, ?, ?);";
                PreparedStatement addPlz = myConn.prepareStatement(addStringPlz);
                addPlz.setString(1, p);
                addPlz.setString(2, o);
                addPlz.setString(3, l);
                addPlz.executeUpdate();
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void safeNewArtikel(String aN, String bez, String z, String ra, String sk, String zu, String ePreis, String vPreis, String mwst, String m, String d) throws SQLException {
        String[] parameter = {aN, bez, z, ra, sk, zu, ePreis, vPreis, mwst, m, d};
        String addStringArtikel = "insert into kunde(artikelnummer, bezeichnung, zusatztext, rabatt, skonto, zuschlag, einkaufspreis, verkaufspreis, mwst, menge, datum) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement addArtikel = myConn.prepareStatement(addStringArtikel)) {

            for (int i = 0; i < 11; i++) {
                addArtikel.setString(i + 1, parameter[i]);
            }
            addArtikel.executeUpdate();
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void safeNewAngebot(String k, String d, String ak, ArrayList<Artikel> art, ArrayList<Integer> m) throws SQLException {

        Statement stmtCheckKunde = null;
        ResultSet rsCheckKunde = null;
        Statement stmtCheckAngebot = null;
        ResultSet rsCheckAngebot = null;
        PreparedStatement stmtAddAngebot = null;
        String aNumber = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo)) {

            //Test ob der Kunde überhaupt existiert
            stmtCheckKunde = myConn.createStatement();
            rsCheckKunde = stmtCheckKunde.executeQuery("select kundennummer from kunde;");
            boolean kundeExist = false;
            while (rsCheckKunde.next()) {
                String test = rsCheckKunde.getString("kundennummer");
                kundeExist = test.equals(k);
            }

            //Test ob Angebot bereits existiert
            stmtCheckAngebot = myConn.createStatement();
            rsCheckAngebot = stmtCheckAngebot.executeQuery("select angebotsnummer from angebot;");
            boolean angebotExist = false;
            while (rsCheckAngebot.next()) {
                String test = rsCheckAngebot.getString("angebotsnummer");
                angebotExist = test.equals(k);
            }

            if (kundeExist == true && angebotExist == false) {

                aNumber = generateRandomOfferNumber(d);
                String addStringAngebot = "insert into angebot(angebotsnummer, kunde, datum, akzeptiert) values(?, ?, ?, ?);";
                stmtAddAngebot = myConn.prepareStatement(addStringAngebot);
                stmtAddAngebot.setString(1, aNumber);
                stmtAddAngebot.setString(2, k);
                stmtAddAngebot.setString(3, d);
                stmtAddAngebot.setString(4, ak);
                stmtAddAngebot.executeUpdate();
            }

            int countM = 0;
            for (Artikel it : art) {
                safeArtikelInAngebot(aNumber, it.getArtikelnummer(), m.get(countM), Boolean.parseBoolean(it.getAlternative()), Double.parseDouble(it.getRabattmenge()));
                countM++;
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsCheckKunde != null) {
                rsCheckKunde.close();
            }
            if (stmtCheckKunde != null) {
                stmtCheckKunde.close();
            }
            if (rsCheckAngebot != null) {
                rsCheckAngebot.close();
            }
            if (stmtCheckAngebot != null) {
                stmtCheckAngebot.close();
            }
            if (stmtAddAngebot != null) {
                stmtAddAngebot.close();
            }
        }
    }

    @Override
    public void safeNewRechnung(String k, String d, String ak, ArrayList<Artikel> art, ArrayList<Integer> m) throws SQLException {

        Statement stmtCheckKunde = null;
        ResultSet rsCheckKunde = null;
        Statement stmtCheckRechnung = null;
        ResultSet rsCheckRechnung = null;
        PreparedStatement stmtAddRechnung = null;

        String rNumber = null;

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo)) {

            //Test ob der Kunde überhaupt existiert
            stmtCheckKunde = myConn.createStatement();
            rsCheckKunde = stmtCheckKunde.executeQuery("select kundennummer from kunde;");
            boolean kundeExist = false;
            while (rsCheckKunde.next()) {
                String test = rsCheckKunde.getString("kundennummer");
                kundeExist = test.equals(k);
            }

            //Test ob Angebot bereits existiert
            stmtCheckRechnung = myConn.createStatement();
            rsCheckRechnung = stmtCheckRechnung.executeQuery("select angebotsnummer from angebot;");
            boolean rechnungExist = false;
            while (rsCheckRechnung.next()) {
                String test = rsCheckRechnung.getString("angebotsnummer");
                rechnungExist = test.equals(k);
            }

            if (kundeExist == true && rechnungExist == false) {

                rNumber = generateRandomBillNumber(d);
                String addStringRechnung = "insert into angebot(angebotsnummer, kunde, datum, akzeptiert) values(?, ?, ?, ?);";
                stmtAddRechnung = myConn.prepareStatement(addStringRechnung);
                stmtAddRechnung.setString(1, rNumber);
                stmtAddRechnung.setString(2, k);
                stmtAddRechnung.setString(3, d);
                stmtAddRechnung.setString(4, ak);
                stmtAddRechnung.executeUpdate();
            }

            int countM = 0;
            for (Artikel it : art) {
                safeArtikelInAngebot(rNumber, it.getArtikelnummer(), m.get(countM), Boolean.parseBoolean(it.getAlternative()), Double.parseDouble(it.getRabattmenge()));
                countM++;
            }
        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsCheckKunde != null) {
                rsCheckKunde.close();
            }
            if (stmtCheckKunde != null) {
                stmtCheckKunde.close();
            }
            if (rsCheckRechnung != null) {
                rsCheckRechnung.close();
            }
            if (stmtCheckRechnung != null) {
                stmtCheckRechnung.close();
            }
            if (stmtAddRechnung != null) {
                stmtAddRechnung.close();
            }
        }
    }

    @Override
    public void safeArtikelInAngebot(String angebot, String artikel, int menge, boolean alt, double r) throws SQLException {
        String addStringArtikelInAngebot = "insert into artikelinangebot(angebot, artikel, menge, alternativ, rabatt) values(?, ?, ?, ?, ?);";
        String checkMengeString = "select menge from artikel join angebot on artikel.artikelnummer = angebot.artikel where angebot.artikel = ?";
        ResultSet rsCheckMenge = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtCheckMenge = myConn.prepareStatement(checkMengeString);
                PreparedStatement stmtAddArtikelInAngebot = myConn.prepareStatement(addStringArtikelInAngebot)) {

            //Testen ob die Menge des Artikels für das Angebot ausreicht
            stmtCheckMenge.setInt(1, menge);
            rsCheckMenge = stmtCheckMenge.executeQuery();
            boolean testMenge = false;
            while (rsCheckMenge.next()) {
                if (rsCheckMenge.getInt("menge") >= menge) {
                    testMenge = true;
                }
            }

            if (testMenge == true) {
                stmtAddArtikelInAngebot.setString(1, angebot);
                stmtAddArtikelInAngebot.setString(2, artikel);
                stmtAddArtikelInAngebot.setInt(3, menge);
                stmtAddArtikelInAngebot.setBoolean(4, alt);
                stmtAddArtikelInAngebot.setDouble(5, r);
                stmtAddArtikelInAngebot.executeUpdate();
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
            if (rsCheckMenge != null) {
                rsCheckMenge.close();
            }
        }
    }

    @Override
    public void updateKunde(String attr, String id, String eingabe) throws SQLException {
        String updateKundeString = "update kunde set ? = ? where kundennummer = ?";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtUpdateKunde = myConn.prepareStatement(updateKundeString)) {

            stmtUpdateKunde.setString(1, attr);
            stmtUpdateKunde.setString(2, eingabe);
            stmtUpdateKunde.setString(3, id);
            stmtUpdateKunde.executeUpdate();
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void updateArtikel(String attr, String id, String eingabe) throws SQLException {
        String updateArtikelString = "update artikel set ? = ? where artikelnummer = ?";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement stmtUpdateArtikel = myConn.prepareStatement(updateArtikelString)) {

            stmtUpdateArtikel.setString(1, attr);
            stmtUpdateArtikel.setString(2, eingabe);
            stmtUpdateArtikel.setString(3, id);
            stmtUpdateArtikel.executeUpdate();
        } catch (SQLException exc) {
            throw exc;
        }
    }

    /**
     * Generiert die chronologisch nächste Rechnungsnummer für einen bestimmten
     * Kunden
     *
     * @param letzteRechnung - die letzte gültige Rechnung. kann null sein
     * @param kunde - Kunde für den die Rechnung bestimmt ist
     * @param datum - aktuelles Datum
     * @return neue Rechnungsnummer
     * @throws SQLException
     */
    private String generateRandomBillNumber(String datum) throws SQLException {
        String stringLetzteRechnungen = "select * from angebot order by angebotsnummer asc;";
        ResultSet rsLetzteRechnungen = null;
        String letzteRechnung = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtLetzteRechnungen = myConn.createStatement()) {

            rsLetzteRechnungen = stmtLetzteRechnungen.executeQuery(stringLetzteRechnungen);

            while (rsLetzteRechnungen.next()) {
                dataRechnung.add(new Angebot(
                        rsLetzteRechnungen.getString("angebotsnummer"),
                        rsLetzteRechnungen.getString("kunde"),
                        rsLetzteRechnungen.getString("datum"),
                        rsLetzteRechnungen.getString("akzeptiert")));
            }
            for (Angebot aIt : dataRechnung) {
                letzteRechnung = aIt.getAngebotsnummer();
            }
        } finally {
            if (rsLetzteRechnungen != null) {
                rsLetzteRechnungen.close();
            }
        }

        String subYear = null;
        String subNumber = null;
        String newNumber = null;
        DateTimeFormatter dateTf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate ld = LocalDate.parse(datum, dateTf);
        if (letzteRechnung != null) {
            subYear = letzteRechnung.substring(0, 4);
            if (subYear.equals(String.valueOf(ld.getYear()))) {
                subNumber = letzteRechnung.substring(5, 10);
                subNumber = subNumber.replace("0", "");
                String zeroTemp = "";
                int number = Integer.parseInt(subNumber);
                number++; //Nummer um 1 erhöhen
                subNumber = String.valueOf(number);
                if (subNumber.length() == 5) { //wenn Länge bereits erreicht
                    newNumber = subNumber;
                } else {
                    for (int i = 0; i < 5; i++) { //5-stellige Ziffer mit Nullen auffüllen
                        zeroTemp += "0";
                        newNumber = zeroTemp + subNumber;
                        if (newNumber.length() == 5) { //Abbruch, wenn vorzeitig fertig ausgefüllt
                            break;
                        }
                    }
                }
            } else { //wenn die letzte Rechnung aus dem vorherigen Jahr oder früher stammt
                subYear = String.valueOf(ld.getYear());
                newNumber = "00001";
            }
        } else {
            subYear = String.valueOf(ld.getYear());
            newNumber = "00001";
        }

        String fullNumber = subYear + "-" + newNumber + "-R";
        return fullNumber;
    }

    private String generateRandomOfferNumber(String datum) throws SQLException {
        String stringLetzteAngebote = "select * from angebot where kunde = ? order by angebotsnummer asc;";
        ResultSet rsLetzteAngebot = null;
        String letztesAngebot = null;
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtLetzteRechnungen = myConn.createStatement()) {

            rsLetzteAngebot = stmtLetzteRechnungen.executeQuery(stringLetzteAngebote);

            while (rsLetzteAngebot.next()) {
                dataAngebot.add(new Angebot(
                        rsLetzteAngebot.getString("angebotsnummer"),
                        rsLetzteAngebot.getString("kunde"),
                        rsLetzteAngebot.getString("datum"),
                        rsLetzteAngebot.getString("akzeptiert")));
            }
            for (Angebot aIt : dataAngebot) {
                letztesAngebot = aIt.getAngebotsnummer();
            }
        } finally {
            if (rsLetzteAngebot != null) {
                rsLetzteAngebot.close();
            }
        }

        String subYear = null;
        String subNumber = null;
        String newNumber = null;
        DateTimeFormatter dateTf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate ld = LocalDate.parse(datum, dateTf);
        if (letztesAngebot != null) {
            subYear = letztesAngebot.substring(0, 4);
            if (subYear.equals(String.valueOf(ld.getYear()))) {
                subNumber = letztesAngebot.substring(5, 10);
                subNumber = subNumber.replace("0", "");
                String zeroTemp = "";
                int number = Integer.parseInt(subNumber);
                number++; //Nummer um 1 erhöhen
                subNumber = String.valueOf(number);
                if (subNumber.length() == 5) {
                    newNumber = subNumber;
                } else {
                    for (int i = 0; i < 5; i++) { //5-stellige Ziffer mit Nullen auffüllen
                        zeroTemp += "0";
                        newNumber = zeroTemp + subNumber;
                        if (newNumber.length() == 5) { //Abbruch, wenn vorzeitig fertig ausgefüllt
                            break;
                        }
                    }
                }
            } else { //wenn die letzte Rechnung aus dem vorherigen Jahr oder früher stammt
                subYear = String.valueOf(ld.getYear());
                newNumber = "00001";
            }
        } else {
            subYear = String.valueOf(ld.getYear());
            newNumber = "00001";
        }

        String fullNumber = subYear + "-" + newNumber + "-A";
        return fullNumber;
    }

    private String generateRandomClientNumber(String kunde) {

        String newKunde = kunde.substring(1, 6);
        newKunde = newKunde.replace("0", "");
        int plusKunde = Integer.parseInt(newKunde);
        plusKunde++;
        String newKundeTemp = String.valueOf(plusKunde);
        String zeroTemp = "";
        if (newKundeTemp.length() == 5) {
            newKunde = newKundeTemp;
        } else {
            for (int i = 0; i < 5; i++) {
                zeroTemp += "0";
                newKunde = zeroTemp + newKundeTemp;
                if (newKunde.length() == 5) {
                    break;
                }
            }
        }
        String fullKunde = "K" + newKunde;
        return fullKunde;
    }
}
