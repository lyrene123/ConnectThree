package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesClient;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the Three Stones Game Graphical User Interface with buttons,
 * textfields, textareas, and labels in order to display to the user the game
 * board, the total points of black and white stones, the total black and white
 * stones left, and the host and IP of the computer the user is playing against.
 *
 * @author Eric
 * @author Lyrene
 * @author Jacob
 */
public class ThreeStonesGUI {

    //for logging
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    //the parent container of the whole window UI
    private final JPanel mainRootPane = new JPanel(new BorderLayout(0, 0));

    //text fields
    private final JLabel portLbl = new JLabel("Port");
    private final JLabel hostLbl = new JLabel("Host");
    private final JLabel clientScorePnts = new JLabel("0");
    private final JLabel serverScorePnts = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent's Score");

    //labels
    private final JTextField portNum = new JTextField("50000", 10);
    private final JTextField hostNum = new JTextField("localhost", 10);

    //buttons
    private final JButton quitBtn = new JButton("Quit Game");
    private final JButton connectBtn = new JButton("Connect");
    private final JButton playAgainButton = new JButton("Play Again");

    //the cells representing the slots in the three stones game board
    private final JButton[][] gameBoardCells = new JButton[11][11];

    JTextArea textArea = new JTextArea(5, 5);
    private final ThreeStonesClient threeStonesClnt;
    private final ThreeStonesClientGameBoard clientGameBoard;
    private JPanel threeStonesGameBoard;
    private JFrame frame;

    /**
     * Default constructor that initializes a ThreeStonesClient instance
     */
    public ThreeStonesGUI() {
        this.threeStonesClnt = new ThreeStonesClient();
        this.clientGameBoard = this.threeStonesClnt.getBoard();
    }

