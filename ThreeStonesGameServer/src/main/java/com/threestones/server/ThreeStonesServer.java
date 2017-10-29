package com.threestones.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior of a server in a Three Stones game and accepts any
 * client who wants to play the game.
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesServer {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Default constructor.
     */
    public ThreeStonesServer() {
    }

    /**
     * Starts the server and continuously accepts the connection of any client
     * and creates a ThreeStonesServerSession instance when a connection is made
     *
     * @throws IOException
     */
    public void runServer() throws IOException {
        int servPort = 50000;
        log.debug("Server Start");

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        // Run forever, accepting and servicing connections
        while (true) {
            log.debug("Server is listening at: " + servSock.getLocalSocketAddress().toString());
            Socket clntSock = servSock.accept();// Get client connection

            //create session once connection is made
            ThreeStonesServerSession session = new ThreeStonesServerSession();
            session.playGameSession(clntSock); //pass client socket to the session
        }
    }
}
