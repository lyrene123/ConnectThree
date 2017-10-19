/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.server.app;

import com.threestones.server.gamestate.ThreeStonesGameBoard;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author 1537385
 */
public class GameboardController implements Initializable {

    @FXML
    private BorderPane gameBoardIViewId;

    @FXML
    private GridPane gameBoardGridPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ThreeStonesGameBoard board = new ThreeStonesGameBoard();
        this.gameBoardGridPane = new GridPane();
        //this.gameBoardGridPane.setPrefHeight(1000);
        //this.gameBoardGridPane.setPrefWidth(1000);
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {

                Rectangle rec = new Rectangle();

                rec.setWidth(50);
                rec.setHeight(50);
                switch(board.getBoard()[row][col]){
                    
                }
                rec.setFill(Color.web("#00F"));
                rec.setStroke(Color.GREEN);
                
                //rec.setFill(colors[n]);
                gameBoardGridPane.setRowIndex(rec, row);
                gameBoardGridPane.setColumnIndex(rec, col);
                gameBoardGridPane.getChildren().add(rec);

            }
        }
        this.gameBoardIViewId.setCenter(gameBoardGridPane);
    }
}
