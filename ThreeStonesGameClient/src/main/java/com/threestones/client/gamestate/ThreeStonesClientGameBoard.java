package com.threestones.client.gamestate;

import com.threestones.view.ThreeStonesGUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and properties of a game board in a game of Three
 * Stones such as the state of each slot in the game board, the combination of
 * black and white stones on the board, and manages the necessary updates made
 * to the game board when moves are done.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesClientGameBoard {

    //for logging information on the console
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Enums holding the different states of a slot in a Three Stones game board
     */
    public enum CellState {
        BLACK, WHITE, AVAILABLE, VACANT, UNAVAILABLE
    }

    private CellState[][] board; //array representing the state of the cells of the board
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
     * Sets the handle to the GUI instance of the object.
     *
     * @param gui ThreeStonesGUI object
     */
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
     * Updates the GUI of the application in order for the user to see the
     * updates made on the board with a new move
     *
     * @param x coord x position of the new move
     * @param y coord y position of the new move
     * @param color stone color
     */
    public void reDrawBoard(int x, int y, CellState color) {
        this.gui.updateView(x, y, color);
    }

    /**
     * Updates the client board with the new move's x and y coordinates, the
     * updated black and white points retrieved from the server reponse packet
     * the number of stones left to be played. Notifies the GUI view as well for
     * the user to see the updates made and displays any extra information for
     * the user
     *
     * @param x coord x position
     * @param y coord y position
     * @param whitePoints white stone score
     * @param blackPoints white stone score
     * @param color stone color of the new move
     * @param msgCode message code representing the type of message to display
     */
    public void updateBoard(int x, int y, int whitePoints, int blackPoints, CellState color, int msgCode) {
        //update the scores
        if (whitePoints != -1 && blackPoints != -1) {
            this.whiteScore = whitePoints;
            this.blackScore = blackPoints;
        }

        //update the stone counts
        if (color == CellState.BLACK) {
            this.blackStoneCount--;
        } else if (color == CellState.WHITE) {
            this.whiteStoneCount--;
        }

        //update the board and notify the GUI with the new move 
        if (x != -1 && y != -1) {
            board[x][y] = color;
            board = updateRestOfBoard(x, y);
            reDrawBoard(x, y, color);
        }
        displayMessages(msgCode, whitePoints, blackPoints);
    }

    /**
     * Notifies the GUI for any messages to be displayed on the text area for
     * the user. The message to be displayed will vary depending on the message
     * code whether it's a message to notify player has won, server has won,
     * game is a tie, or player has one last move.
     *
     * @param msgCode message code representing the type of message
     * @param whitePoints white score to display to user
     * @param blackPoints black score to display to user
     */
    private void displayMessages(int msgCode, int whitePoints, int blackPoints) {
        switch (msgCode) {
            case -2:
                this.gui.handlePlayerLastMove();
                break;
            case -1:
                this.gui.notifyInvalidMove();
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
     * Updates the rest of the board after a move with coord x and y has been
     * made on the board. if the row and column in which the move is in is not
     * fully taken by other stones, sets all unoccupied cells that are on the
     * same row and column as the move to available and the rest as unavailable
     * only if they are not yet taken by any black or white stone. If column and
     * row are fully taken by other stones, sets all free cells as available.
     *
     * Note: For the client application, this logic is only used for the purpose
     * of showing the right colors on the GUI, and not for the purpose of
     * calculating points or any game logic unlike the server side application.
     *
     * @param x int x coord position
     * @param y int y coord position
     * @return CellState 2D array representing the updated board
     */
    public CellState[][] updateRestOfBoard(int x, int y) {
        if (!checkIfFull(x, y)) {
            updateBoardColRowNotFull(x, y);
        } else {
            updateBoardColRowFull();
        }
        return board;
    }

    /**
     * Iterates through the board and updates the cells in the same row and
     * column as an x coord and y coord as available and the rest of the cells
     * of the board as unavailable only if those cells are not taken by white or
     * black stones and if they are not vacant cells.
     *
     * Note: For the client application, this logic is only used for the purpose
     * of showing the right colors on the GUI, and not for the purpose of
     * calculating points or any game logic unlike the server side application.
     *
     * @param x coord y position
     * @param y coord y position
     */
    private void updateBoardColRowNotFull(int x, int y) {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                //don't update the board if cell is taken by a black or white stone
                if (board[i][j] == CellState.BLACK || board[i][j] == CellState.WHITE) {
                    continue;
                }
                //if the cell is on the same row and not taken by a stone nor vacant, make it available
                if (i == x && board[i][j] != CellState.VACANT && board[i][j] != CellState.BLACK
                        && board[i][j] != CellState.WHITE) {
                    board[i][j] = CellState.AVAILABLE;
                } else if (j == y && board[i][j] != CellState.VACANT
                        && board[i][j] != CellState.BLACK && board[i][j] != CellState.WHITE) {
                    //if the cell is on the same column and not taken by a stone nor vacant, make it available
                    board[i][j] = CellState.AVAILABLE;
                } else if (board[i][j] != CellState.WHITE
                        && board[i][j] != CellState.BLACK && board[i][j] != CellState.VACANT) {
                    //set all other cell on the board as unavailable
                    board[i][j] = CellState.UNAVAILABLE;
                } else {
                    //set the other cells not part of the board as vacant
                    board[i][j] = CellState.VACANT;
                }
            }
        }
    }

    /**
     * Iterate through the board and updates all cells on the board that are
     * taken by any white or black stones and not a vacant cell (not part of the
     * board) as available.
     *
     * Note: For the client application, this logic is only used for the purpose
     * of showing the right colors on the GUI, and not for the purpose of
     * calculating points or any game logic unlike the server side application.
     */
    private void updateBoardColRowFull() {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == CellState.UNAVAILABLE) {
                    board[i][j] = CellState.AVAILABLE;
                }
            }
        }
    }

    /**
     * Checks if a row x and column y is full with stones on the game board and
     * returns a boolean true or false
     *
     * Note: For the client application, this logic is only used for the purpose
     * of showing the right colors on the GUI, and not for the purpose of
     * calculating points or any game logic unlike the server side application.
     *
     * @param x int x row
     * @param y int y column
     * @return boolean whether or not the row or column is full
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
     * Builds the game board by filling up the CellState 2D array instance
     * property. The game board layout is read from a file and written into a 2D
     * array which is iterated through and used to define each CellState
     * element.
     */
    private void constructBoard() {
        String file = "gameboard.csv";
        try {
            int[][] arr = constructArrayFromFile(file);//read layout from file
            board = new CellState[arr.length][arr.length];
            //loop through the 2D array and each element from that array represents 
            //an item in the CellState 2D array
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
            log.error("Error occured while reading from gameboard.csv");
        }
    }

    /**
     * Reads the gameboard layout from a file and put the file data into a 2D
     * array.
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

        //read each line of the file and add it as a row in the array
        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(token);
            for (int col = 0; col < size; col++) {
                arr[row][col] = Integer.parseInt(vals[col]);
            }
            row++;
        }
        return arr;
    }
}
