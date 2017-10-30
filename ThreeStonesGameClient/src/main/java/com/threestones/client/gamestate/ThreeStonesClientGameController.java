package com.threestones.client.gamestate;

import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
import com.threestones.client.packet.ThreeStonesClientPacket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and properties of the game controller of the client
 * side application of Three Stones game. Handles the click listener of the
 * buttons on the game board and manages the packet that are sent to the server
 * and the packets received from the server. Builds the appropriate packet to be
 * sent to the server with the right operation code and data and updates the
 * client game board UI accordingly. Executes the right tasks when a packet is
 * received from server.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesClientGameController {

    //for logging information on the console
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final ThreeStonesClientGameBoard board; //client game board instance

    private final int port = 50000; //the port in which the server is listening
    private Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private final int BUFF_SIZE = 5; //fixed size packet byte array 

    /**
     * Default constructor that initializes the client game board and starts a
     * new game
     */
    public ThreeStonesClientGameController() {
        board = new ThreeStonesClientGameBoard();
        board.startNewGame();
    }

    /**
     * Returns client game board.
     *
     * @return ThreeStonesClientGameBoard object
     */
    public ThreeStonesClientGameBoard getBoard() {
        return board;
    }

    /**
     * Handles the click event on the the buttons of the game board. Sends a
     * packet to the server containing the server's move in order to validate
     * move and receives the confirmation packet from the server.
     *
     * @param x coord x position
     * @param y coord y position
     */
    public void handleClickBoardCell(int x, int y) {
        log.debug("Handling board cell click");
        //send move to server  and receive validation response
        sendClientMovePacketToServer(x, y);
    }

    /**
     * Receives the server's packet and handles the server response by executing
     * the right task depending on the operation code which is the first byte of
     * the packet byte array. For each type of server response packet, the game
     * controller will update the board which will then be displayed on the UI
     * for the user to see the changes and updates.
     *
     * @throws IOException
     */
    public void handleServerResponse() throws IOException {
        byte[] byteBuffer = receiveServerPacket();
        //examine the operation code first byte and for each case update the board
        //with a message code if any
        switch (byteBuffer[0]) {
            case -2: //player's move is valid
                log.debug("code -2 valid player move");
                board.updateBoard(byteBuffer[1], byteBuffer[2],
                        -1, -1, CellState.WHITE, 10);
                sendRequestForServerMove();
                break;
            case -1: //player's move is invalid
                log.debug("code -1 invalid player move");
                board.updateBoard(-1, -1, -1, -1, null, -1);
                break;
            case 1: //servers's normal move
                log.debug("code 1 receive servers's move");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3],
                        byteBuffer[4], CellState.BLACK, 10);
                break;
            case 3: //Server's move with player won message
                log.debug("code 3 Player won");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3],
                        byteBuffer[4], CellState.BLACK, 1);
                break;
            case 4: //Server's move with server won message
                log.debug("code 4 Server won");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3],
                        byteBuffer[4], CellState.BLACK, 2);
                break;
            case 5: //Server's move with tie game message
                log.debug("code 5 Game Tie");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3],
                        byteBuffer[4], CellState.BLACK, 0);
                break;
            case 6: //player has one move left
                log.debug("code 6 Player has one move left");
                board.updateBoard(byteBuffer[1], byteBuffer[2], byteBuffer[3],
                        byteBuffer[4], CellState.BLACK, -2);
        }
    }

    private void sendRequestForServerMove() {
        log.info("Sending client request for server's move");

        //build the packet containing the server's move coords and the 1 opcode
        //which indicates a player's move for the server to receive
        byte[] requestForServerMovePacket = new byte[BUFF_SIZE];
        requestForServerMovePacket[0] = 4;

        try {
            this.outStream.write(requestForServerMovePacket);
            handleServerResponse();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.
                    getName()).log(Level.SEVERE, "Problem sending client packet to server", ex);
        }
    }

    /**
     * Builds a client packet containing the player's move x and y coordinates
     * and sends the packet byte array to server through the output stream of
     * the socket connection.
     *
     * @param x coords x position
     * @param y coords y position
     */
    public void sendClientMovePacketToServer(int x, int y) {
        log.info("Sending client packet to server");
        log.debug("position x " + x + "position y " + y);

        //build the packet containing the server's move coords and the 1 opcode
        //which indicates a player's move for the server to receive
        byte[] playerMovePacket = {(byte) 1, (byte) x, (byte) y, (byte) 0, (byte) 0};

        try {
            this.outStream.write(playerMovePacket);
            handleServerResponse(); //get the server's confirmation if move is valid or not
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.
                    getName()).log(Level.SEVERE, "Problem sending client packet to server", ex);
        }
    }

    /**
     * Builds a start game request packet with the first byte containing the
     * operation code 0 which indicates a request to the server to start a game
     * and build a game board. Receives the server's response of 0 if server has
     * received the request packet. Returns a boolean true or false whether or
     * not the server sent a response packet with an operation code of 0.
     *
     * @return boolean true or false whether server sent a response of 0 opcode
     */
    public boolean sendStartGameRequestToServer() {
        log.debug("inside sendStartGameRequestToServer");
        byte[] receivedPacket = new byte[BUFF_SIZE];
        receivedPacket[0] = -1;
        try {
            //build and send a start game request to the server
            byte[] startGameRequestPacket = new byte[BUFF_SIZE];
            startGameRequestPacket[0] = 0;
            outStream.write(startGameRequestPacket);

            //receive the server's reponse
            receivedPacket = receiveServerPacket();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.
                    getName()).log(Level.SEVERE, "Problem sending client packet to server", ex);
        }
        return receivedPacket[0] == 0;
    }

    /**
     * Creates a connection with a server by instantiating a client Socket
     * object with the same port in which the server is listening to and the ip
     * address of the client's machine. Retrieves the output and input stream of
     * the socket object created.
     *
     * @param address String IP address of the client's machine
     * @throws IOException
     */
    public void createConnectionWithServer(String address) throws IOException {
        socket = new Socket(address, port);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
    }

    /**
     * Builds a play again request packet with the first byte containing the
     * operation code 2 which indicates a request to the server to play again
     * and reinitialize the game board. Receives the server's response of 2 if
     * server has received the request packet. Returns a boolean true or false
     * whether or not the server sent a confirmation packet with an operation
     * code of 2.
     *
     * @return boolean true or false whether server sent a response of 2 opcode
     */
    public boolean sendPlayAgainRequestToServer() {
        byte[] receivedPacket = new byte[BUFF_SIZE];
        try {
            //build and send the play again request packet
            byte[] playAgainRequestPacket = new byte[BUFF_SIZE];
            playAgainRequestPacket[0] = 2;
            outStream.write(playAgainRequestPacket);

            //receive the server's confirmation
            receivedPacket = receiveServerPacket();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.
                    getName()).log(Level.SEVERE, "Problem sending client packet to server", ex);
        }
        return receivedPacket[0] == 2;
    }

    /**
     * Builds a quit game request packet with the first byte containing the
     * operation code 3 which indicates a request to the server to quit game and
     * close socket connection.
     */
    public void sendQuitGameRequestToServer() {
        try {
            //build and send the quit game request to the server
            byte[] quitRequestPacket = new byte[BUFF_SIZE];
            quitRequestPacket[0] = 3;
            outStream.write(quitRequestPacket);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.
                    getName()).log(Level.SEVERE, "Problem sending client packet to server", ex);
        }
    }

    /**
     * Receives the server's packet response from the input stream of the socket
     * connection and returns the packet byte array.
     *
     * @return packet byte array containing server's response
     * @throws SocketException
     * @throws IOException
     */
    public byte[] receiveServerPacket() throws SocketException, IOException {
        byte[] byteBuffer = new byte[BUFF_SIZE];
        int totalBytesRcvd = 0;
        int bytesRcvd;
        log.debug("Receiving server packet");
        while (totalBytesRcvd < byteBuffer.length) {
            if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd,
                    byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection close prematurely");
            }
            totalBytesRcvd += bytesRcvd;
        }
        return byteBuffer;
    }
}
