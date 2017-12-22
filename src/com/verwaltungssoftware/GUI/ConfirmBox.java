/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forschungsprojekt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class ConfirmBox {
    static boolean bool;
    
    static boolean display(String title, String message){
        Scene usure;
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        
        Label text = new Label(message);
        Button yes = new Button("Ja");
        yes.setPrefSize(50, 25);
        Button no = new Button("Nein");
        no.setPrefSize(50, 25);
        
        yes.setOnAction(e -> {
            bool = true;
             popupStage.close();
        });
        no.setOnAction(e -> {
            bool = false;
            popupStage.close();
        });
        
        HBox buttons = new HBox();
        buttons.getChildren().addAll(yes, no);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(8);
        buttons.setAlignment(Pos.CENTER);
        
        BorderPane pane = new BorderPane();
        pane.setCenter(text);
        pane.setBottom(buttons);
        
        usure = new Scene(pane, 300, 100);
        popupStage.setScene(usure);
        popupStage.showAndWait();
        
        return bool;
        
    }
}
