/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltungssoftware;

import java.util.ArrayList;

/**
 *
 * @author Marcel
 */
public class ArtikelInAngebot {
    private final Angebot angebot;
    private final ArrayList<Artikel> artikel;
    
    public ArtikelInAngebot(Angebot ang){
        angebot = ang;
        artikel = new ArrayList<>();
    }
}
