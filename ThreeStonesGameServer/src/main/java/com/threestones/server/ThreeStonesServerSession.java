package com.threestones.server;

import com.threestones.server.gamestate.ThreeStonesServerGame;
import com.threestones.server.gamestate.ThreeStonesServerGameBoard.CellState;
import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and the properties of a game session in the server
 * side in a game of Three Stones such as handling the receiving and the sending
 * of packets between server depending on client's and server's response to each
 * other (e.g sending/receive move, sending/receiving start-game confirmation,
 * etc)
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 *
 */
public class ThreeStonesServerSession {

    //for logging information
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final int BUFSIZE = 5; //size of the packet
    private boolean isGameOver; //boolean to determine if game is over
    private boolean isPlayAgain; //boolean to determine if user wants to play again
    private ThreeStonesServerGame serverGame; //The server's game logic
    private InputStream inStream; //client's input stream
    private OutputStream outStream; //client's output stream

    /**
     * Default constructor that initializes the game over and play again boolean
     * properties and the ThreeStonesServerGame object
     */
    public ThreeStonesServerSession() {
        this.isGameOver = false;
        this.isPlayAgain = true;
        this.serverGame = new ThreeStonesServerGame();
    }

    /**
     * Starts game session by accepting the Socket object created by the
     * client's connection and retrieving the input and output stream. Session
     * will then continously manage the game and the packet send and receive
     * between server/client while user still wants to play and game is not over
     * yet.
     *
     * @param clientSock Socket object
     * @throws IOException
     */
    public void playGameSession(Socket clientSock) throws IOException {
        //retrieve the input and output stream from the client Socket
        inStream = clientSock.getInputStream();
        outStream = clientSock.getOutputStream();

        //continue the game session while user is still playing and game not over
        int recvMsgSize = 0;
        while (isPlayAgain) {
            while (!isGameOver) {
                //receive and process client packets
                recvMsgSize = receiveClientPackets();
            }
        }
        clientSock.close(); //close client socket when user no longer wants to play
    }

    /**
     * Helper method that manages the packets received from the client and
     * deciding the right actions to perform based on the operation code or
     * first byte of the packet received. These are done as long as the
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
            byte opCode = receivedPacket[0];//check for op code at beginning of packet
            switch (opCode) {
                case 0: //start game request from client
                    log.debug("inside receiveClientPackets code 0 - start game request");
                    handleClientGameRequest();
                    break;
                case 1: //player's move
                    log.debug("inside receiveClientPackets code 1 - player's move");
                    handlePlayerMove(receivedPacket);
                    if (isGameOver) {
                        sendPlayAgainRequestToClnt();
                    }
                    break;
                case 2: //player's request to play again
                    log.debug("inside receiveClientPackets code 3 - play again request");
                    handleClientPlayAgainRequest();
                    break;
                case 3: //player's request not to play again
                    log.debug("inside receiveClientPackets code 4 - dont play again request");
                    isPlayAgain = false;
                    return -1;
            }
        }
        return recvMsgSize;
    }

    /**
     * Sends a packet request containing the appropriate operation code that
     * would ask player whether or not to continue playing and receives the
     * player's response.
     *
     * @throws IOException
     */
    private void sendPlayAgainRequestToClnt() throws IOException {
        byte[] outPacket = new byte[BUFSIZE];
        outPacket[0] = 3; //opcode of 3 for request to play again packet message
        outStream.write(outPacket);
        //receiveClientPackets(); //receive client's response
    }

    /**
     * Handles the client's request to play again by restarting the server side
     * game and setting play again to true and isgameover back to false. Then
     * sends back a confirmation message to client that game has been restarted
     * and player can resume playing
     *
     * @throws IOException
     */
    private void handleClientPlayAgainRequest() throws IOException {
        serverGame.startGame(); //restart a new game
        isPlayAgain = true;
        isGameOver = false;

        //send a confirmation to client that game is restarted
        byte[] playAgainConfirmPacket = new byte[BUFSIZE];
        playAgainConfirmPacket[0] = 2; //opcode of 2 confirms that client can play again
        sendServerPacketToClient(playAgainConfirmPacket);
    }

    /**
     * Handles the player's move when server receives packet of operation code
     * of 1. Retrieves the x and y coords from the packet and updates the board.
     * Server then makes its own move and then builds the packet to send back to
     * client with opcode, its coord x, coord y, and points of both white and
     * black.
     *
     * @param receivedPacket
     * @throws IOException
     */
    private void handlePlayerMove(byte[] receivedPacket) throws IOException {
        int coordMoveX = receivedPacket[1];
        int coordMoveY = receivedPacket[2];
        serverGame.updateBoard(coordMoveX, coordMoveY, CellState.WHITE);
        byte[] serverMovesPoint = serverGame.determineNextMove(); //[opcode,x,y,white,black]

        //check if the operation code of outgoing packet is 3 or 4 and if yes, the game is over 
        //and server has made its last move
        if (serverMovesPoint[0] == 3 || serverMovesPoint[0] == 4) {
            isGameOver = true;
        }
        sendServerPacketToClient(serverMovesPoint);
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
        serverGame.startGame();
        //null refers to a code 0 which indicates a start game confirmation
        sendServerPacketToClient(null);
    }

    /**
     * Sends packet from server to client with the input byte array. If input is
     * null, create a game started message to client with 0 opcode
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
