package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;

/**
 * Encapsulates the behavior and properties of server move which includes the x
 * and y coordinates, the possible points for white and black stones that could
 * be generated by the move, and the amount of white and black stones that are
 * around the x and y coordinates of the move.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesServerMove {

    //possible points generated by the move
    private int whitePoints;
    private int blackPoints;

    //x and y coordinates of the move
    private int coordX;
    private int coordY;

    //number of white and black stones around the coordinates of the move
    private int nearbyWhiteStones;
    private int nearbyBlackStones;

    /**
     * Default constructor
     */
    public ThreeStonesServerMove() {

    }

    /**
     * Initializes the white and black points with the input points, the stones
     * count to their default value, and the coordinates with the input x and y
     * coordinates.
     *
     * @param whitePoints points from the white stones
     * @param blackPoints points from the black stones
     * @param x coordinates x
     * @param y coordinates y
     */
    public ThreeStonesServerMove(int whitePoints, int blackPoints, int x, int y) {
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.coordX = x;
        this.coordY = y;
        this.nearbyWhiteStones = 0;
        this.nearbyBlackStones = 0;
    }

    /**
     * Returns the total number of white and black stones around the coordinates
     * of the move
     *
     * @return int total stones
     */
    public int getTotalNearbyStones() {
        return nearbyWhiteStones + nearbyBlackStones;
    }

    /**
     * Returns the total white stones around the coordinates of the move
     *
     * @return int total white stones
     */
    public int getNearbyWhiteStones() {
        return nearbyWhiteStones;
    }

    /**
     * Returns the total black stones around the coordinates of the move
     *
     * @return int total black stones
     */
    public int getNearbyBlackStones() {
        return nearbyBlackStones;
    }

    /**
     * Returns the total white points that can be generated by the move
     *
     * @return int total white points
     */
    public int getWhitePoints() {
        return whitePoints;
    }

    /**
     * Returns the total black points that can be generated by the move
     *
     * @return int total black points
     */
    public int getBlackPoints() {
        return blackPoints;
    }

    /**
     * Returns the x coordinates of the move
     *
     * @return int x coord
     */
    public int getXCoord() {
        return coordX;
    }

    /**
     * Returns the y coordinate of the move
     *
     * @return int y coord
     */
    public int getYCoord() {
        return coordY;
    }

    /**
     * Returns Highest scoring value between whitePoints and blackPoints
     *
     * @return int highest score
     */
    public int getHighestScore() {
        if (whitePoints > blackPoints) {
            return whitePoints;
        }
        return blackPoints;
    }

    /**
     * Converts the move coordinates into a byte array and returns the array.
     *
     * @return byte array containing the move coords
     */
    public byte[] toByte() {
        return new byte[]{(byte) coordX, (byte) coordY};
    }

    /**
     * Scans around the x,y location of the move and counts the amount of white
     * and black stones that are around the move's coordinates.
     *
     * @param board CellState 2D array representing the game board
     */
    public void countNearbyStones(CellState[][] board) {
        for (int i = coordX - 1; i <= coordX + 1; i++) {
            for (int j = coordY - 1; j <= coordY + 1; j++) {
                if (!(i == coordX && j == coordY)) {
                    if (board[i][j] == CellState.WHITE) {
                        nearbyWhiteStones++;
                    } else if (board[i][j] == CellState.BLACK) {
                        nearbyBlackStones++;
                    }
                }
            }
        }
    }

    /**
     * Returns a String representation of a ThreeStonesServerMove instance and
     * its properties.
     *
     * @return String 
     */
    @Override
    public String toString() {
        return "W:" + whitePoints + " B:" + blackPoints + " X:" + coordX + " Y:" + coordY;
    }
}
