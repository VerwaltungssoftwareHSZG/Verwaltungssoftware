package verwaltungssoftware;

import javafx.beans.property.SimpleStringProperty;

public class Angebot {
    private final SimpleStringProperty angebotsnummer, kunde, datum;
    //private final ArrayList<Artikel> waren;
    
    Angebot(String aNummer, String k, String date){
        angebotsnummer = new SimpleStringProperty(aNummer);
        kunde = new SimpleStringProperty(k);
        datum = new SimpleStringProperty(date);
        //waren = new ArrayList<>();
    }
    
    public void setAngebotsnummer(String a){
        angebotsnummer.set(a);
    }
    
    public String getAngebotsnummer(){
        return angebotsnummer.get();
    }
    
    public void setDatum(String d){
        datum.set(d);
    }
    
    public String getDatum(){
        return datum.get();
    }
    
    public void setKunde(String k){
        kunde.set(k);
    }
    
    public String getKunde(){
        return kunde.get();
    }
    /*public void addArtikel(Artikel a){
        waren.add(a);
    }
    
    public Artikel getArtikel(int index){
        return waren.get(index);
    }*/
}
