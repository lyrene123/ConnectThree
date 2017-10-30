package com.threestones.client.gamestate;

import com.threestones.view.ThreeStonesGUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.LoggerFactory;

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
public class ThreeStonesClientGameBoard {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

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
    private ThreeStonesGUI gui;

    /**
     * Default constructor
     */
    public ThreeStonesClientGameBoard() {

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

    public void reDrawBoard(int x, int y, CellState color) {
        this.gui.updateView(x, y, color);
    }

    public void updateBoard(int x, int y, int whitePoints, int blackPoints, CellState color, int message) {
        if (whitePoints != -1 && blackPoints != -1) {
            this.whiteScore = whitePoints;
            this.blackScore = blackPoints;
        }

        if (color == CellState.BLACK) {
            this.blackStoneCount--;
        } else if (color == CellState.WHITE) {
            this.whiteStoneCount--;
        }
        board[x][y] = color;
        board = getBoardChange(x, y);
        reDrawBoard(x, y, color);
        displayMessages(message, whitePoints, blackPoints);
    }

    private void displayMessages(int message, int whitePoints, int blackPoints) {
        switch (message) {
            case -2:
                this.gui.handlePlayerLastMove();
                break;
            case 0:
                this.gui.notifyTieGame(whitePoints, blackPoints);
                break;
            case 1:
                this.gui.notifyPlayerWon(whitePoints, blackPoints);
                break;
            case 2:
                this.gui.notifyServerWon(whitePoints, blackPoints);
                break;
        }
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
        if (!checkIfFull(x, y)) {
            for (int i = 0; i < board[0].length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == CellState.BLACK || board[i][j] == CellState.WHITE) {
                        continue;
                    }
                    if (i == x && board[i][j] != CellState.VACANT && board[i][j] 
                            != CellState.BLACK && board[i][j] != CellState.WHITE) {
                        board[i][j] = CellState.AVAILABLE;
                    } else if (j == y && board[i][j] != CellState.VACANT && board[i][j] 
                            != CellState.BLACK && board[i][j] != CellState.WHITE) {
                        board[i][j] = CellState.AVAILABLE;
                    } else if (board[i][j] != CellState.WHITE && board[i][j] 
                            != CellState.BLACK && board[i][j] != CellState.VACANT) {
                        board[i][j] = CellState.UNAVAILABLE;
                    } else {
                        board[i][j] = CellState.VACANT;
                    }
                }
            }
        } else {
            for (int i = 0; i < board[0].length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == CellState.UNAVAILABLE) {
                        board[i][j] = CellState.AVAILABLE;
                    }
                }
            }
        }
        return board;
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

    public void setGui(ThreeStonesGUI gui) {
        this.gui = gui;
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
        int size = 11;
        String token = ",";
        int[][] arr = new int[size][size];

        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(token);
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
            log.error("Error reading from gameboard.csv", e);
        }
    }
}
