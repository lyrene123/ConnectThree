package com.threestones.server.app;

import com.threestones.server.ThreeStonesServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Application of the server side of the game Three Stones. Creates a
 * ThreeStonesServer instance and runs the server to start servicing clients.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesServerApp {

    /**
     * Instantiates a ThreeStonesServer instance to start running the server to
     * accept clients to connect to the three stones game.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        ThreeStonesServer server = new ThreeStonesServer();

        try {
            server.runServer();
        } catch (IOException ex) {
            Logger.getLogger(ThreeStonesServerApp.class.getName()).log(Level.SEVERE, "Cannot run Server", ex);
        }

    }
}
