package com.verwaltungssoftware.objects;

import javafx.beans.property.SimpleStringProperty;

public class Kunde {

    SimpleStringProperty kundennummer, anrede, vorname, name, straße, hausnummer, plz, ort, land;

    public Kunde(String kNummer, String a, String vn, String n, String s, String h, String p, String o, String l) {
        kundennummer = new SimpleStringProperty(kNummer);
        anrede = new SimpleStringProperty(a);
        vorname = new SimpleStringProperty(vn);
        name = new SimpleStringProperty(n);
        straße = new SimpleStringProperty(s);
        hausnummer = new SimpleStringProperty(h);
        plz = new SimpleStringProperty(p);
        ort = new SimpleStringProperty(o);
        land = new SimpleStringProperty(l);
    }

    public String getKundennummer() {
        return kundennummer.get();
    }

    public void setKundennummer(String k) {
        kundennummer.set(k);
    }

    public String getAnrede() {
        return anrede.get();
    }

    public void setAnrede(String a) {
        anrede.set(a);
    }

    public String getVorname() {
        return vorname.get();
    }

    public void setVorname(String vn) {
        vorname.set(vn);
    }

    public void setName(String n) {
        name.set(n);
    }

    public String getName() {
        return name.get();

    }

    public String getStraße() {
        return straße.get();
    }

    public void setStraße(String s) {
        straße.set(s);
    }

    public String getHausnummer() {
        return hausnummer.get();
    }

    public void setHausnummer(String h) {
        hausnummer.set(h);
    }

    public String getPlz() {
        return plz.get();
    }

    public void setPlz(String p) {
        plz.set(p);
    }

    public String getOrt() {
        return ort.get();
    }

    public void setOrt(String o) {
        ort.set(o);
    }

    public String getLand() {
        return land.get();
    }

    public void setLand(String l) {
        land.set(l);
    }
}
