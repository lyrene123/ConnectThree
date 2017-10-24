package com.threestones.server;

import com.threestones.server.gamestate.ThreeStonesServerGame;
import java.io.*;   // for IOException and Input/OutputStream
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ehugh
 */
public class ThreeStonesServerSession {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final int BUFSIZE = 4;	
    private boolean gameOver;
    private boolean playAgain;
    private ThreeStonesServerGame serverGame;
    private InputStream in;
    private OutputStream out;

    public ThreeStonesServerSession() {
        this.gameOver = false;
        this.playAgain = true;
        this.serverGame = new ThreeStonesServerGame();
    }

    public void playSession(Socket clientSock) throws IOException {
        int recvMsgSize = 0;
        byte[] receivedPacket = new byte[BUFSIZE];	
        in = clientSock.getInputStream();
        out = clientSock.getOutputStream();
        
        while (playAgain) {
            while (!gameOver) {
                recvMsgSize = receiveClientPackets(recvMsgSize, receivedPacket);
                //if game over or connection is closed
                if (gameOver || recvMsgSize == -1) {
                    if (recvMsgSize == -1) {
                        playAgain = false;
                    }
                    break;
                }
            }
            if (gameOver && recvMsgSize != -1) {
                //ask to play again with OutputStream
                byte[] outPacket = new byte[1];
                outPacket[0] = 3; //play again message
                out.write(outPacket);
            }
        }
        clientSock.close();
    }

    private int receiveClientPackets(int recvMsgSize, byte[] receivedPacket) throws IOException {
        // Receive until client closes connection, indicated by -1 return
        while ((recvMsgSize = in.read(receivedPacket)) != -1) {
            //check for op code at beginning of packet
            byte opCode = receivedPacket[0];
            switch (opCode) {
                case 0:
                    log.debug("code 0");
                    serverGame.startGame();
                    sendServerMovePacketToClient(null, out);
                    break; //start game message
                case 1:
                    //handlePlayerMove(receivedPacket, out);
                    break; //player's move
                case 2:
                    log.debug("code 1");
                    //handlePlayerMove(receivedPacket, out); //player's last move
                    gameOver = true;
                    return 0;
                case 3:
                    log.debug("code 2");
                    playAgain = true;
                    gameOver = false;
                    break;
            }
        }
        return recvMsgSize;
    }

    private void handlePlayerMove(byte[] receivedPacket, OutputStream out) throws IOException {
        int x = receivedPacket[1];
        int y = receivedPacket[2];
        serverGame.updateBoard(x, y);

       byte[] serverMovesPoint = serverGame.determineNextMove(); //[points,x,y]
        
        byte[] packetValues = new byte[4];
        int counter = 0;
        for (int i = 0; i < packetValues.length; i++) {
            if (i == 0) {
                packetValues[i] = 1;//a move packet
            } else {
                packetValues[i] = serverMovesPoint[counter]; //[1,points,x,y]
                counter++;
            }
        }
        sendServerMovePacketToClient(packetValues, out);
    }

    private void sendServerMovePacketToClient(byte[] values, OutputStream out) throws IOException {
        //outstream to sent back server move to client
        if (values == null) {
            values = new byte[1];
            values[0] = 0; //game has started message
        } 
        out.write(values, 0, values.length);
    }
}
