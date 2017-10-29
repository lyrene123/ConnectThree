package com.threestones.client.gamestate;

import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
import com.threestones.client.packet.ThreeStonesClientPacket;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ehugh
 */
public class ThreeStonesClient {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final ThreeStonesClientGameBoard board;
    private final ThreeStonesClientPacket clientPacket;

    public ThreeStonesClient() {
        board = new ThreeStonesClientGameBoard();
        board.startNewGame();
        clientPacket = new ThreeStonesClientPacket();
    }

    public ThreeStonesClientGameBoard getBoard() {
        return board;
    }

    public ThreeStonesClientPacket getClientPacket() {
        return clientPacket;
    }

    public void clickBoardCell(int x, int y) throws IOException {
        //send server validation

        if (board.getBoard()[x][y] == CellState.AVAILABLE) {
            log.debug("sent: " + x + " " + y);
            board.updateBoard(x, y, -1, -1, CellState.WHITE);
            clientPacket.sendMove(x, y);
            serverResponse();

            //ThreeStonesClientGame localGame = new ThreeStonesClientGame();
            //board.reDrawBoard(x, y, CellState.WHITE ); 
            //localGame.setBoard(board); 
            //byte[] move = localGame.determineNextMove(x, y);
            //board.updateBoard(move[1], move[2], CellState.BLACK);
            //board.reDrawBoard(move[1], move[2], CellState.BLACK);
            //localGame.setBoard(board);
            //log.debug("clickBoardCell new server move   " + move[1] + " " + move[2] );
            //ThreeStonesClientPacket.sendMove(x,y);
            //must make sure that server checks that if row col are full all cells become available
        } else if (board.getBoard()[x][y] == CellState.UNAVAILABLE) {
            //display wrong move message

        }

    }

    public void serverResponse() throws IOException {
        byte[] byteBuffer = clientPacket.receivePacket();
        switch (byteBuffer[0]) {
            case 1: //servers's move
                log.debug("code 1 receive servers's move");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK);
                break;
            case 3: //Player won
                log.debug("code 2 Player won");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK);
                
                
            //handlePlayerMove(receivedPacket, out); 
            //isGameOver = true;
            //return 0;
                break;
            //case 3: //player's request to play again
                //log.debug("code 3"); //player wants to play again
                //isPlayAgain = true;
                //isGameOver = false;
               // break;
            //case 4: //player's request not to play again
                //log.debug("code 4");
            // isPlayAgain = false;
            //return -1;
        }
    }

    private void handleServerMove() {

    }

}
