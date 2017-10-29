package com.threestones.client.app;

import com.threestones.view.ThreeStonesGUI;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * Main application for the client side in a game of Three Stones. This is the
 * main application that would be executed in order to display the UI of the
 * game to the user and enable the user to connect to the server side
 * application of the Three Stones and start playing against the server.
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesClientApp {

    /**
     * Creates a JFrame window for the display of the game board of the Three
     * Stones game, the counters for the white and black points and the counters
     * for the white and black stones left to play. The UI will then let the the
     * connect to the server and start the game.
     *
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Runnable r;
        r = new Runnable() {
            ThreeStonesGUI ts = new ThreeStonesGUI();

            @Override
            public void run() {
                ts.buildThreeStonesUI();
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
