package com.threestones.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ehugh
 */
// TODO merge packet class into server to be able to receive and send requests
public class ThreeStonesServer {

    private ThreeStonesServerSession session;
    private static final int BUFSIZE = 32;	// Size of receive buffer

    public ThreeStonesServer() {
        this.session = new ThreeStonesServerSession();
    }

    public void runServer() throws IOException {

        int servPort = 50000;
        
        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);
        for (;;) {
        Socket clientSocket = servSock.accept();
        this.session.playSession(clientSocket);// Close the socket. This client is finished.
        }
    }
    /* NOT REACHED */
 /*
            s is a socket 
        then call s.accept() receives any inbound connections
        
        
        instantiate a new session and passes the socket 
        server.playSession();
        all of this is in a loop    
     */

}

}
