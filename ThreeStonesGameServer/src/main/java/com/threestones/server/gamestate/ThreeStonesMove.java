package com.threestones.server.gamestate;

/**
 *
 * @author Jacob
 */
public class ThreeStonesMove {

    private int whitePoints;
    private int blackPoints;
    private int x;
    private int y;

    public ThreeStonesMove(int whitePoints, int blackPoints, int x, int y) {
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.x = x;
        this.y = y;
    }

    public ThreeStonesMove() {
    }

    public int getWhitePoints() {
        return whitePoints;
    }

    public int getBlackPoints() {
        return blackPoints;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Returns Highest scoring value between whitePoints and blackPoints
    public int getMoveValue() {
        if (whitePoints > blackPoints) {
            return whitePoints;
        }
        return blackPoints;
    }

    //converts move to a byte array for server message
    public byte[] toByte() {
        return new byte[]{(byte) blackPoints, (byte) x, (byte) y};
    }
    @Override
    public String toString(){
        return  "W:" + whitePoints + " B:" + blackPoints + " X:" + x + " Y:" + y;
    }
}
