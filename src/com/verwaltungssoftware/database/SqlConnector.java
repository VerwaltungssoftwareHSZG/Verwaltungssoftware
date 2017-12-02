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
import java.util.ArrayList;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SqlConnector implements ISql {

    Properties userInfo;
    private final ObservableList<Artikel> dataArtikel;
    private final ObservableList<Kunde> dataKunde;
    private final ObservableList<Angebot> dataAngebot;
    private final ObservableList<Artikel> dataArtikelInAngebot;

    public SqlConnector() {
        userInfo = new Properties();
        userInfo.put("user", "root");
        userInfo.put("password", "databasemarcel");
        dataArtikel = FXCollections.observableArrayList();
        dataKunde = FXCollections.observableArrayList();
        dataAngebot = FXCollections.observableArrayList();
        dataArtikelInAngebot = FXCollections.observableArrayList();
    }

    public ObservableList<Artikel> getDataArtikel() {
        return dataArtikel;
    }

    public ObservableList<Kunde> getDataKunde() {
        return dataKunde;
    }

    public ObservableList<Angebot> getDataAngebot() {
        return dataAngebot;
    }

    public ObservableList<Artikel> getDataArtikelInAngebot() {
        return dataArtikelInAngebot;
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
                        rsArtikel.getString("Datum")));
            }
        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void loadDataKunde() throws SQLException {

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                Statement stmtKunde = myConn.createStatement();
                ResultSet rsKunde = stmtKunde.executeQuery("select * from kunde join postleitzahl on kunde.postleitzahl = postleitzahl.plz")) {

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
                ResultSet rsAngebot = stmtAngebot.executeQuery("select * from angebot")) {

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
                        rsSearchArtikel.getString("Rabatt"),
                        rsSearchArtikel.getString("Skonto"),
                        rsSearchArtikel.getString("Zuschlag"),
                        rsSearchArtikel.getString("Einkaufspreis"),
                        rsSearchArtikel.getString("Verkaufspreis"),
                        rsSearchArtikel.getString("Mwst"),
                        rsSearchArtikel.getString("Menge"),
                        rsSearchArtikel.getString("Datum")));
            }

        } catch (SQLException exc) {
            throw exc;
        }
    }

    @Override
    public void safeNewKunde(String k, String a, String vn, String n, String s, String h, String z, String p, String o, String l) throws SQLException {
        String[] parameter = {k, a, vn, n, s, h, z, p, o, l};
        String addStringKunde = "insert into kunde(kundennummer, anrede, vorname, name, straße, hausnummer, zusatz, postleitzahl) values(?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Verwaltungssoftware?useSSL=true", userInfo);
                PreparedStatement addKunde = myConn.prepareStatement(addStringKunde)) {
            //gewährleistet, dass Postleitzahl vorhanden ist
            safeNewPlz(p, o, l);

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
    public void safeNewAngebot(String a, String k, String d, ArrayList<Artikel> art, ArrayList<Integer> m) throws SQLException {

        Statement stmtCheckKunde = null;
        ResultSet rsCheckKunde = null;
        Statement stmtCheckAngebot = null;
        ResultSet rsCheckAngebot = null;
        PreparedStatement stmtAddAngebot = null;

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
                String addStringAngebot = "insert into angebot(angebotsnummer, kunde, datum) values(?, ?, ?);";
                stmtAddAngebot = myConn.prepareStatement(addStringAngebot);
                stmtAddAngebot.setString(1, a);
                stmtAddAngebot.setString(2, k);
                stmtAddAngebot.setString(3, d);
                stmtAddAngebot.executeUpdate();
            }

            int countM = 0;
            for (Artikel it : art) {
                safeArtikelInAngebot(a, it.getArtikelnummer(), m.get(countM));
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
    public void safeArtikelInAngebot(String angebot, String artikel, int menge) throws SQLException {
        String addStringArtikelInAngebot = "insert into artikelinangebot(angebot, artikel, menge) values(?, ?, ?);";
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
}
