package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesServerGame {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final int BUFSIZE = 5;
    private ThreeStonesServerGameBoard board;

    public ThreeStonesServerGame() {
        this.board = new ThreeStonesServerGameBoard();
    }

    public void updateBoard(int x, int y, CellState color) {
        board.updateBoard(x, y, color);
    }

    public void startGame() {
        board.initializeGameBoard();
    }

    public void setBoard(ThreeStonesServerGameBoard board) {
        this.board = board;
    }

    public byte[] determineNextMove() {
        List<ThreeStonesMove> possibleMoves = new ArrayList<>();
        CellState[][] gameBoard = board.getBoard();
        //LOOPS THROUGH GAMEBOARD
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //IF CURRENT TILE IS AVAILABLE ADD THE MOVE TO THE LIST
                if (gameBoard[i][j] == CellState.AVAILABLE) {
                    log.debug("determineNextMove CellState.Available");
                    //creates new move with possible values
                    int whitePoints = board.checkForThreeStones(i, j, CellState.WHITE);
                    log.debug("whitePoints " + whitePoints);

                    int blackPoints = board.checkForThreeStones(i, j, CellState.BLACK);
                    log.debug("blackPoints " + blackPoints);
                    ThreeStonesMove move = new ThreeStonesMove(board.checkForThreeStones(i, j, CellState.WHITE), board.checkForThreeStones(i, j, CellState.BLACK), i, j);
                    move.countNearbyTiles(board.getBoard());
                    possibleMoves.add(move);
                }
            }
        }
        //DETERMINE THE BEST MOVE OUT OF THE LIST
        byte[] serverPacket;
        byte[] serverMoves;
        if (possibleMoves.size() > 1) {
            serverMoves = ThreeStonesMove.determineBestMove(possibleMoves).toByte();
        } else {
            serverMoves = possibleMoves.get(0).toByte();
        }
        updateBoard(serverMoves[0], serverMoves[1], CellState.BLACK);
        serverPacket = constructServerMovePacket(serverMoves);
        return serverPacket;
    }

    private byte[] constructServerMovePacket(byte[] serverMove) {
        int whitePnts = board.getWhiteScore();
        int blackPnts = board.getBlackScore();

        //if the current server's move is the last move of the game, check who wins
        if (board.getWhiteStoneCount() == 0 && board.getBlackStoneCount() == 0) {
            if (whitePnts > blackPnts) {
                //opcode of 3 indicates player has won
                return new byte[]{(byte) 3, (byte) serverMove[0],
                    (byte) serverMove[1], (byte) whitePnts, (byte) blackPnts};
            } else if (blackPnts > whitePnts) {
                //opcode of 4 indicates server has won
                return new byte[]{(byte) 4, (byte) serverMove[0],
                    (byte) serverMove[1], (byte) whitePnts, (byte) blackPnts};
            } else {
                //opcode of 5 indicates a tie
                return new byte[]{(byte) 5, (byte) serverMove[0],
                    (byte) serverMove[1], (byte) whitePnts, (byte) blackPnts};
            }
        } else if (board.getWhiteStoneCount() == 1) {
            //opcode of 6 indicates that player has one move left
            return new byte[]{(byte) 6, (byte) serverMove[0],
                (byte) serverMove[1], (byte) whitePnts, (byte) blackPnts};
        } else {
            //opcode of 1 indicates a regular server move
            return new byte[]{(byte) 1, (byte) serverMove[0],
                (byte) serverMove[1], (byte) whitePnts, (byte) blackPnts};
        }
    }

}
