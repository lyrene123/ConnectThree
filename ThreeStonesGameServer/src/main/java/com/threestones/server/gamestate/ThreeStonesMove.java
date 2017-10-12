/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public int getMoveValue(){
        if (whitePoints > blackPoints)
            return whitePoints;
        return blackPoints;
    }
}
