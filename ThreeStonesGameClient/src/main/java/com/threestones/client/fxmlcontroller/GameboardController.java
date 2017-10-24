/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.fxmlcontroller;


import com.threestones.client.gamestate.ThreeStonesClient;
import com.threestones.client.gamestate.ThreeStonesGameBoard;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author 1537385
 */
public class GameboardController implements Initializable {
   // private ThreStonesGameBoard;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    @FXML
    private BorderPane gameBoardIViewId;

    @FXML
    private GridPane gameBoardGridPane;
    
    private ThreeStonesClient clientGame;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientGame = new ThreeStonesClient();
       
        this.gameBoardGridPane = new GridPane();

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {

                Rectangle rec = new Rectangle();

                rec.setWidth(50);
                rec.setHeight(50);
                rec.getStyleClass().add("white-stone");

   
                rec.setStroke(Color.BLACK);
                switch (clientGame.getBoard().getBoard()[row][col]){
                    case AVAILABLE:
                    rec.setFill(Color.GREEN);
                    rec.setOnMouseClicked(e -> {
                    Rectangle rec1 = (Rectangle) e.getSource();
                    int x = GridPane.getRowIndex(rec1);
                    int y = GridPane.getColumnIndex(rec1);
                    //clientGame.clickBoardCell(x,y);
                 //   this.clickBoardCell(x,y);
                });
                    break;
                    case VACANT:
                    rec.setFill(Color.BLACK);
                    break;
            }
                //rec.setFill(colors[n]);
                gameBoardGridPane.setRowIndex(rec, row);
                gameBoardGridPane.setColumnIndex(rec, col);
                gameBoardGridPane.getChildren().add(rec);

            }
        }
        this.gameBoardIViewId.setCenter(gameBoardGridPane);
    }

    private void clickButton() {

    }

}
