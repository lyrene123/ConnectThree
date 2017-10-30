package com.threestones.server;

import com.threestones.server.gamestate.ThreeStonesServerGameBoard;
import com.threestones.server.gamestate.ThreeStonesServerGameController;
import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import com.threestones.server.gamestate.ThreeStonesServerMove;
import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and the properties of a game session handled by the
 * server side application of the three stones game. In a game session, the
 * server handles the receiving and the sending of packets between the server
 * and the client side application. Depending on the type of packet message
 * received, the server will execute tasks and respond accordingly.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 *
 */
public class ThreeStonesServerSession extends Thread{

    //for logging information on the console
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final int BUFSIZE = 5; //fix size of the packet
    private boolean isGameOver; //boolean to determine if game is over
    private boolean isPlayAgain; //boolean to determine if user wants to play again
    private final ThreeStonesServerGameController serverGameCont; //The server's game logic instance
    private InputStream inStream; //client's socket input stream
    private OutputStream outStream; //client's socket output stream

    /**
     * Default constructor that initializes the game over and play again boolean
     * properties and the ThreeStonesServerGameController instance
     */
    public ThreeStonesServerSession() {
        this.isGameOver = false;
        this.isPlayAgain = true;
        this.serverGameCont = new ThreeStonesServerGameController();
    }

    /**
     * Starts game session by accepting the Socket object created by the
     * client's connection and retrieving the input and output stream. Server
     * will then continously manage the game and the packet send and receive
     * between server/client while user still wants to play and game is not over
     * yet.
     *
     * @param clientSock Client Socket object
     * @throws IOException
     */
    public void run(Socket clientSock) throws IOException {
        //retrieve the input and output stream from the client Socket
        inStream = clientSock.getInputStream();
        outStream = clientSock.getOutputStream();

        //continue the game session while user is still playing and game not over
        while (isPlayAgain) {
            while (!isGameOver) {
                receiveClientPackets(); //receive and process client packets
            }
        }
        clientSock.close(); //close client socket when user no longer wants to play
    }

    /**
     * Helper method that manages the packets received from the client and
     * deciding the right actions to perform based on the operation code which
     * is the first byte of the packet received. These are done as long as the
     * connection with the client is maintained or the game is over or the play
     * again request is declined by player.
     *
     * @return int size of bytes read from packet or -1 if connection is closed
     * @throws IOException
     */
    private int receiveClientPackets() throws IOException {
        int recvMsgSize = 0;
        byte[] receivedPacket = new byte[BUFSIZE];

        // Receive until client closes connection, indicated by -1 return
        while ((recvMsgSize = inStream.read(receivedPacket)) != -1) {
            //check for op code at beginning of packet
            switch (receivedPacket[0]) {
                case 0: //start game request from client
                    log.debug("inside receiveClientPackets code 0 - start game request");
                    handleClientGameRequest();
                    break;
                case 1: //player's move
                    log.debug("inside receiveClientPackets code 1 - player's move");
                    validatePlayerMove(receivedPacket);
                    break;
                case 2: //player's request to play again
                    log.debug("inside receiveClientPackets code 2 - play again request");
                    handleClientPlayAgainRequest();
                    break;
                case 3: //player's request not to play again
                    log.debug("inside receiveClientPackets code 3 - dont play again request");
                    isPlayAgain = false;
                    return -1; //exit loop
                case 4: //player's request for server's move
                    log.debug("inside receiveClientPackets code 3 - request for server's move");
                    sendServerMoveToClient();
                    break;
            }
        }
        return recvMsgSize;
    }

    /**
     * Handles the client's request to play again by restarting the server side
     * game and setting play again boolean to true and isgameover boolean back
     * to false. Then server sends back a confirmation message to client that
     * game has been restarted and player can resume playing
     *
     * @throws IOException
     */
    private void handleClientPlayAgainRequest() throws IOException {
        log.info("Handling player's request to play again");
        serverGameCont.initServerGame(); //restart a new game
        isPlayAgain = true;
        isGameOver = false;

        //send a confirmation with opcode 2 to client that game is restarted
        byte[] playAgainConfirmPacket = new byte[BUFSIZE];
        playAgainConfirmPacket[0] = 2; //opcode of 2 confirms that client can play again
        sendServerPacketToClient(playAgainConfirmPacket);
    }

