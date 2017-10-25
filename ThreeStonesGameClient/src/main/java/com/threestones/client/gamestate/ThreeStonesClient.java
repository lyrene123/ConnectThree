/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.gamestate;

import com.threestones.client.gamestate.ThreeStonesGameBoard.CellState;
import com.threestones.client.packet.ThreeStonesClientPacket;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ehugh
 */
public class ThreeStonesClient {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private ThreeStonesGameBoard board;
    private ThreeStonesClientPacket clientPacket;

    public ThreeStonesClient() {
        board = new ThreeStonesGameBoard();
        clientPacket = new ThreeStonesClientPacket();
    }

    public ThreeStonesGameBoard getBoard() {
        return board;
    }

    public ThreeStonesClientPacket getClientPacket() {
        return clientPacket;
    }

    public void clickBoardCell(int x, int y) throws IOException {
        //send server validation

        if (board.getBoard()[y][x] == CellState.AVAILABLE) {
            log.debug("sent: " + y + " " + x);

            clientPacket.sendMove(x, y);
            serverResponse();
            //ThreeStonesClientPacket.sendMove(x,y);
            //must make sure that server checks that if row col are full all cells become available
        } else if (board.getBoard()[y][x] == CellState.UNAVAILABLE) {
            //display wrong move message

        }

    }

    public void serverResponse() throws IOException {

        byte[] byteBuffer = clientPacket.receivePacket();
        int x, y;
        switch (byteBuffer[0]) {
            case 1: //player's move
                log.debug("code 1");
                x = byteBuffer[2];
                y = byteBuffer[3];
                board.updateBoard(x, y);
                //handlePlayerMove(receivedPacket, out);
                break;
            case 2: //player's last move
                log.debug("code 2");
                x = receivedPacket[2];
                y = receivedPacket[3];
                serverGame.updateBoard(x, y);
                //handlePlayerMove(receivedPacket, out); 
                isGameOver = true;
                return 0;
            case 3: //player's request to play again
                log.debug("code 3"); //player wants to play again
                isPlayAgain = true;
                isGameOver = false;
                break;
            case 4: //player's request not to play again
                log.debug("code 4");
                isPlayAgain = false;
                return -1;
        }

    }

}
