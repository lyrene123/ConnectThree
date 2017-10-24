/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesClient;
import com.threestones.client.gamestate.ThreeStonesGameBoard;
import com.threestones.client.gamestate.ThreeStonesGameBoard.CellState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
//import javafx.scene.paint.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1411544
 */
public class JPanelBoard {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());
    //the string map array will hold the state of all tiles generated from a CSV file.
    //A tile represents all available squares for the user to play
    //private CellState[][] map = null;
    //reference from the ThreeStonesGame Class to access its pusblic methods
    private ThreeStonesGameBoard map = new ThreeStonesGameBoard();

    //gui is the root Pane that supports the entire UI
    private final JPanel mainView = new JPanel(new BorderLayout(0, 0));

    //boardSquares array holds all tiles with its current state. 
    private JButton[][] cells = new JButton[11][11];

    //the boardGame Pane supports the tiles 
    private JPanel boardGame;

    //the portnumber fields holds the Port Number to connect to the server
    private final JTextField portNumber = new JTextField("50000", 10);

    //the hostNumber holds the IP address to connect to the server
    private final JTextField hostNumber = new JTextField("localhost", 10);

    //Labes
    private final JLabel portLabel = new JLabel(
            "Port");
    private final JLabel hostLabel = new JLabel(
            "Host");

    private JLabel clientScorePoints = new JLabel("0");
    private JLabel serverScoreValue = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent Score");

    ////the connectedLabel displays the actual state of the connection as text. 
    //private JLabel connectedLabel = new JLabel("DISCONNECTED");
    private JButton quitBtn = new JButton("Quit");

    private JButton connectBtn = new JButton("Connect");

    JTextArea textArea = new JTextArea(5, 5);
    private ThreeStonesClient client;

    public JPanelBoard() {
        this.client = new ThreeStonesClient();
        ThreeStonesGameBoard board = new ThreeStonesGameBoard();
        //this.map = parseCSV("");
    }

    public void buildView() {

        // set up the main GUI
        mainView.setBorder(new EmptyBorder(5, 10, 5, 10));
        JPanel settingsPanel = new JPanel();

        settingsPanel.setBackground(Color.decode("#e67e22"));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        mainView.setBackground(Color.decode("#e67e22"));
        //A pane for the main menu bar 
        JToolBar settingsToolbar = new JToolBar();

        settingsToolbar.setFloatable(false);
        //connect is a button that handles the connection
        connectBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        connectBtn.setPreferredSize(new Dimension(100, 30));
        connectBtn.addActionListener(e -> {
            onConnectClick();
        });

        //game.connectButton = connect;
        settingsToolbar.add(connectBtn);
        quitBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        quitBtn.setPreferredSize(new Dimension(100, 30));
//        quitButton.addActionListener(e -> {
//            handleCloseConnection();
//        });
        quitBtn.setEnabled(false);
        //game.quitButton = quitButton;
        settingsToolbar.add(quitBtn);
        JButton play = new JButton("PLAY");
        play.setPreferredSize(new Dimension(100, 30));
        play.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(play);
        JButton resetButton = new JButton("RESET");
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.setFont(new Font("Monospace", Font.BOLD, 15));
        resetButton.setEnabled(false);
//        resetButton.addActionListener(e -> {
//            handleResetButton();
//        });
        //game.resetButton = resetButton;
        settingsToolbar.add(resetButton);
        //toolbar1.addSeparator();
        portLabel.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(portLabel);
        //toolbar1.addSeparator();
        portNumber.setHorizontalAlignment(JTextField.CENTER);
        portNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        portNumber.setMaximumSize(new Dimension(10, 80));
        portNumber.setFont(new Font("Monospace", Font.BOLD, 15));

        settingsToolbar.add(portNumber);
        //toolbar1.addSeparator();
        hostLabel.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostLabel);
        //toolbar1.addSeparator();
        hostNumber.setHorizontalAlignment(JTextField.CENTER);
        hostNumber.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        hostNumber.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostNumber);
        settingsToolbar.setAlignmentX(0);

