package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesMove;
import com.threestones.server.gamestate.ThreeStonesGameBoard.CellState;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesServerGame {

    private ThreeStonesGameBoard board;

    //GENERAL BASIC LOGIC FOR OUR MOVE SELECTION
    //DEPENDING ON WHERE WE WANT TO TAKE THIS WE CAN ADD MORE MOVE COMPLEXITY SO THAT IT CHECKS THE AVAILALBE
    //SQUARES THAT WILL BECOME AVAILABLE BASE ON A CERTAIN MOVE AND THE SCORES WHITE OR BLACK CAN SCORE IN THE FUTURE (reading ahead of time)
    public ThreeStonesServerGame() {
        this.board = new ThreeStonesGameBoard();
    }

    public void drawBoard() {
        readFile("gameboard.csv");
    }

    public void updateBoard(int x, int y) {
        board.changeBoard(x, y);
    }

    public void determineNextMove() {
        List<ThreeStonesMove> bestMoves = new ArrayList<ThreeStonesMove>();
        CellState[][] gameBoard = board.getBoard();
        int highestMoveValue = 0;

        //LOOPS THROUGH GAMEBOARD
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //IF CURRENT TILE IS AVAILABLE DETERMINE ITS VALUE
                if (gameBoard[i][j] == CellState.AVAILABLE) {
                    ThreeStonesMove move = new ThreeStonesMove(board.checkForThreeStones(i, j, CellState.WHITE), board.checkForThreeStones(i, j, CellState.BLACK), i, j);
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

    }

    private ThreeStonesMove createMove(int x, int y) {

        return new ThreeStonesMove();
    }

    private void readFile(String filename) {
        try {
            constructArray(filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void constructArray(String filename) throws IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        String line;
        int row = 0;
        int size = 0;
        String token = ",";
        int[][] arr = null;

        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(token);
            if (board.getBoard() == null) {
                size = vals.length;
                arr = new int[size][size];
            }

            for (int col = 0; col < size; col++) {
                arr[row][col] = Integer.parseInt(vals[col]);
            }
            row++;
        }
        constructBoard(arr);
    }

    private void constructBoard(int[][] arr) {
        CellState[][] gameboard = new CellState[arr.length][arr.length];
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr.length; j++){
                switch(arr[i][j]){
                    case -1: gameboard[i][j] = CellState.UNAVAILABLE;
                    case 0: gameboard[i][j] = CellState.AVAILABLE;
                    default: gameboard[i][j] = CellState.UNAVAILABLE;
                }
            }
        }
        board.setBoard(gameboard);
    }
}
