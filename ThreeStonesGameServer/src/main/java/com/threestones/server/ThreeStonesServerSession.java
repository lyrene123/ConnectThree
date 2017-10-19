
package com.threestones.server;

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
    
    public ThreeStonesServerSession(){
        this.gameOver = false;
        this.playAgain = true;
    }

    public void playSession(Socket clientSock) throws IOException {
        /*
      loop 
                playAgain is a boolean its a private field of session
                loop on !gameOver
                    //ServerGame.playGame()
         */

        int recvMsgSize = 0;
        byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer
        InputStream in = clientSock.getInputStream();
        OutputStream out = clientSock.getOutputStream();
        
        while (playAgain) {
            while (!gameOver) {

                // Receive until client closes connection, indicated by -1 return
                while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                    //receive client's packet here and check content

                    /*
                        content :
                        1) start game message -> startGame() which creates board and initializes everything
                        2) move -> AI's game logic methods, use OutputStream to send back AI's move
                     */
                    //check for any game over. If game over, break this loop and assign gameOver to true
                }

                //if game over or connection is closed
                if(gameOver){
                    
                }
                    
            }
            if (gameOver && recvMsgSize != -1) {
                //ask to play again
            }else{
                playAgain = false;
            }
        }
        clientSock.close();
    }
}
