/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesGameBoard;
import com.threestones.client.gamestate.ThreeStonesGameBoard.CellState;
import java.awt.BorderLayout;
import java.awt.Color;
//import javafx.scene.paint.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author 1411544
 */
public class JPanelBoard {

    //the string map array will hold the state of all tiles generated from a CSV file.
    //A tile represents all available squares for the user to play
    //private CellState[][] map = null;

    //reference from the ThreeStonesGame Class to access its pusblic methods
    private ThreeStonesGameBoard map = new ThreeStonesGameBoard();
    

    //gui is the root Pane that supports the entire UI
    private final JPanel gui = new JPanel(new BorderLayout(0, 0));

    //boardSquares array holds all tiles with its current state. 
    private JButton[][] boardSquares = new JButton[11][11];

    //the boardGame Pane supports the tiles 
    private JPanel boardGame;

    //the portnumber fields holds the Port Number to connect to the server
    private final JTextField portNumber = new JTextField(
            "50000", 10);

    //the hostNumber holds the IP address to connect to the server
    private final JTextField hostNumber = new JTextField(
            "localhost", 10);

    //Labes
    private final JLabel portLabel = new JLabel(
            "Port");
    private final JLabel hostLabel = new JLabel(
            "Host");

    private JLabel clientScorePoints = new JLabel(
            "0");
    private JLabel serverScoreValue = new JLabel(
            "0");
    private final JLabel clientScoreTV = new JLabel(
            "Your Score");
    private final JLabel serverScoreTV = new JLabel(
            "Opponent Score");

    ////the connectedLabel displays the actual state of the connection as text. 
    //private JLabel connectedLabel = new JLabel("DISCONNECTED");
    private JButton quitBtn = new JButton("Quit");

    private JButton connectBtn = new JButton("Connect");

    JTextArea textArea = new JTextArea(5, 5);

    /**
     * The non-parameter Constructor retrieves the state of the game from a CSV
     * file which contains the position of all tiles and stores it in an array
     * called map.
     */
    public JPanelBoard() {
        
            ThreeStonesGameBoard board = new ThreeStonesGameBoard();
            //this.map = parseCSV("");
        
    }

