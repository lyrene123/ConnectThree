/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.app;

import com.threestones.view.ThreeStonesGUI;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author 1412844
 */
public class ThreeStonesClientApp {
    public static void main(String[] args) throws IOException {
        Runnable r;
        r = new Runnable() {
            ThreeStonesGUI ts = new ThreeStonesGUI();

            @Override
            public void run() {
                ts.buildView();
                JFrame gameBoard = new JFrame("3 Stones");
                gameBoard.add(ts.getMainView());
                gameBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gameBoard.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                gameBoard.pack();
                // ensures the minimum size is enforced.
                gameBoard.setMinimumSize(gameBoard.getSize());
                gameBoard.setVisible(true);

            }
        };
        SwingUtilities.invokeLater(r);
    }
}
