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
import javafx.scene.control.TableView;
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
public class TablePopup {
    static Scene tablePopup;
    public static void display(String title, VBox box){
        Stage popupStage = new Stage();
        
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        
        Button cancel = new Button("Abbrechen");
        cancel.setOnAction(e -> popupStage.close());
        
        HBox buttons = new HBox();
        buttons.getChildren().add(cancel);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(8);
        
        buttons.setAlignment(Pos.CENTER);
        BorderPane pane = new BorderPane();
        
        pane.setCenter(box);
        pane.setBottom(buttons);
        
        tablePopup = new Scene(pane, 600, 500);
        popupStage.setScene(tablePopup);
        popupStage.show();
    }
}