    /**
     * Builds the User Interface of the Three Stones game to display to the
     * user containing the board game, the points, the number of stones and 
     * other info such as the ip address of the server
     */
    public void buildThreeStonesUI() {
        //Create and set up the window.
        frame = new JFrame("ThreeStones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //build the scores panel
        JPanel scoresPanel = new JPanel();
        buildScoresPanel(scoresPanel);

        //build the client score text view
        clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScoreTV.setForeground(Color.WHITE);

        buildClientScoreLbl();

        //build the server score text view
        serverScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScoreTV.setForeground(Color.BLACK);

        buildServerScoreLbl();

        //build threeStonesGameBoard instance and add it to the main layout
        threeStonesGameBoard = new JPanel(new GridLayout(0, 11));
        threeStonesGameBoard.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        mainRootPane.add(threeStonesGameBoard, BorderLayout.CENTER);

        buildGameBoard();
        buildTextArea();

        //build the panel containing the settings information
        JPanel settingsPanel = new JPanel();
        buildSettingsPanel(settingsPanel);

        //build the toolbar containing the different settings and buttons
        JToolBar settingsToolbar = new JToolBar();
        settingsToolbar.setFloatable(false);
        buildConnectBtn(settingsToolbar); //build and add connect button to toolbar
        buildQuitBtn(settingsToolbar); //build and add quit button to toolbar
        buildPlayAgainBtn(settingsToolbar); //build and add play again button to toolbar

        portLbl.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(portLbl); //add port label to toolbar

        buildPortNumField(settingsToolbar); //build and port num text field to toolbar

        hostLbl.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostLbl); //add host label to toolbar

        buildHostNumField(settingsToolbar); //build and host num text field to toolbar

        //add the settings tool bar to the settins panel and add the panel to main layout
        settingsPanel.add(settingsToolbar);

        frame.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
        frame.add(mainRootPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void buildGameBoard() {
        // create the board squares
        for (int x = 0; x < gameBoardCells.length; x++) {
            for (int y = 0; y < gameBoardCells.length; y++) {
                switch (clientGameBoard.getBoard()[x][y]) {
                    case VACANT:
                        gameBoardCells[x][y] = new JButton();
                        gameBoardCells[x][y].setPreferredSize(new Dimension(60, 60));
                        gameBoardCells[x][y].setBackground(Color.ORANGE);
                        break;
                    case AVAILABLE:
                        gameBoardCells[x][y] = new JButton();
                        gameBoardCells[x][y].setPreferredSize(new Dimension(60, 60));
                        gameBoardCells[x][y].setBackground(Color.WHITE);
                        final int xx = x;
                        final int yy = y;
                        gameBoardCells[x][y].addActionListener(e -> {
                            try {
                                threeStonesClnt.clickBoardCell(xx, yy);
                            } catch (IOException ex) {
                                Logger.getLogger(ThreeStonesGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                }
                gameBoardCells[x][y].setEnabled(false);
            }
        }

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                threeStonesGameBoard.add(gameBoardCells[j][i]);
            }
        }
    }

    private void buildTextArea() {
        textArea = new JTextArea(1, 1);
        textArea.setLineWrap(true);
        textArea.setPreferredSize(new Dimension(200, frame.getHeight()));
        frame.getContentPane().add(textArea, BorderLayout.EAST);
    }

    private void buildScoresPanel(JPanel scoresPanel) {
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.X_AXIS));
        scoresPanel.setBackground(Color.decode("#e67e22"));

        //build and add the scores tool bar in the scores panel
        JToolBar scoresToolbar = new JToolBar();
        buildScoresToolBar(scoresToolbar);
        scoresPanel.add(scoresToolbar);
        scoresPanel.setAlignmentX(0);

        //add the scores panel in to the main panel
        mainRootPane.add(scoresPanel, BorderLayout.PAGE_START);
    }

    private void buildScoresToolBar(JToolBar scoresToolbar) {
        scoresToolbar.setFloatable(false);
        scoresToolbar.setBackground(Color.decode("#e67e22"));
        scoresToolbar.add(clientScoreTV);
        scoresToolbar.add(clientScorePnts);
        scoresToolbar.add(serverScoreTV);
        scoresToolbar.add(serverScorePnts);
        scoresToolbar.setAlignmentX(0);
    }

    private void buildServerScoreLbl() {
        serverScorePnts.setHorizontalAlignment(JTextField.CENTER);
        serverScorePnts.setMaximumSize(new Dimension(100, 150));
        serverScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScorePnts.setForeground(Color.BLACK);
    }

    private void buildClientScoreLbl() {
        clientScorePnts.setHorizontalAlignment(JTextField.CENTER);
        clientScorePnts.setPreferredSize(new Dimension(150, 40));
        clientScorePnts.setMaximumSize(new Dimension(100, 150));
        clientScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScorePnts.setForeground(Color.WHITE);
    }

    private void buildHostNumField(JToolBar settingsToolbar) {
        hostNum.setHorizontalAlignment(JTextField.CENTER);
        hostNum.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        hostNum.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostNum);
        settingsToolbar.setAlignmentX(0);
    }

    private void buildPortNumField(JToolBar settingsToolbar) {
        portNum.setHorizontalAlignment(JTextField.CENTER);
        portNum.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        portNum.setMaximumSize(new Dimension(10, 80));
        portNum.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(portNum);
    }

    private void buildSettingsPanel(JPanel settingsPanel) {
        settingsPanel.setBackground(Color.decode("#e67e22"));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        mainRootPane.setBackground(Color.decode("#e67e22"));
    }

    private void buildConnectBtn(JToolBar settingsToolbar) {
        connectBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        connectBtn.setPreferredSize(new Dimension(100, 30));
        connectBtn.addActionListener(e -> {
            onConnectClick();
        });
        settingsToolbar.add(connectBtn);
    }

    private void buildQuitBtn(JToolBar settingsToolbar) {
        quitBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        quitBtn.setPreferredSize(new Dimension(100, 30));
        quitBtn.addActionListener(e -> {
            onCloseConnectionClick();
        });
        quitBtn.setEnabled(false);
        settingsToolbar.add(quitBtn);
    }

    private void buildPlayAgainBtn(JToolBar settingToolBar) {
        playAgainButton.setPreferredSize(new Dimension(100, 30));
        playAgainButton.setFont(new Font("Monospace", Font.BOLD, 15));
        playAgainButton.addActionListener(e -> {
            onPlayAgainClick();
        });
        playAgainButton.setEnabled(false);
        settingToolBar.add(playAgainButton);
    }

    public final JComponent getMainView() {
        return mainRootPane;
    }

    public JComponent getGameLog() {
        return textArea;
    }

    public String getPortNumber() {
        return portNum.getText();
    }

    public String getIp() {
        return hostNum.getText();
    }

    private void onConnectClick() {
        log.debug("inside onConnectClick");
        try {
            log.debug("inside onConnectClick before getClientPacket.connectToServer()");
            if (this.threeStonesClnt.getClientPacket().connectToServer()) {
                this.textArea.setText("connection successful");
                enableBoard();
            }
            log.debug("inside onConnectClick after getClientPacket.connectToServer()");
            this.connectBtn.setEnabled(false);
            this.connectBtn.setBackground(Color.GREEN);
            log.debug("inside onConnectClick end of try catch");
        } catch (IOException ex) {
            Logger.getLogger(ThreeStonesGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void enableBoard() {
        log.debug("start enableBoard ");
        CellState[][] board = threeStonesClnt.getBoard().getBoard();
        log.debug("board to string  " + Arrays.toString(board));
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != CellState.VACANT) {
                    gameBoardCells[i][j].setEnabled(true);
                }
            }
        }
    }

    private void onCloseConnectionClick() {

    }

    private void onPlayAgainClick() {
        // this must clear board and scores but keeps connection
    }
}
