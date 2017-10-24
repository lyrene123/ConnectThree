package com.threestones.server.gamestate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ThreeStonesGameBoard {

    public enum CellState {
        BLACK, WHITE, AVAILABLE, VACANT, UNAVAILABLE
    }

    private CellState[][] board;
    private int blackStoneCount;
    private int whiteStoneCount;
    private int whiteScore;
    private int blackScore;

    public ThreeStonesGameBoard() {
        startNewGame();
    }

    public void startNewGame() {
        int blackStoneCount = 15;
        int whiteStoneCount = 15;
        int whiteScore = 0;
        int blackScore = 0;
        constructBoard();
    }

    public CellState[][] getBoard() {
        return board;
    }

    public void setBoard(CellState[][] board) {
        this.board = board;
    }

    public int getBlackStoneCount() {
        return blackStoneCount;
    }

    public void setBlackStoneCount(int blackStoneCount) {
        this.blackStoneCount = blackStoneCount;
    }

    public int getWhiteStoneCount() {
        return whiteStoneCount;
    }

    public void setWhiteStoneCount(int whiteStoneCount) {
        this.whiteStoneCount = whiteStoneCount;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    /**
     * @returns number of points scored from the coordinate
     * @param x x coordinate on game board
     * @param y y coordinate on game board
     */
    public int checkForThreeStones(int x, int y, CellState color) {
        int points = 0;
        //check horizontal left

        if (board[y][x - 1] == color && board[y][x - 2] == color) {
            points++;
        }

        //check horizontal middle
        if (board[y][x - 1] == color && board[y][x + 1] == color) {
            points++;
        }

        //check horizontal right
        if (board[y][x + 1] == color && board[y][x + 2] == color) {
            points++;
        }

        //check vertical Up
        if (board[y - 1][x] == color && board[y - 2][x] == color) {
            points++;
        }

        //check vertical Middle
        if (board[y - 1][x] == color && board[y + 1][x] == color) {
            points++;
        }

        //check vertical Down
        if (board[y + 1][x] == color && board[y + 2][x] == color) {
            points++;
        }

        //check diagonal N-E /
        if (board[y - 1][x + 1] == color && board[y - 2][x + 2] == color) {
            points++;
        }

        //check diagonal N-W \
        if (board[y - 1][x - 1] == color && board[y + 2][x - 2] == color) {
            points++;
        }

        //check diagonal S-E \
        if (board[x + 1][y + 1] == color && board[x + 2][y + 2] == color) {
            points++;
        }

        //check diagonal S-W /
        if (board[y + 1][x - 1] == color && board[y + 2][x - 2] == color) {
            points++;
        }

        //check diagonals Middle
        //Diagnol Middle Right /
        if (board[y - 1][x - 1] == color && board[y + 1][x + 1] == color) {
            points++;
        }
        //Diagonal Middle Left \
        if (board[y + 1][x - 1] == color && board[y + 1][x + 1] == color) {
            points++;
        }

        return points;
    }
    
    //returns an altered version of the board
    //I made it to return for now incase we want to
    //make the ai more sophisticated (to check for moves 2-3 turns ahead)
    public CellState[][] getBoardChange(int x, int y) {
        CellState[][] board = this.board;

        if (!checkIfFull(x, y)) {
            for (int i = 0; i < board[0].length; i++) {
                for (int j = 0; j < board[0].length; j++) {

                    if (i == x || j == y && board[i][j] != CellState.VACANT) {
                        board[i][j] = CellState.AVAILABLE;
                    } else if (board[i][j] != CellState.VACANT) {
                        board[i][j] = CellState.AVAILABLE;
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

    //checks if both row and column is full
    private boolean checkIfFull(int x, int y) {
        //check row
        boolean full = true;
        for (int i = 0; i < board[0].length && full; i++) {
            if (board[i][x] == CellState.AVAILABLE || board[i][x] == CellState.UNAVAILABLE) {
                full = false;
            }
        }
        //check col
        for (int i = 0; i < board[0].length && full; i++) {
            if (board[x][y] == CellState.AVAILABLE || board[x][y] == CellState.UNAVAILABLE) {
                full = false;
            }
        }
        return full;
    }

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
