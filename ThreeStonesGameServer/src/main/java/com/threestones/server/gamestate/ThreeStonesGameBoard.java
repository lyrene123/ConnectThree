package com.threestones.server.gamestate;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesGameBoard {
    public enum CellState {
        BLACK, WHITE, AVAILABLE, VACANT, UNAVAILABLE
    }
    
    private CellState[][] board;
    private int blackStoneCount;
    private int whiteStoneCount;
    private int whiteScore;
    private int blackScore;

    public ThreeStonesGameBoard() { }

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
        if ((x - 2) >= 0) {
            if (board[x - 1][y] == color && board[x - 2][y] == color) {
                points++;
            }
        }

        //check horizontal middle
        if ((x - 1) >= 0 && (x + 1) < board.length) {
            if (board[x - 1][y] == color && board[x + 1][y] == color) {
                points++;
            }
        }

        //check horizontal right
        if ((x + 2) >= 0) {
            if (board[x + 1][y] == color && board[x + 2][y] == color) {
                points++;
            }
        }

        //check vertical Up
        if ((y - 2) >= 0) {
            if (board[x][y - 1] == color && board[x][y - 2] == color) {
                points++;
            }
        }

        //check vertical Middle
        if ((y - 1) >= 0 && (y + 1) < board.length) {
            if (board[x][y - 1] == color && board[x][y + 1] == color) {
                points++;
            }
        }

        //check vertical Down
        if ((y + 2) < board.length) {
            if (board[x][y + 1] == color && board[x][y + 2] == color) {
                points++;
            }
        }

        //check diagonal N-E /
        if ((x + 2) < board.length && (y - 2) >= 0) {
            if (board[x + 1][y - 1] == color && board[x + 2][y - 2] == color) {
                points++;
            }
        }

        //check diagonal N-W \
        if ((x - 2) >= 0 && (y - 2) >= 0) {
            if (board[x - 1][y - 1] == color && board[x - 2][y + 2] == color) {
                points++;
            }
        }

        //check diagonal S-E \
        if ((x + 2) < board.length && (y + 2) < board.length) {
            if (board[x + 1][y + 1] == color && board[x + 2][y + 2] == color) {
                points++;
            }
        }

        //check diagonal S-W /
        if ((x - 2) >= 0 && (y + 2) < board.length) {
            if (board[x - 1][y + 1] == color && board[x - 2][y + 2] == color) {
                points++;
            }
        }

        //check diagonals Middle
        if ((x - 1) >= 0 && (y - 1) >= 0 && (x + 1) < board.length && (y + 1) < board.length) {
            //Diagnol Middle Right /
            if (board[x - 1][y - 1] == color && board[x + 1][y + 1] == color) {
                points++;
            }
            //Diagonal Middle Left \
            if (board[x - 1][y + 1] == color && board[x + 1][y + 1] == color) {
                points++;
            }
        }

        return points;
    }

    public CellState[][] changeBoard(int x, int y) {
        CellState[][] gameboard = copyArray();
        for (int i = 0; i < gameboard[0].length; i++) {
            for (int j = 0; j < gameboard[0].length; j++) {
                if (i == x || j == y && gameboard[i][j] != CellState.VACANT) {
                    gameboard[i][j] = CellState.AVAILABLE;
                } else if (gameboard[i][j] != CellState.VACANT) {
                    gameboard[i][j] = CellState.AVAILABLE;
                }
            }
        }
        return gameboard;
    }

    private CellState[][] copyArray() {
        CellState[][] copie = new CellState[board.length][];
        for (int i = 0; i < board.length; i++) {
            copie[i] = board[i].clone();
        }
        return copie;
    }

}
