package com.threestones.client.gamestate;

import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesClientGame {

    private ThreeStonesClientGameBoard board;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    //GENERAL BASIC LOGIC FOR OUR MOVE SELECTION
    //DEPENDING ON WHERE WE WANT TO TAKE THIS WE CAN ADD MORE MOVE COMPLEXITY SO THAT IT CHECKS THE AVAILALBE
    //SQUARES THAT WILL BECOME AVAILABLE BASE ON A CERTAIN MOVE AND THE SCORES WHITE OR BLACK CAN SCORE IN THE FUTURE (reading ahead of time)
    public ThreeStonesClientGame() {
        this.board = new ThreeStonesClientGameBoard();
    }

    public void updateBoard(int x, int y) {
        board.setBoard(board.getBoardChange(x, y));
    }

    public byte[] determineNextMove() {
        List<ThreeStonesMove> bestMoves = new ArrayList<ThreeStonesMove>();
        CellState[][] gameBoard = board.getBoard();
        int highestMoveValue = 0;
        byte[] moves = null;

        //LOOPS THROUGH GAMEBOARD
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //IF CURRENT TILE IS AVAILABLE DETERMINE ITS VALUE
                if (gameBoard[j][i] == CellState.AVAILABLE) {
                    log.debug("determineNextMove CellState.Available");
                    int move1 = board.checkForThreeStones(i, j, CellState.WHITE);
                    int move2 = board.checkForThreeStones(i, j, CellState.BLACK);
                    ThreeStonesMove move = new ThreeStonesMove(move1, move2, i, j);
                    //ThreeStonesMove move = new ThreeStonesMove(board.checkForThreeStones(i, j, CellState.WHITE), board.checkForThreeStones(i, j, CellState.BLACK), i, j);
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
            //RANDOMIZES MOVE
            //CAN CHANGE IT SO IT PREFERS HIS OWN SCORES OVER THE ENEMY PlAYER SCORING
            //WASNT SURE WHAT TO DO FROM HERE , HOW WE ARE GOING TO HANDLE making moves

            //(int)(Math.random() * bestMoves.size())
        } else {
            //IF ONLY ONE GOOD MOVE MAKE IT USING LIST.get(0)
        }
        return moves;
    }

    public void startGame() {
        board.startNewGame();
    }

    private ThreeStonesMove createMove(int x, int y) {

        return new ThreeStonesMove();
    }

}
