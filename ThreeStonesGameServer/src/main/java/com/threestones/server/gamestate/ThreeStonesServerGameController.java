package com.threestones.server.gamestate;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and properties of the game controller of the server
 * side the three stones game application. Manages the possible moves the server
 * can make based on the client's move and chooses the best possible move.
 * Builds the byte array containing the server's move with the appropriate
 * operation code and the white and black points generated by the server's new
 * move. For every move made by the server or received by the client, updates
 * the board with the x and y coords and the stone color accordingly.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesServerGameController {

    //for logging info on the console
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    //server game board instance
    private final ThreeStonesServerGameBoard board;

    /**
     * Default constructor that initializes the server game board
     */
    public ThreeStonesServerGameController() {
        this.board = new ThreeStonesServerGameBoard();
    }

    /**
     * Updates the board with the x and y coords and the stone color.
     *
     * @param x x coord position
     * @param y y coord position
     * @param color stone color CellState enum
     */
    public void updateBoard(int x, int y, CellState color) {
        board.updateBoardWithNewMove(x, y, color);
    }

    /**
     * Initializes the game by creating and preparing the game board of the
     * server
     */
    public void initServerGame() {
        board.initializeGameBoard();
    }

    /**
     * Scans through the whole game board and considers all possible moves that
     * are valid that the server could do and chooses the best move that would
     * most beneficial to the server as much as possible. Returns the byte array
     * packet containing the server's move.
     *
     * @return byte array packet containing server's move
     */
    public byte[] determineNextServerMove() {
        log.debug("determineNextMove - server getting possible moves");
        //list of ThreeStonesServerMove objects representing the possible moves of the server
        List<ThreeStonesServerMove> possibleMoves = new ArrayList<>();

        //retrieve the array of CellState representing the updated server game board
        CellState[][] gameBoard = board.getBoard();

        //loop through the server game board
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                //only consider the available cells to add to the list of possible moves
                if (gameBoard[i][j] == CellState.AVAILABLE) {
                    log.debug("determineNextMove - server adding a possible move");
                    /*
                    * Calculate the points of all three stones combination that 
                    * could be generated by a specific move [i,j] for both black 
                    * and white stones and then create a ThreeStonesServerMove instance 
                    * to encapsulate those information
                     */
                    ThreeStonesServerMove move = new ThreeStonesServerMove(board.calculateThreeStonesPoints(i, j, CellState.WHITE),
                            board.calculateThreeStonesPoints(i, j, CellState.BLACK), i, j);
                    move.countNearbyTiles(board.getBoard());
                    possibleMoves.add(move); //add ThreeStonesServerMove instance to the list of possible moves
                }
            }
        }
        //choose the best move for the server among the list of possible moves 
        return chooseBestServerMove(possibleMoves);
    }

    /**
     * Chooses the best move for the server that would be more beneficial for
     * him as much as possible. Updates the game board with the server's new
     * move and builds the byte array packet containing the appropriate
     * operation code, the server's x and y coord move, and the calculated white
     * and black points so far from the board.
     *
     * @param possibleMoves List of possible moves for the server
     * @return a byte array packet containing server's move
     */
    private byte[] chooseBestServerMove(List<ThreeStonesServerMove> possibleMoves) {
        byte[] serverPacket;
        byte[] serverMoves;

        if (possibleMoves.size() > 1) {
            //if there are more than one possible move, choose the best one
            serverMoves = ThreeStonesServerMove.determineBestMove(possibleMoves).toByte();
        } else {
            //if there are only one possible move, take that one
            serverMoves = possibleMoves.get(0).toByte();
        }
        //update the server board with the server's new move
        updateBoard(serverMoves[0], serverMoves[1], CellState.BLACK);
        //construct and return server packet move
        serverPacket = constructServerMovePacket(serverMoves);
        return serverPacket;
    }

    /**
     * Constructs and returns the server packet move with the appropriate
     * operation code depending on the current situation of the game along with
     * the server's x and y move coords, and the white and black points total
     * calculated so far on the board.
     *
     * @param serverMove byte array containing the x and y coords of server move
     * @return complete byte array containing opcode, server move, white/black
     * points
     */
    private byte[] constructServerMovePacket(byte[] serverMove) {
        //retrieve the white and black points calculated so far from the board
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
