/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.gamestate;

import com.threestones.client.gamestate.ThreeStonesGameBoard.CellState;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ehugh
 */
public class ThreeStonesClient {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private ThreeStonesGameBoard board;

    public ThreeStonesClient() {
        board = new ThreeStonesGameBoard();
    }

    public ThreeStonesGameBoard getBoard() {
        return board;
    }

    public void clickBoardCell(int x, int y) {
        //send server validation
        
        if (board.getBoard()[y][x] == CellState.AVAILABLE){
            log.debug("Good Move");
            //GOOD MOVE SEND TO SERVER
            //must make sure that server checks that if row col are full all cells become available
            
        }
        
        else if (board.getBoard()[y][x] == CellState.UNAVAILABLE){
            //display wrong move message
        }
        
    }

}
