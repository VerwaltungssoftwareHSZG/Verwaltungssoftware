package com.verwaltungssoftware.database;

import com.verwaltungssoftware.objects.Artikel;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ISql {

    public void loadDataArtikel() throws SQLException;

    public void loadDataKunde() throws SQLException;

    public void loadDataAngebot() throws SQLException;
    
    public void loadDataRechnung() throws SQLException;

    public void loadArtikelFromAngebot(String nummer) throws SQLException;

    public void safeNewPlz(String p, String o, String l) throws SQLException;

    public void safeNewKunde(String k, String a, String vn, String n, String s, String h, String z, String p, String o, String l) throws SQLException;

    public void safeNewArtikel(String aN, String bez, String z, String ra, String sk, String zu, String ePreis, String vPreis, String mwst, String m, String d) throws SQLException;

    public void safeNewAngebot(String a, String k, String d, String ak, ArrayList<Artikel> art, ArrayList<Integer> m) throws SQLException;

    public void safeArtikelInAngebot(String angebot, String artikel, int menge, boolean alt, double r) throws SQLException;

    public void updateKunde(String attr, String id, String eingabe) throws SQLException;

    public void updateArtikel(String attr, String id, String eingabe) throws SQLException;
    
    public void loadFilteredAngebote(String filter) throws SQLException;
    
    public void loadFilteredRechnung(String filter) throws SQLException;
    
    public void loadFilteredKunden(String filter) throws SQLException;
    
    public void loadFilteredArtikel(String filter) throws SQLException;

}
