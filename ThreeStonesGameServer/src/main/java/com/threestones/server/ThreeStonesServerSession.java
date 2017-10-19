/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.server;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
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

    public void playSession(Socket clientSock) throws IOException {
        /*
      loop 
                playAgain is a boolean its a private field of session
                loop on !gameOver
                    //ServerGame.playGame()
         */

        while (playAgain) {
            while (!gameOver) {
                int recvMsgSize;						// Size of received message
                byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer

                // Run forever, accepting and servicing connections
                System.out.println("Handling client at "
                        + clientSock.getInetAddress().getHostAddress() + " on port "
                        + clientSock.getPort());

                InputStream in = clientSock.getInputStream();
                OutputStream out = clientSock.getOutputStream();

                // Receive until client closes connection, indicated by -1 return
                while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                    out.write(byteBuffer, 0, recvMsgSize);
                }

                clientSock.close();

            }
        }

    }
}

