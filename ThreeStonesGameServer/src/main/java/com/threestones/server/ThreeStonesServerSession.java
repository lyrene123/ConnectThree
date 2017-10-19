
package com.threestones.server;

import com.threestones.server.gamestate.ThreeStonesServerGame;
import java.io.*;   // for IOException and Input/OutputStream
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author ehugh
 */
public class ThreeStonesServerSession {

    private static final int BUFSIZE = 32;	// Size of receive buffer
    private boolean gameOver;
    private boolean playAgain;
    private ThreeStonesServerGame serverGame;

    public ThreeStonesServerSession() {
        this.gameOver = false;
        this.playAgain = true;
        this.serverGame = new ThreeStonesServerGame();
    }

    public void playSession(Socket clientSock) throws IOException {
        int recvMsgSize = 0;
        byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer
        InputStream in = clientSock.getInputStream();
        OutputStream out = clientSock.getOutputStream();
        while (playAgain) {
            while (!gameOver) {
                recvMsgSize = receiveClientPackets(in, out, recvMsgSize, byteBuffer);          
                //if game over or connection is closed
                if (gameOver || recvMsgSize == -1) {
                    if (recvMsgSize == -1) playAgain = false;
                    break;
                }
            }
            if (gameOver && recvMsgSize != -1) {
                //ask to play again with OutputStream
            }
        }
        clientSock.close();
    }

    private int receiveClientPackets(InputStream in, OutputStream out, int recvMsgSize, byte[] byteBuffer) throws IOException {
        // Receive until client closes connection, indicated by -1 return
        while ((recvMsgSize = in.read(byteBuffer)) != -1) {
            //check for op code at beginning of packet
            byte opCode = byteBuffer[0];
            switch(opCode){
                case 0: serverGame.drawBoard(); break; //start game message
                case 1: handlePlayerMove(byteBuffer); break; //player's move
                case 2: handlePlayerMove(byteBuffer); //player's last move
                        gameOver = true;
                        return 0;
            }
        }
        return recvMsgSize;
    }
    
    private void handlePlayerMove(byte[] byteBuffer){
        int x = byteBuffer[1];
        int y = byteBuffer[2];
        serverGame.updateBoard(x, y);
        serverGame.determineNextMove();
        
        //outstream to sent back move to client
    }
}
