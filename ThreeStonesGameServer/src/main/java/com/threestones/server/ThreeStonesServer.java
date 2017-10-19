package com.threestones.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior of a server in a Three Stones game and accepts
 * any client who wants to play the game.
 * 
 * @author ehugh
 */
public class ThreeStonesServer {

<<<<<<< HEAD
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ThreeStonesServer() {
    }

//    public void runServer() throws IOException {
//        int servPort = 50000;
//        // Create a server socket to accept client connection requests
//        ServerSocket servSock = new ServerSocket(servPort);
//        for (;;) {
//            Socket clientSocket = servSock.accept();
//            ThreeStonesServerSession serverSession = new ThreeStonesServerSession();
//            serverSession.playSession(clientSocket);
//        }
//    }
=======
    /**
     * Default constructor.
     */
    public ThreeStonesServer() { }

    /**
     * Starts the server and continuously accepts the connection of any client
     * and creates a ThreeStonesServerSession instance when a connection is made
     * 
     * @throws IOException 
     */
>>>>>>> 1fb53ca81116ce3cd05dfaea91b8e2b6b7f40fe6
    public void runServer() throws IOException {
        int servPort = 50000;
        log.debug("Server Start");
        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);
        log.debug("Server Created Socket");
        int recvMsgSize;						// Size of received message
        byte[] byteBuffer = new byte[10];	// Receive buffer

        // Run forever, accepting and servicing connections
        log.debug("Server Start Listening");
        while (true) {
            log.debug("Loop");
            Socket clntSock = servSock.accept();	// Get client connection

            log.debug("Handling client at "
                    + clntSock.getInetAddress().getHostAddress() + " on port "
                    + clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, recvMsgSize);
            }

            clntSock.close();
        }// Close the socket. This client is finished.
    }
}