    private void sendServerMoveToClient() throws IOException {
        //get the server's move containing [opcode,x,y,white points,black points]
        byte[] serverMovesPoint = serverGameCont.determineNextServerMove();

        //check if the operation code of outgoing packet is 3-4-5 and if yes, the game is over 
        //and server has made its last move
        if (serverMovesPoint[0] == 3 || serverMovesPoint[0] == 4 || serverMovesPoint[0] == 5) {
            isGameOver = true;
        }
        sendServerPacketToClient(serverMovesPoint);
    }

    private boolean validatePlayerMove(byte[] receivedPacket) {
        log.info("Validating Player's move....");
        int coordX = receivedPacket[1];
        int coordY = receivedPacket[2];
        ThreeStonesServerMove lastPlayedServerMove = serverGameCont.getLastPlayedServerMove();
        try {
            if (lastPlayedServerMove != null) {
                ThreeStonesServerGameBoard board = serverGameCont.getGameBoard();
                if (board.getBoard()[coordX][coordY] == CellState.AVAILABLE) {
                    log.debug("validatePlayerMove AVAILABLE");
                    sendValidMoveConfirmationToClnt(coordX, coordY);
                    return true;
                }

                log.debug("Server move x: " + lastPlayedServerMove.getXCoord()
                        + " move y: " + lastPlayedServerMove.getYCoord());
                log.debug("Player clicked: " + coordX + "-" + coordY);
                sendInvalidMoveConfirmationToClnt();
            } else {
                sendValidMoveConfirmationToClnt(coordX, coordY);
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreeStonesServerSession.class.
                    getName()).log(Level.SEVERE, "Problem sending server packet to client", ex);
        }
        return false;
    }

    private void sendValidMoveConfirmationToClnt(int coordX, int coordY) throws IOException {
        log.info("Player move validated, sending a VALID move confirmation");
        serverGameCont.updateBoard(coordX, coordY, CellState.WHITE);
        byte[] validMoveConfirmPacket = new byte[BUFSIZE];
        validMoveConfirmPacket[0] = -2;
        validMoveConfirmPacket[1] = (byte) coordX;
        validMoveConfirmPacket[2] = (byte) coordY;
        sendServerPacketToClient(validMoveConfirmPacket);
    }

    private void sendInvalidMoveConfirmationToClnt() throws IOException {
        log.info("Player move validated, sending a INVALID move confirmation");
        byte[] invalidMoveConfirmPacket = new byte[BUFSIZE];
        invalidMoveConfirmPacket[0] = -1;
        sendServerPacketToClient(invalidMoveConfirmPacket);
    }

    /**
     * Handles the client's request to start a game by starting the server side
     * game and by sending back a confirmation message that player can start
     * playing
     *
     * @throws IOException
     */
    private void handleClientGameRequest() throws IOException {
        //start the server side game 
        serverGameCont.initServerGame();
        //null refers to a code 0 which indicates a start game confirmation to the client
        sendServerPacketToClient(null);
    }

    /**
     * Sends a packet from server to client with an input byte array. If array
     * is null, create a packet with opcode of 0 which indicates a game started
     * message to client with 0 opcode.
     *
     * @param values byte array to send in a packet to client
     * @throws IOException
     */
    private void sendServerPacketToClient(byte[] serverPacket) throws IOException {
        //if byte array is null, then create game started message to client with 0 opcode
        if (serverPacket == null) {
            serverPacket = new byte[BUFSIZE];
            serverPacket[0] = 0; //game has started confirmation
        }
        outStream.write(serverPacket, 0, serverPacket.length); //send packet through outputstream
    }
}