    /**
     * The initializeGui method set up the interface of the ThreeStones Game.
     */
    public void initializeGui() {

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 10, 5, 10));
        JPanel panel = new JPanel();
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.decode("#e67e22"));
        panel.setBackground(Color.decode("#e67e22"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        gui.setBackground(Color.decode("#e67e22"));
        //A pane for the main menu bar 
        JToolBar toolbar1 = new JToolBar();
        
        toolbar1.setFloatable(false);
        //connect is a button that handles the connection
        connectBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        connectBtn.setPreferredSize(new Dimension(100, 30));
        //connect.addActionListener(e -> {
//            try {
//                handleConnectButton();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });

            //game.connectButton = connect;
            toolbar1.add(connectBtn);
            toolbar1.addSeparator();
            quitBtn.setFont(new Font("Monospace", Font.BOLD, 15));
            quitBtn.setPreferredSize(new Dimension(100, 30));
//        quitButton.addActionListener(e -> {
//            handleCloseConnection();
//        });
            quitBtn.setEnabled(false);
            //game.quitButton = quitButton;
            toolbar1.add(quitBtn);
            toolbar1.addSeparator();
            JButton play = new JButton("PLAY");
            play.setPreferredSize(new Dimension(100, 30));
            play.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar1.add(play);
            JButton resetButton = new JButton("RESET");
            resetButton.setPreferredSize(new Dimension(100, 30));
            resetButton.setFont(new Font("Monospace", Font.BOLD, 15));
            resetButton.setEnabled(false);
//        resetButton.addActionListener(e -> {
//            handleResetButton();
//        });
            //game.resetButton = resetButton;
            toolbar1.add(resetButton);
            toolbar1.addSeparator();
            portLabel.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar1.add(portLabel);
            toolbar1.addSeparator();
            portNumber.setHorizontalAlignment(JTextField.CENTER);

            portNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 2));
            portNumber.setMaximumSize(new Dimension(10, 80));
            portNumber.setFont(new Font("Monospace", Font.BOLD, 15));

            toolbar1.add(portNumber);
            toolbar1.addSeparator();
            hostLabel.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar1.add(hostLabel);
            toolbar1.addSeparator();
            hostNumber.setHorizontalAlignment(JTextField.CENTER);
            hostNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 2));
            hostNumber.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar1.add(hostNumber);
            toolbar1.setAlignmentX(0);

            JToolBar toolbar2 = new JToolBar();
            toolbar2.setFloatable(false);
            //connectedLabel.setMaximumSize(new Dimension(150, 150));
            //connectedLabel.setHorizontalAlignment(JLabel.CENTER);
            //connectedLabel.setFont(new Font("Monospace", Font.BOLD, 15));
            //connectedLabel.setOpaque(true);
            //connectedLabel.setBackground(Color.decode("#e67e22"));
            //connectedLabel.setForeground(Color.red);
            //game.connectedLabel = connectedLabel;
            //toolbar2.add(connectedLabel);
            clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar2.add(clientScoreTV);
            toolbar2.addSeparator();
            clientScorePoints.setHorizontalAlignment(JTextField.CENTER);
            clientScorePoints.setPreferredSize(new Dimension(150, 40));
            clientScorePoints.setMaximumSize(new Dimension(100, 150));
            clientScorePoints.setFont(new Font("Monospace", Font.BOLD, 15));
            //game.whiteScoreLabel = clientScorePoints;
            toolbar2.add(clientScorePoints);
            toolbar2.addSeparator();
            serverScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
            toolbar2.add(serverScoreTV);
            toolbar2.addSeparator();
            serverScoreValue.setHorizontalAlignment(JTextField.CENTER);
            serverScoreValue.setMaximumSize(new Dimension(100, 150));
            serverScoreValue.setFont(new Font("Monospace", Font.BOLD, 15));
            //game.blackScoreLabel = serverScorePoints;
            toolbar2.add(serverScoreValue);
            toolbar2.setAlignmentX(0);
            //gui.add(toolsBottom, BorderLayout.AFTER_LINE_ENDS);

            
            textArea.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
            textArea.setFont(new Font("Monospace", Font.BOLD, 15));
            //game.textArea = textArea;
            JScrollPane scrollPane = new JScrollPane(textArea);
            gui.add(scrollPane, BorderLayout.PAGE_END);

            boardGame = new JPanel(new GridLayout(0, 11));
            boardGame.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
            //game.board = boardGame;
            topPanel.add(toolbar2);
            topPanel.setAlignmentX(0);
            gui.add(topPanel,BorderLayout.PAGE_START);
            gui.add(boardGame,BorderLayout.CENTER );
            panel.add(toolbar1);
            gui.add(panel,BorderLayout.PAGE_END);


            // create the board squares
            Insets buttonMargin = new Insets(0, 0, 0, 0);
            for (int x = 0; x < boardSquares.length; x++) {
                for (int y = 0; y < boardSquares.length; y++) {

                    boardSquares[x][y] = new JButton();
                    boardSquares[x][y].setPreferredSize(new Dimension(60,60));
                    boardSquares[x][y].setBackground(Color.WHITE);
                    //boardSquares[x][y].set
                }
            }
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    boardGame.add(boardSquares[j][i]);
                }
            }
            //boardGame.setBackground(Color.decode("#e67e22"));
            
            
        
            //}

            /*public void handleResetButton() {
        Tile[][] tileList = game.getTileList();
        if (game.resetGame()) {
            for (int x = 0; x < boardSquares.length; x++) {
                for (int y = 0; y < boardSquares.length; y++) {

                    switch (map[y][x]) {
                        case "w":
                            break;
                        case "e":
                            tileList[x][y].getButton().setEnabled(true);
                            tileList[x][y].getButton().setIcon(null);
                            tileList[x][y].setType("space");
                            tileList[x][y].setAvailable(true);
                            tileList[x][y].getButton().setBackground(Color.lightGray);
                            break;
                    }
                }
            }
            clientScorePoints.setText("0");
            serverScorePoints.setText("0");
        }
        textArea.setText("GAME RESET! PLAY!");

    }
             */
        
        }
    public final JComponent getGui() {
        return gui;
    }

    public JComponent getTextArea() {
        return textArea;
    }

    public String getPortNumber() {
        return portNumber.getText();
    }

    public String getIp() {
        return hostNumber.getText();
    }

    /**
     * The Parse method reads from a CSV file and creates a 2D array map of the
     * board.
     *
     * @param content
     * @return
     * @throws IOException
     */
   

    public static void main(String[] args) throws IOException {
        Runnable r;
        r = new Runnable() {
            JPanelBoard ts = new JPanelBoard();

            @Override
            public void run() {
                ts.initializeGui();
                JFrame gameBoard = new JFrame("3 Stones");
                gameBoard.add(ts.getGui());
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
