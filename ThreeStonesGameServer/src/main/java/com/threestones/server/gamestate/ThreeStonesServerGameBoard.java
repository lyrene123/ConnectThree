package com.threestones.server.gamestate;

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
public class ThreeStonesServerGameBoard {

    //for logging information on the console
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

    /**
     * Default constructor
     */
    public ThreeStonesServerGameBoard() {

    }

    /**
     * Initializes the game board by setting the black stones and white stones
     * total left, the points to their default values and builds the board as
     * well.
     */
    public void initializeGameBoard() {
        blackStoneCount = 15;
        whiteStoneCount = 15;
        whiteScore = 0;
        blackScore = 0;
        constructBoard();
    }

    /**
     * Returns the CellState which is a 2D array representing the cells of the
     * game board.
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
     * @param blackStoneCount int black stone count
     */
    public void setBlackStoneCount(int blackStoneCount) {
        this.blackStoneCount = blackStoneCount;
    }

    /**
     * Returns the number of white stones left to be played
     *
     * @return int white stone count
     */
    public int getWhiteStoneCount() {
        return whiteStoneCount;
    }

    /**
     * Sets the number of white stones left to be played
     *
     * @param whiteStoneCount int white stone count
     */
    public void setWhiteStoneCount(int whiteStoneCount) {
        this.whiteStoneCount = whiteStoneCount;
    }

    /**
     * Returns the total white score
     *
     * @return int white score
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Sets the total white score
     *
     * @param whiteScore int white score
     */
    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    /**
     * Returns the total black score
     *
     * @return int black score
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Sets the total black score
     *
     * @param blackScore int black score
     */
    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    /**
     * Calculates and returns the number of points scored by all possible
     * combinations of black or white stones on the game board around a
     * coordinate x and y position with a specific stone color
     *
     * @param color stone color CellState
     * @param x x coordinate position on game board
     * @param y y coordinate position on game board
     * @return number of points scored around the coordinate position
     */
    public int calculateThreeStonesPoints(int x, int y, CellState color) {
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
        if (board[x + 1][y - 1] == color && board[x + 2][y - 2] == color) {
            points++;
        }

        //check diagonals Middle
        //Diagnol Middle Right /
        if (board[x - 1][y + 1] == color && board[x + 1][y - 1] == color) {
            points++;
        }
        //Diagonal Middle Left \
        if (board[x - 1][y - 1] == color && board[x + 1][y + 1] == color) {
            points++;
        }

        return points;
    }

    /**
     * Updates the rest of the board after a move with coord x and y has been
     * made on the board. if the row and column in which the move is in is not
     * fully taken by other stones, sets all valid cells that are on the same
     * row and column as the move as available and the rest as unavailable only
     * if they are not yet taken by any black or white stone. If column and row
     * are fully taken by other stones, set all free cells as available.
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
     * Updates a specific cell on a board with the input x and y coords and a
     * stone color. Updates the stone count (white or black stone count
     * depending on the input color) and the points for white or black. The rest
     * of the board is updated as well by calling the updateRestOfBoard method.
     *
     * @param x coord x position
     * @param y coord y position
     * @param color
     */
    public void updateBoardWithNewMove(int x, int y, CellState color) {
        board[x][y] = color; //set the stone color to the cell
        //update the stone count and points
        if (color == CellState.WHITE) {
            whiteScore += calculateThreeStonesPoints(x, y, color);
            whiteStoneCount--;
        } else {
            blackScore += calculateThreeStonesPoints(x, y, color);
            blackStoneCount--;
        }
        this.board = updateRestOfBoard(x, y); //update rest of board
    }

    /**
     * Checks if a row x and column y is full with stones on the game board and
     * returns a boolean true or false
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
