package com.threestones.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ehugh
 */
// TODO merge packet class into server to be able to receive and send requests
public class ThreeStonesServer {

    public ThreeStonesServer() { }

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
