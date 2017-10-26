/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesClient;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;

/* TopLevelDemo.java requires no other files. */
public class SecondGUI {

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private JLabel clientScorePoints = new JLabel("0");
    private JLabel serverScorePoints = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent Score");
    private final JPanel mainView = new JPanel(new BorderLayout(0, 0));
    private JPanel boardGame;

    private ThreeStonesClient client;
    private ThreeStonesClientGameBoard clientGameBoard;

    private JButton[][] cells = new JButton[11][11];

    // settings 
    private JPanel settingsPanel = new JPanel();
    private final JTextField portNumber = new JTextField("50000", 10);
    private final JTextField hostNumber = new JTextField("localhost", 10);
    private final JLabel portLabel = new JLabel("Port");
    private final JLabel hostLabel = new JLabel("Host");
    private JButton quitBtn = new JButton("Quit");
    private JButton connectBtn = new JButton("Connect");

    public SecondGUI() {
        this.client = new ThreeStonesClient();
        this.clientGameBoard = client.getBoard();
    }

    public void buildView() {
        //Create and set up the window.
        JFrame frame = new JFrame("ThreeStones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create the menu bar.  Make it have a green background.
        JMenuBar greenMenuBar = new JMenuBar();
        greenMenuBar.setOpaque(true);
        greenMenuBar.setBackground(new Color(154, 165, 127));
        greenMenuBar.setPreferredSize(new Dimension(200, 50));

        JPanel clientScorePanel = new JPanel();
        //clientScorePanel.setPreferredSize(new Dimension(50, 50));
        JPanel serverScorePanel = new JPanel();

        //clientScoreTV.setPreferredSize(new Dimension(20, 20));
        clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));

        //clientScorePoints.setPreferredSize(new Dimension(50, 50));
        clientScorePoints.setFont(new Font("Monospace", Font.BOLD, 15));

        clientScorePanel.add(clientScoreTV);
        clientScorePanel.add(clientScorePoints);

        serverScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScoreTV.setPreferredSize(new Dimension(50, 50));

        serverScorePoints.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScorePoints.setPreferredSize(new Dimension(50, 50));

        //greenMenuBar.add(clientScoreTV);
        greenMenuBar.add(clientScorePanel);
        greenMenuBar.add(serverScorePanel);
        //greenMenuBar.add(serverScorePoints);

        //Create a yellow label to put in the content pane.
        JLabel yellowLabel = new JLabel();
        yellowLabel.setOpaque(true);
        yellowLabel.setBackground(new Color(248, 213, 131));

        yellowLabel.setPreferredSize(new Dimension(200, 180));
        frame.setPreferredSize(new Dimension(1000, 800));
        //Set the menu bar and add the label to the content pane.

        boardGame = new JPanel(new GridLayout(0, 11));
        //boardGame.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        //game.board = boardGame;

        mainView.add(boardGame, BorderLayout.CENTER);
        //settingsPanel.add(settingsToolbar);
        //mainView.add(settingsPanel, BorderLayout.PAGE_END);

        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells.length; y++) {

                switch (client.getBoard().getBoard()[x][y]) {
                    case VACANT:
                        cells[x][y] = new JButton();
                        cells[x][y].setPreferredSize(new Dimension(60, 60));
                        cells[x][y].setBackground(Color.ORANGE);

                        break;
                    case AVAILABLE:
                        cells[x][y] = new JButton();
                        cells[x][y].setPreferredSize(new Dimension(60, 60));
                        cells[x][y].setBackground(Color.WHITE);
                        final int xx = x;
                        final int yy = y;
                        cells[x][y].addActionListener(e -> {
                            try {
                                client.clickBoardCell(xx, yy);
                            } catch (IOException ex) {
                                Logger.getLogger(ThreeStonesGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                }
                cells[x][y].setEnabled(false);

            }
        }

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                boardGame.add(cells[j][i]);
            }
        }

        JTextArea textArea = new JTextArea(1, 1);
        textArea.setLineWrap(true);
        textArea.setPreferredSize(new Dimension(200, frame.getHeight()));
        frame.getContentPane().add(textArea, BorderLayout.EAST);

        settingsPanel.setBackground(Color.decode("#e67e22"));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        JToolBar settingsToolbar = new JToolBar();
        settingsToolbar.setFloatable(false);
        connectBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        connectBtn.setPreferredSize(new Dimension(100, 30));
        connectBtn.addActionListener(e -> {
            onConnectClick();
        });
        settingsToolbar.add(connectBtn);
        quitBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        quitBtn.setPreferredSize(new Dimension(100, 30));
        quitBtn.addActionListener(e -> {
            onCloseConnectionClick();
        });
        quitBtn.setEnabled(false);
        settingsToolbar.add(quitBtn);
        JButton play = new JButton("PLAY");
        play.setPreferredSize(new Dimension(100, 30));
        play.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(play);
        JButton resetButton = new JButton("RESET");
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.setFont(new Font("Monospace", Font.BOLD, 15));
        resetButton.setEnabled(false);
        resetButton.addActionListener(e -> {
            onResetClick();
        });
        settingsToolbar.add(resetButton);
        portLabel.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(portLabel);
        portNumber.setHorizontalAlignment(JTextField.CENTER);
        portNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        portNumber.setMaximumSize(new Dimension(10, 80));
        portNumber.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(portNumber);
        hostLabel.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostLabel);
        hostNumber.setHorizontalAlignment(JTextField.CENTER);
        hostNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        hostNumber.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostNumber);
        settingsToolbar.setAlignmentX(0);

        settingsPanel.add(settingsToolbar);
        frame.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(greenMenuBar);
        frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
        frame.add(mainView);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public final JComponent getMainView() {
        return mainView;
    }

    private void onResetClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void onCloseConnectionClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void onConnectClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
