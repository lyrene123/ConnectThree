
package com.threestones.server.app;

import com.threestones.server.ThreeStonesServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Application of the server side in a game of Three Stones. Creates a 
 * Three Stones server instance and runs the server to start servicing clients.
 * 
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesServerApp {
    public static void main(String[] args) {
        ThreeStonesServer server = new ThreeStonesServer();
        
        try {
            server.runServer();
        } catch (IOException ex) {
            Logger.getLogger(ThreeStonesServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
