package com.threestones.server.gamestate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Encapsulates the behavior and properties of a game board in a game of Three
 * Stones such as the state of each slot in the game board, the calculation of
 * points made by black and white stones, and the necessary updates made to the
 * game board when moves are done.
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesServerGameBoard {

    /**
     * Enums holding the different states of a slot in a Three Stones game board
     */
    public enum CellState {
        BLACK, WHITE, AVAILABLE, VACANT, UNAVAILABLE
    }

    private CellState[][] board;
    private int blackStoneCount;
    private int whiteStoneCount;
    private int whiteScore;
    private int blackScore;

    /**
     * Default constructor
     */
    public ThreeStonesServerGameBoard() {
    }

    /**
     * Initializes the game board by setting the black stones and white stones
     * total left, the points as well as the construction of the board.
     */
    public void startNewGame() {
        blackStoneCount = 15;
        whiteStoneCount = 15;
        whiteScore = 0;
        blackScore = 0;
        constructBoard();
    }

    /**
     * Returns the CellState a2D array
     *
     * @return CellState 2D array
     */
    public CellState[][] getBoard() {
        return board;
    }

    /**
     * Sets the board with a CellState 2D array as input
     *
     * @param board CellState 2D array
     */
    public void setBoard(CellState[][] board) {
        this.board = board;
    }

    /**
     * Returns the number of black stones left to be played
     *
     * @return int
     */
    public int getBlackStoneCount() {
        return blackStoneCount;
    }

    /**
     * Sets the number of black stones left to be played
     *
     * @param blackStoneCount int
     */
    public void setBlackStoneCount(int blackStoneCount) {
        this.blackStoneCount = blackStoneCount;
    }

    /**
     * Sets the number of white stones left to be played
     *
     * @return int
     */
    public int getWhiteStoneCount() {
        return whiteStoneCount;
    }

    /**
     * Sets the number of white stones left to be played
     *
     * @param whiteStoneCount int
     */
    public void setWhiteStoneCount(int whiteStoneCount) {
        this.whiteStoneCount = whiteStoneCount;
    }

    /**
     * Returns the total white score
     *
     * @return int
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Sets the total white score
     *
     * @param whiteScore int
     */
    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    /**
     * Returns the total black score
     *
     * @return int
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Sets the total black score
     *
     * @param blackScore int
     */
    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    /**
     * Calculates and returns the number of points scored by all possible
     * combinations of black or white stones on the game board based on the
     * coordinate x and y
     *
     * @returns number of points scored from the coordinate
     * @param x row
     * @param y col
     */
    public int checkForThreeStones(int x, int y, CellState color) {
        int points = 0;

        //check horizontal left
        if (board[x][y - 1] == color && board[x][y - 2] == color) {
            points++;
        }

        //check horizontal middle
        if (board[x][y - 1] == color && board[x][y + 1] == color) {
            points++;
        }

        //check horizontal right
        if (board[x][y + 1] == color && board[x][y + 2] == color) {
            points++;
        }

        //check vertical Up
        if (board[x - 1][y] == color && board[x - 2][y] == color) {
            points++;
        }

        //check vertical Middle
        if (board[x - 1][y] == color && board[x + 1][y] == color) {
            points++;
        }

        //check vertical Down
        if (board[x + 1][y] == color && board[x + 2][y] == color) {
            points++;
        }

        //check diagonal N-E /
        if (board[x - 1][y + 1] == color && board[x - 2][y + 2] == color) {
            points++;
        }

        //check diagonal N-W \
        if (board[x - 1][y - 1] == color && board[x - 2][y - 2] == color) {
            points++;
        }

        //check diagonal S-E \
        if (board[x + 1][y + 1] == color && board[x + 2][y + 2] == color) {
            points++;

        }

        //check diagonal S-W /
        if (board[x - 1][y + 1] == color && board[x - 2][y + 2] == color) {
            points++;
        }

        //check diagonals Middle
        
        //Diagnol Middle Right /
        if (board[x - 1][y + 1] == color && board[x + 1][y - 1] == color) {
            points++;
        }
        //Diagonal Middle Left \
        if (board[x - 1][y - 1] == color && board[x + 1][y - 1] == color) {
            points++;
        }

        return points;
    }

    /**
     * Updates the board with the new coordinate x and y and returns the altered
     * version of the board by returning the CellState 2D array
     *
     * @param x int x row
     * @param y int y column
     * @return CellState 2D array
     */
    public CellState[][] getBoardChange(int x, int y) {
        CellState[][] boardCopy = this.board.clone();

        if (!checkIfFull(x, y)) {
            for (int i = 0; i < boardCopy[0].length; i++) {
                for (int j = 0; j < boardCopy[0].length; j++) {
                    if (i == x || j == y && boardCopy[i][j] != CellState.VACANT) {
                        boardCopy[i][j] = CellState.AVAILABLE;
                    } else if (boardCopy[i][j] != CellState.VACANT) {
                        boardCopy[i][j] = CellState.AVAILABLE;
                    }
                }
            }
        } else {
            for (int i = 0; i < boardCopy[0].length; i++) {
                for (int j = 0; j < boardCopy[0].length; j++) {
                    if (boardCopy[i][j] == CellState.UNAVAILABLE) {
                        boardCopy[i][j] = CellState.AVAILABLE;
                    }
                }
            }
        }
        return boardCopy;
    }

    public void makeMove(int x, int y, CellState color) {

        int points = checkForThreeStones(x, y, color);
        if (color == CellState.WHITE) {
            whiteScore += points;
        } else {
            blackScore += points;
        }
        this.board = getBoardChange(x, y);

    }

    /**
     * Checks if a row x and column y is full with stones on the game board and
     * returns a boolean true or false
     *
     * @param x int x row
     * @param y int y column
     * @return boolean
     */
    private boolean checkIfFull(int x, int y) {
        //check row
        boolean full = true;
        for (int j = 0; j < board[0].length && full; j++) {
            if (board[x][j] == CellState.AVAILABLE || board[x][j] == CellState.UNAVAILABLE) {
                full = false;
            }
        }
        //check col
        for (int i = 0; i < board[0].length && full; i++) {
            if (board[i][y] == CellState.AVAILABLE || board[i][y] == CellState.UNAVAILABLE) {
                full = false;
            }
        }
        return full;
    }

    /**
     * Reads the gameboard layout from a file and put data from the file into a
     * 2D array
     *
     * @param filename filename String
     * @return 2D array
     * @throws IOException
     */
    private int[][] constructArrayFromFile(String filename) throws IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        String line;
        int row = 0;
        int size = 0;
        String token = ",";
        int[][] arr = null;

        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(token);
            size = vals.length;
            arr = new int[size][size];

            for (int col = 0; col < size; col++) {
                arr[row][col] = Integer.parseInt(vals[col]);
            }
            row++;
        }
        return arr;
    }

    private void constructBoard() {

        String file = "gameboard.csv";
        try {
            int[][] arr = constructArrayFromFile(file);
            board = new CellState[arr.length][arr.length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    switch (arr[i][j]) {
                        case -1:
                            board[i][j] = CellState.VACANT;
                            break;
                        case 0:
                            board[i][j] = CellState.AVAILABLE;
                            break;
                        default:
                            board[i][j] = CellState.AVAILABLE;
                    }
                }
            }

        } catch (IOException e) {
        }
    }
}
