package com.threestones.server;

import com.threestones.server.gamestate.ThreeStonesServerGame;
import java.io.*;   // for IOException and Input/OutputStream
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior and the characteristics of a game session in the
 * server side in a game of Three Stones such as handling the receiving and the
 * sending of packets between server and client.
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 *
 */
public class ThreeStonesServerSession {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final int BUFSIZE = 4;
    private boolean isGameOver;
    private boolean isPlayAgain;
    private ThreeStonesServerGame serverGame;
    private InputStream inStream;
    private OutputStream outStream;

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
     * will then continue to manage the game and the packet send and receive
     * between server/client while user still wants to play and game is not over
     * yet.
     *
     * @param clientSock Socket object
     * @throws IOException
     */
    public void playGameSession(Socket clientSock) throws IOException {
        //retrieve the input and output stream from the Socket
        inStream = clientSock.getInputStream();
        outStream = clientSock.getOutputStream();

        //continue the session while user still playing and game not over
        int recvMsgSize = 0;
        byte[] receivedPacket = new byte[BUFSIZE];
        while (isPlayAgain) {
            while (!isGameOver) {
                //receive and process client packets
                recvMsgSize = receiveClientPackets(recvMsgSize, receivedPacket);

                //if game over or connection is closed
                if (isGameOver || recvMsgSize == -1) {
                    if (recvMsgSize == -1) {
                        isPlayAgain = false; //connection is closed, so don't play again
                    }
                    break; //break from inner loop since game is done
                }
            }
            //ask to play again with OutputStream if connection is not closed
            if (isGameOver && recvMsgSize != -1) {
                byte[] outPacket = new byte[BUFSIZE];
                outPacket[0] = 3; //build play again request message to client
                outStream.write(outPacket);
                receiveClientPackets(recvMsgSize, receivedPacket); //receive client's response
            }
        }
        clientSock.close(); //close client socket
    }

    /**
     * Helper method that manages the packets received from the client and
     * deciding the right actions to perform based on the operation code or
     * first byte of the packet received. These are done as long as the
     * connection with the client is maintained or the game is over or the play
     * again request is declined by player.
     *
     * @param recvMsgSize int size of bytes read from packet
     * @param receivedPacket byte array containing data of packet
     * @return int size of bytes read from packet or -1 if connection is closed
     * @throws IOException
     */
    private int receiveClientPackets(int recvMsgSize, byte[] receivedPacket) throws IOException {
        // Receive until client closes connection, indicated by -1 return
        while ((recvMsgSize = inStream.read(receivedPacket)) != -1) {
            //check for op code at beginning of packet
            byte opCode = receivedPacket[0];
            int x, y;
            switch (opCode) {
                case 0: //start game request from client
                    log.debug("code 0");
                    serverGame.startGame();
                    sendServerMovePacketToClient(null);
                    break;
                case 1: //player's move
                    log.debug("code 1");
                    log.debug("inside receiveClientPackets case1");
                    x = receivedPacket[2];
                    y = receivedPacket[3];
                    log.debug("before severGame.updateBoard");
                    serverGame.updateBoard(x, y);
                    createPacketServerMove(receivedPacket);
                    break;
                case 2: //player's last move
                    log.debug("code 2");
                    x = receivedPacket[2];
                    y = receivedPacket[3];
                    serverGame.updateBoard(x, y);
                    //handlePlayerMove(receivedPacket); 
                    isGameOver = true;
                    return 0;
                case 3: //player's request to play again
                    log.debug("code 3"); //player wants to play again
                    isPlayAgain = true;
                    isGameOver = false;
                    break;
                case 4: //player's request not to play again
                    log.debug("code 4");
                    isPlayAgain = false;
                    return -1;
            }
        }
        return recvMsgSize;
    }

    /**
     * Creates the packet containing the server's move coordinates and the
     * appropriate operation code for move packet and the server's points made
     * with its new move.
     *
     * @param receivedPacket byte array packet containing
     * @throws IOException
     */
    private void createPacketServerMove(byte[] receivedPacket) throws IOException {
        byte[] serverMovesPoint = serverGame.determineNextMove(); //[points,x,y]

        //build the server's packet to send the client containing [opcode,points,x,y]
        byte[] serverMovePacket = new byte[BUFSIZE];
        int counter = 0;
        for (int i = 0; i < serverMovePacket.length; i++) {
            if (i == 0) {
                serverMovePacket[i] = 1;//opcode for move packet
            } else {
                serverMovePacket[i] = serverMovesPoint[counter]; 
                counter++;
            }
        }
        sendServerMovePacketToClient(serverMovePacket); //send the packet to client
    }

    /**
     * Sends packet from server to client with the input byte array. If input
     * is null, create a game started message to client with 0 opcode
     * 
     * @param values byte array to send in a packet to client
     * @throws IOException 
     */
    private void sendServerMovePacketToClient(byte[] serverMovePacket) throws IOException {
        //if byte array is null, then create game started message to client with 0 opcode
        if (serverMovePacket == null) {
            serverMovePacket = new byte[BUFSIZE];
            serverMovePacket[0] = 0; //game has started message
        }
        outStream.write(serverMovePacket, 0, serverMovePacket.length); //send packet through outputstream
    }
}
