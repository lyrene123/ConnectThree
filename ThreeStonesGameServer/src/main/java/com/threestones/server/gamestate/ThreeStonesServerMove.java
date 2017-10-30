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
public class ThreeStonesServerMove {

    private static final org.slf4j.Logger LOG_STATIC = LoggerFactory.getLogger(ThreeStonesServerMove.class);
    private int whitePoints;
    private int blackPoints;
    private int coordX;
    private int coordY;
    private int nearbyWhiteStones;
    private int nearbyBlackStones;

    public ThreeStonesServerMove(int whitePoints, int blackPoints, int x, int y) {
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.coordX = x;
        this.coordY = y;
        this.nearbyWhiteStones = 0;
        this.nearbyBlackStones = 0;
    }

    public void countNearbyTiles(CellState[][] board) {
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

    public int getNearbyTiles() {
        return nearbyWhiteStones + nearbyBlackStones;
    }

    public int getNearbyWhiteTiles() {
        return nearbyWhiteStones;
    }

    public int getNearbyBlackTiles() {
        return nearbyBlackStones;
    }

    public ThreeStonesServerMove() {
    }

    public int getWhitePoints() {
        return whitePoints;
    }

    public int getBlackPoints() {
        return blackPoints;
    }

    public int getX() {
        return coordX;
    }

    public int getY() {
        return coordY;
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
        return new byte[]{(byte) coordX, (byte) coordY};
    }

    @Override
    public String toString() {
        return "W:" + whitePoints + " B:" + blackPoints + " X:" + coordX + " Y:" + coordY;
    }

    public static ThreeStonesServerMove determineBestMove(List<ThreeStonesServerMove> moves) {
        LOG_STATIC.debug("determining best move");
        int biggestPossibleBlackPoints = 0;
        int biggestPossibleWhitePoints = 0;
        List<ThreeStonesServerMove> possibleWhiteScores = new ArrayList<>();
        List<ThreeStonesServerMove> possibleBlackScores = new ArrayList<>();

        //Starts by determining by move value
        for (ThreeStonesServerMove m : moves) {
            LOG_STATIC.debug("move: " + m.toString());
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
            LOG_STATIC.debug("determining best move by proximity");
            return determineMoveByPromixty(moves);
        } //No possible scoring with black but some with white
        else if (biggestPossibleBlackPoints == 0 && biggestPossibleWhitePoints > 0) {
            LOG_STATIC.debug("determining best move by white");
            return Collections.max(possibleWhiteScores, WhiteMoveComparator);
        } //No possible white scoring
        else if (biggestPossibleBlackPoints > 0 && biggestPossibleWhitePoints == 0) {
            LOG_STATIC.debug("determining best move by black");
            return Collections.max(possibleBlackScores, BlackMoveComparator);
        }
        LOG_STATIC.debug("determining best move by elimination");
        return determineMoveByElimination(possibleWhiteScores, possibleBlackScores);

    }

    private static ThreeStonesServerMove determineMoveByElimination(List<ThreeStonesServerMove> whiteMoves, List<ThreeStonesServerMove> blackMoves) {
        List<ThreeStonesServerMove> bestMoves = new ArrayList<>();
        int bestMoveValue = -15;
        for (ThreeStonesServerMove tsBlack : blackMoves) {
            int maxWhite = 0;
            for (ThreeStonesServerMove tsWhite : whiteMoves) {
                if ((tsWhite.getX() == tsBlack.getX() || tsWhite.getY() == tsBlack.getY()) && !(tsWhite.equals(tsBlack))) {
                    maxWhite = (maxWhite > tsWhite.getWhitePoints()) ? maxWhite : tsWhite.getWhitePoints();
                }
            }
            if (tsBlack.getBlackPoints() - maxWhite > bestMoveValue) {
                bestMoves.clear();
                bestMoves.add(tsBlack);
            } else if (tsBlack.getBlackPoints() - maxWhite == bestMoveValue) {
                bestMoves.add(tsBlack);
            }
        }
        if (bestMoves.size() > 1) {
            return bestMoves.get((int) (Math.random() * bestMoves.size()));
        }

        return bestMoves.get(0);
    }

    private static ThreeStonesServerMove determineMoveByPromixty(List<ThreeStonesServerMove> moves) {
        int mostNearbyTiles = 0;
        List<ThreeStonesServerMove> bestMoves = new ArrayList<>();
        for (ThreeStonesServerMove m : moves) {
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

    private static Comparator<ThreeStonesServerMove> WhiteMoveComparator = ((ThreeStonesServerMove move1, ThreeStonesServerMove move2) -> move1.getWhitePoints() - move2.getWhitePoints());
    private static Comparator<ThreeStonesServerMove> BlackMoveComparator = ((ThreeStonesServerMove move1, ThreeStonesServerMove move2) -> move1.getBlackPoints() - move2.getBlackPoints());
}
