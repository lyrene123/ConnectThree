package com.threestones.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the behavior of a server in a Three Stones game server side
 * application that accepts to service any client who wants to connect and play
 * the game from the client side of the three stones game application. Each
 * client will have its own session and each session will be executed in a
 * separate thread.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesServer {

    //for logging information to the console
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Default constructor.
     */
    public ThreeStonesServer() {
    }

    /**
     * Starts the server by creating a server socket and continuously accepts
     * the connection of any client. Once a connection is made, a
     * ThreeStonesServerSession is created passing it the client's socket and
     * this is done in a separate thread.
     *
     * @throws IOException
     */
    public void runServer() throws IOException {
        int servPort = 50000;
        log.debug("Server Started");

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);
        InetAddress servAddress = InetAddress.getLocalHost();

        // Run forever, accepting and servicing connections
        while (true) {
            log.info("Server is listening at: " + servAddress.getHostAddress() + " at port " + servSock.getLocalPort());
            Socket clntSock = servSock.accept();// wait for client connection
            log.info("Server is accepting client at: " + clntSock.getLocalSocketAddress().toString());

            //create session once connection is made in a separate thread
            new Thread(new ThreeStonesServerSession(clntSock)).start();

        }
    }
}
