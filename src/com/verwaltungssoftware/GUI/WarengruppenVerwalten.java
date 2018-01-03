/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.verwaltungssoftware.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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
public class WarengruppenVerwalten {
    static Scene verwaltung;
    public static void display(){
        Stage popupStage = new Stage();
        
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Warengruppen verwalten");
        
        
        verwaltung = new Scene(pane, 350, 350);
        popupStage.setScene(verwaltung);
        popupStage.show();
    }
}