//            connectedLabel.setMaximumSize(new Dimension(150, 150));
//            connectedLabel.setHorizontalAlignment(JLabel.CENTER);
//            connectedLabel.setFont(new Font("Monospace", Font.BOLD, 15));
//            connectedLabel.setOpaque(true);
//            connectedLabel.setBackground(Color.decode("#e67e22"));
//            connectedLabel.setForeground(Color.red);
        //game.connectedLabel = connectedLabel;
        //toolbar2.add(connectedLabel);
        clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScoreTV.setForeground(Color.WHITE);

        clientScorePoints.setHorizontalAlignment(JTextField.CENTER);
        clientScorePoints.setPreferredSize(new Dimension(150, 40));
        clientScorePoints.setMaximumSize(new Dimension(100, 150));

        clientScorePoints.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScorePoints.setForeground(Color.WHITE);
        //game.whiteScoreLabel = clientScorePoints;

        serverScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScoreTV.setForeground(Color.BLACK);

        serverScoreValue.setHorizontalAlignment(JTextField.CENTER);
        serverScoreValue.setMaximumSize(new Dimension(100, 150));
        serverScoreValue.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScoreValue.setForeground(Color.BLACK);
        //game.blackScoreLabel = serverScorePoints;
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.X_AXIS));
        scoresPanel.setBackground(Color.decode("#e67e22"));
        JToolBar scoresToolbar = new JToolBar();
        scoresToolbar.setFloatable(false);
        scoresToolbar.setBackground(Color.decode("#e67e22"));
        scoresToolbar.add(clientScoreTV);
        scoresToolbar.add(clientScorePoints);
        scoresToolbar.add(serverScoreTV);
        scoresToolbar.add(serverScoreValue);
        scoresToolbar.setAlignmentX(0);
        scoresPanel.add(scoresToolbar);
        scoresPanel.setAlignmentX(0);
        mainView.add(scoresPanel, BorderLayout.PAGE_START);
        //gui.add(toolsBottom, BorderLayout.AFTER_LINE_ENDS);
        textArea.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        textArea.setFont(new Font("Monospace", Font.BOLD, 15));
        //game.textArea = textArea;
        JScrollPane scrollPane = new JScrollPane(textArea);
        mainView.add(scrollPane, BorderLayout.PAGE_END);

        boardGame = new JPanel(new GridLayout(0, 11));
        boardGame.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        //game.board = boardGame;

        mainView.add(boardGame, BorderLayout.CENTER);
        settingsPanel.add(settingsToolbar);
        mainView.add(settingsPanel, BorderLayout.PAGE_END);
        ThreeStonesGameBoard threeStonesGameBoard = new ThreeStonesGameBoard();
        CellState[][] board = threeStonesGameBoard.getBoard();
        // create the board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        JPanel pane = new JPanel();
        JTable table = new JTable(11, 11);
        //pane.setLayout(new GridLayout(11, 11));
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells.length; y++) {


                switch (board[x][y]) {
                    case VACANT:
                        cells[x][y] = new JButton();
                        cells[x][y].setPreferredSize(new Dimension(60, 60));
                        cells[x][y].setBackground(Color.ORANGE);
                        cells[x][y].setEnabled(false);
                        break;
                    case AVAILABLE:
                        cells[x][y] = new JButton();
                        cells[x][y].setPreferredSize(new Dimension(60, 60));
                        cells[x][y].setBackground(Color.WHITE);
                        final int xx = x;
                        final int yy = y;
                        cells[x][y].addActionListener(e -> {
                            client.clickBoardCell(xx, yy);
                        });

                }
//                cells[x][y] = new JButton();
//                cells[x][y].setPreferredSize(new Dimension(60, 60));
//                cells[x][y].setBackground(Color.WHITE);
//                final int xx = x;
//                final int yy = y;
//                cells[x][y].addActionListener(e -> {
//                    clientGame.clickBoardCell(xx, yy);
//                });

//                switch (board[x][y]) {
//                    case : {
//
//                    }
//                }
                //cells[x][y].setText("");
//                cells[x][y] = new JButton();
//
//                cells[x][y].setPreferredSize(new Dimension(60, 60));
//                cells[x][y].setBackground(Color.WHITE);
////                final int xx = x;
////                final int yy = y;
//                cells[x][y].addActionListener(e -> {
//                    clientGame.clickBoardCell(0, 0);
//                });
////                table.setValueAt(cells[x][y], x, y);
//                table.addMouseListener(new MouseAdapter);



                //boardSquares[x][y].set
            }
        }

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                boardGame.add(cells[j][i]);
            }
        }
        //boardGame.setBackground(Color.decode("#e67e22"));

        //}
    }

    public void handleResetButton() {
        //Tile[][] tileList = game.getTileList();
//        if (game.resetGame()) {
//            for (int x = 0; x < boardSquares.length; x++) {
//                for (int y = 0; y < boardSquares.length; y++) {
//
//                    switch (map[y][x]) {
//                        case "w":
//                            break;
//                        case "e":
//                            tileList[x][y].getButton().setEnabled(true);
//                            tileList[x][y].getButton().setIcon(null);
//                            tileList[x][y].setType("space");
//                            tileList[x][y].setAvailable(true);
//                            tileList[x][y].getButton().setBackground(Color.lightGray);
//                            break;
//                    }
//                }
//            }
//            clientScorePoints.setText("0");
//            //serverScorePoints.setText("0");
//        }
//        textArea.setText("GAME RESET! PLAY!");

    }

    public final JComponent getGui() {
        return mainView;
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
                ts.buildView();
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

    private void onConnectClick() {
        log.debug("inside onConnectClick");
        try {
            log.debug("inside onConnectClick before getClientPacket.connectToServer()");
            this.client.getClientPacket().connectToServer();
            log.debug("inside onConnectClick after getClientPacket.connectToServer()");
            this.connectBtn.setEnabled(false);
            this.connectBtn.setBackground(Color.GREEN);
            log.debug("inside onConnectClick end of try catch");
        } catch (IOException ex) {
            Logger.getLogger(JPanelBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
