package com.threestones.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Encapsulates the behavior of a server in a Three Stones game and accepts
 * any client who wants to play the game.
 * 
 * @author ehugh
 */
public class ThreeStonesServer {

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
    public void runServer() throws IOException {
        int servPort = 50000;
        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);
        for (;;) {
            Socket clientSocket = servSock.accept();
            ThreeStonesServerSession serverSession = new ThreeStonesServerSession();
            serverSession.playSession(clientSocket);
        }
    }
}
