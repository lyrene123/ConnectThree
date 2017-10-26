
package com.threestones.client.app;

import com.threestones.view.ThreeStonesGUI;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main application for the client side in a game of Three Stones. This is the
 * main application that would be executed in order to display the UI of the 
 * game to the user and enable the user to connect to the server side application
 * of the Three Stones and start playing against the server.
 * 
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesClientApp {
    /**
     * Creates a JFrame window for the gameboard of the Three Stones game 
     * and displays it to the user in order to connect to the server and
     * start the game.
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
               /* JFrame threeStonesBoardUI = new JFrame("Three Stones Game");
                threeStonesBoardUI.add(ts.getMainView());
                threeStonesBoardUI.setLocationByPlatform(true);
                threeStonesBoardUI.pack(); //set the window to preferred size
                threeStonesBoardUI.setMinimumSize(threeStonesBoardUI.getSize());
                threeStonesBoardUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                threeStonesBoardUI.setVisible(true);*/
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
