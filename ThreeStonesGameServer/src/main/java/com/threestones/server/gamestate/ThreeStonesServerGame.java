package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesServerGame {

    private ThreeStonesServerGameBoard board;

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ThreeStonesServerGame() {
        this.board = new ThreeStonesServerGameBoard();
    }

    public void updateBoard(int x, int y, CellState color) {
        board.updateBoard(x, y, color);
    }

    public void startGame() {
        board.initializeGameBoard();
    }

    public void setBoard(ThreeStonesServerGameBoard board) {
        this.board = board;
    }

    public byte[] determineNextMove() {
        List<ThreeStonesMove> possibleMoves = new ArrayList<>();
        CellState[][] gameBoard = board.getBoard();
        //LOOPS THROUGH GAMEBOARD
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //IF CURRENT TILE IS AVAILABLE ADD THE MOVE TO THE LIST
                if (gameBoard[i][j] == CellState.AVAILABLE) {
                    log.debug("determineNextMove CellState.Available");
                    //creates new move with possible values
                    ThreeStonesMove move = new ThreeStonesMove(board.checkForThreeStones(i, j, CellState.WHITE), board.checkForThreeStones(i, j, CellState.BLACK), i, j);
                    move.countNearbyTiles(board.getBoard());
                    possibleMoves.add(move);
                }
            }
        }
        //DETERMINE THE BEST MOVE OUT OF THE LIST
        if (possibleMoves.size() > 1) {
            
            return ThreeStonesMove.determineBestMove(possibleMoves).toByte();
        } else {
            return possibleMoves.get(0).toByte();
        }
    }


    private void constructServerMovePacket(byte[] serverMoveAndPoints){
        
    } 
}
