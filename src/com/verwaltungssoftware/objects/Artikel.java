package com.verwaltungssoftware.objects;

import javafx.beans.property.SimpleStringProperty;

public class Artikel {

    private final SimpleStringProperty artikelnummer, bezeichnung,
            zusatztext, rabatt, skonto, zuschlag,
            einkaufspreis, verkaufspreis,
            mwst, menge, datum;

    public Artikel(String aNummer, String bez, String extra, 
            String r, String s, String ePreis, String vPreis, 
            String z, String m, String num, String d) {

        artikelnummer = new SimpleStringProperty(aNummer);
        bezeichnung = new SimpleStringProperty(bez);
        zusatztext = new SimpleStringProperty(extra);
        rabatt = new SimpleStringProperty(r); //Notiz: Rabatt, Skonto und Zuschlag eher abhängig vom Kunden
        skonto = new SimpleStringProperty(s);
        einkaufspreis = new SimpleStringProperty(ePreis);
        verkaufspreis = new SimpleStringProperty(vPreis);
        zuschlag = new SimpleStringProperty(z);
        mwst = new SimpleStringProperty(m);
        menge = new SimpleStringProperty(num);
        datum = new SimpleStringProperty(d);
    }

    //getter and setter
    public String getArtikelnummer(){
        return artikelnummer.get();
    }
    
    public void getArtikelnummer(String a){
        artikelnummer.set(a);
    }
    
    public String getBezeichnung(){
        return bezeichnung.get();
    }
    
    public void setBezeichnung(String bez){
        bezeichnung.set(bez);
    }
    
    public String getZusatztext(){
        return zusatztext.get();
    }
    
    public void setZusatzstext(String z){
        zusatztext.set(z);
    }
    
    public String getRabatt(){
        return rabatt.get();
    }
    
    public void setRabatt(String r){
        rabatt.set(r);
    }
    
    public String getSkonto(){
        return skonto.get();
    }
    
    public void setSkonto(String s){
        skonto.set(s);
    }
    
    public String getEinkaufspreis(){
        return einkaufspreis.get();
    }
    
    public void setEinkaufspreis(String e){
        einkaufspreis.set(e);
    }
    
    public String getVerkaufspreis(){
        return verkaufspreis.get();
    }
    
    public void setverkaufsrpeis(String v){
        verkaufspreis.set(v);
    }
    
    public String getZuschlag(){
        return zuschlag.get();
    }
    
    public void setZuschlag(String z){
        zuschlag.set(z);
    }
    
    public String getMwst(){
        return mwst.get();
    }
    
    public void setMwst(String m){
        mwst.set(m);
    }
    
    public String getMenge(){
        return menge.get();
    }
    
    public void setMenge(String m){
        menge.set(m);
    }
    
    public String getDatum(){
        return datum.get();
    }
    
    public void setDatum(String d){
        datum.set(d);
    }
    //
}
