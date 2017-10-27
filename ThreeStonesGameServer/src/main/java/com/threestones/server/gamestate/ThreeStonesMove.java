package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jacob
 */
public class ThreeStonesMove {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final org.slf4j.Logger logStatic = LoggerFactory.getLogger(ThreeStonesMove.class);
    private int whitePoints;
    private int blackPoints;
    private int x;
    private int y;
    private int nearbyWhite;
    private int nearbyBlack;

    public ThreeStonesMove(int whitePoints, int blackPoints, int x, int y) {
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.x = x;
        this.y = y;
        this.nearbyWhite = 0;
        this.nearbyBlack = 0;
    }

    public void countNearbyTiles(CellState[][] board) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!(i == x && j == y)) {
                    if (board[i][j] == CellState.WHITE) {
                        nearbyWhite++;
                    } else if (board[i][j] == CellState.BLACK) {
                        nearbyBlack++;
                    }
                }
            }
        }
    }

    public int getNearbyTiles() {
        return nearbyWhite + nearbyBlack;
    }

    public int getNearbyWhiteTiles() {
        return nearbyWhite;
    }

    public int getNearbyBlackTiles() {
        return nearbyBlack;
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
    public String toString() {
        return "W:" + whitePoints + " B:" + blackPoints + " X:" + x + " Y:" + y;
    }

    public static ThreeStonesMove determineBestMove(List<ThreeStonesMove> moves) {
        logStatic.debug("determining best move");
        int biggestPossibleBlackPoints = 0;
        int biggestPossibleWhitePoints = 0;
        List<ThreeStonesMove> possibleWhiteScores = new ArrayList<ThreeStonesMove>();
        List<ThreeStonesMove> possibleBlackScores = new ArrayList<ThreeStonesMove>();

        //Starts by determining by move value
        for (ThreeStonesMove m : moves) {
            logStatic.debug("move: " + m.toString());
            if (m.getWhitePoints() > 0) {
                possibleWhiteScores.add(m);
                if (m.getWhitePoints() > biggestPossibleWhitePoints) {
                    biggestPossibleWhitePoints = m.getWhitePoints();
                }
            }

            if (m.getBlackPoints() > 0) {
                possibleBlackScores.add(m);
                if (m.getBlackPoints() > biggestPossibleBlackPoints) {
                    biggestPossibleBlackPoints = m.getBlackPoints();
                }
            }
        }

        if (biggestPossibleBlackPoints == 0 && biggestPossibleWhitePoints == 0) {
            logStatic.debug("determining best move by proximity");
            return determineMoveByPromixty(moves);
        } //No possible scoring with black but some with white
        else if (biggestPossibleBlackPoints == 0 && biggestPossibleWhitePoints > 0) {
            logStatic.debug("determining best move by white");
            return Collections.max(possibleWhiteScores, WhiteMoveComparator);
        } //No possible white scoring
        else if (biggestPossibleBlackPoints > 0 && biggestPossibleWhitePoints == 0) {
            logStatic.debug("determining best move by black");
            return Collections.max(possibleBlackScores, BlackMoveComparator);
        }
        logStatic.debug("determining best move by elimination");
        return determineMoveByElimination(possibleWhiteScores, possibleBlackScores);

    }

    private static ThreeStonesMove determineMoveByElimination(List<ThreeStonesMove> whiteMoves, List<ThreeStonesMove> blackMoves) {
        List<ThreeStonesMove> bestMoves = new ArrayList<>();
        int bestMoveValue = -15;
        for (ThreeStonesMove m : blackMoves) {
            int maxWhite = 0;
            for (ThreeStonesMove w : whiteMoves) {
                if ((w.getX() == m.getX() || w.getY() == m.getY()) && !(w.equals(m))) {
                    maxWhite = (maxWhite > w.getWhitePoints()) ? maxWhite : w.getWhitePoints();
                }
            }
            if (m.getBlackPoints() - maxWhite > bestMoveValue) {
                bestMoves.clear();
                bestMoves.add(m);
            } else if (m.getBlackPoints() - maxWhite == bestMoveValue) {
                bestMoves.add(m);
            }
        }
        if (bestMoves.size() > 1) {
            return bestMoves.get((int) (Math.random() * bestMoves.size()));
        }

        return bestMoves.get(0);
    }

    private static ThreeStonesMove determineMoveByPromixty(List<ThreeStonesMove> moves) {
        int mostNearbyTiles = 0;
        List<ThreeStonesMove> bestMoves = new ArrayList<>();
        for (ThreeStonesMove m : moves) {
            if (m.getNearbyTiles() > mostNearbyTiles) {
                bestMoves.clear();
                bestMoves.add(m);
            } else if (m.getNearbyTiles() == mostNearbyTiles) {
                bestMoves.add(m);
            }
        }
        if (bestMoves.size() > 1) {
            return bestMoves.get((int) (Math.random() * bestMoves.size()));
        }

        return bestMoves.get(0);
    }

    private static Comparator<ThreeStonesMove> WhiteMoveComparator = ((ThreeStonesMove move1, ThreeStonesMove move2) -> move1.getWhitePoints() - move2.getWhitePoints());
    private static Comparator<ThreeStonesMove> BlackMoveComparator = ((ThreeStonesMove move1, ThreeStonesMove move2) -> move1.getBlackPoints() - move2.getBlackPoints());
}
