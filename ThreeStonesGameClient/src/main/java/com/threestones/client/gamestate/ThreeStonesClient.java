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
            board.updateBoard(x, y, -1, -1, CellState.WHITE, -1);
            clientPacket.sendClientPacketToServer(x, y);
            serverResponse();

        } else if (board.getBoard()[x][y] == CellState.UNAVAILABLE) {
            //display wrong move message

        }

    }

    public void serverResponse() throws IOException {
        byte[] byteBuffer = clientPacket.receiveClientPacket();
        switch (byteBuffer[0]) {
            case 1: //servers's move
                log.debug("code 1 receive servers's move");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK, -1);
                break;
            case 3: //Player won
                log.debug("code 2 Player won");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK, 1);
                break;
            case 4: //server won
                log.debug("code 3 Server won");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK, 2);
                break;
            case 5: //tie
                log.debug("code 3 Tie");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK, 0);
                break;
            case 6: //player has one move left
                log.debug("code 3 Player has one move left");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3], byteBuffer[4], CellState.BLACK, -2);
                break;

            //handlePlayerMove(receivedPacket, out); 
            //isGameOver = true;
            //return 0;
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
