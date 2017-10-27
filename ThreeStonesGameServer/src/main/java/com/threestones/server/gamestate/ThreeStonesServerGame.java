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

    //GENERAL BASIC LOGIC FOR OUR MOVE SELECTION
    //DEPENDING ON WHERE WE WANT TO TAKE THIS WE CAN ADD MORE MOVE COMPLEXITY SO THAT IT CHECKS THE AVAILALBE
    //SQUARES THAT WILL BECOME AVAILABLE BASE ON A CERTAIN MOVE AND THE SCORES WHITE OR BLACK CAN SCORE IN THE FUTURE (reading ahead of time)
    public ThreeStonesServerGame() {
        this.board = new ThreeStonesServerGameBoard();
    }

    public void updateBoard(int x, int y) {
        board.getBoardChange(x, y);
    }

    public byte[] determineNextMove() {
        List<ThreeStonesMove> bestMoves = new ArrayList<ThreeStonesMove>();
        CellState[][] gameBoard = board.getBoard();
        int highestMoveValue = 0;
        ThreeStonesMove bestMove = new ThreeStonesMove();
        //LOOPS THROUGH GAMEBOARD
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //IF CURRENT TILE IS AVAILABLE DETERMINE ITS VALUE
                if (gameBoard[i][j] == CellState.AVAILABLE) {
                    log.debug("determineNextMove CellState.Available");
                    ThreeStonesMove move = new ThreeStonesMove(board.checkForThreeStones(i, j, CellState.WHITE), board.checkForThreeStones(i, j, CellState.BLACK), i, j);
                    log.debug("Checking Moves: " + move.toString());
                    //resets list and adds the current move

                    //ADD LOGIC HERE FOR ADVANCED AI
                    //check hypothical board state
                    //get a list of created hypothical moves
                    //POSSIBLY CHANGE THE moveValue system so that 
                    //different moves are worth more ie white +2 black +4 , moves
                    //2 turns ahead +1 +2
                    if (move.getMoveValue() > highestMoveValue) {
                        bestMoves.clear();
                        highestMoveValue = move.getMoveValue();
                        bestMoves.add(move);
                    } //if move is equal to the top add it to the list
                    else if (move.getMoveValue() == highestMoveValue) {
                        bestMoves.add(move);
                    }
                }
            }
        }
        if (bestMoves.size() > 1) {
            //randomizes best move
            bestMove = bestMoves.get((int)(Math.random() * bestMoves.size()));
        } else {
            bestMove = bestMoves.get(0);
        }
        
        return bestMove.toByte();
    }

    public void startGame() {
        board.startNewGame();
    }

    private ThreeStonesMove createMove(int x, int y) {

        return new ThreeStonesMove();
    }

}
