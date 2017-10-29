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
 * text fields, text areas, and labels in order to display to the user the game
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

    // score labels and values
    private final JLabel clientScorePnts = new JLabel("0");
    private final JLabel serverScorePnts = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent's Score");

    //stone count labels and values
    private final JLabel clientStoneCount = new JLabel("0");
    private final JLabel serverStoneCount = new JLabel("0");
    private final JLabel clientStoneCountTV = new JLabel("Your Stones");
    private final JLabel serverStoneCountTV = new JLabel("Opponent's Stones");

    //port labels
    private final JTextField portNum = new JTextField("50000", 10);
    private final JTextField hostNum = new JTextField("localhost", 10);

    //buttons
    private final JButton quitBtn = new JButton("Quit Game");
    private final JButton connectBtn = new JButton("Connect");
    private final JButton playAgainButton = new JButton("Play Again");

    //the cells representing the slots in the three stones game board
    private final JButton[][] gameBoardCells = new JButton[11][11];

    //text area containing messages during the game
    JTextArea textArea = new JTextArea(5, 5);

    private final ThreeStonesClient threeStonesClnt;
    private final ThreeStonesClientGameBoard clientGameBoard;
    private JPanel threeStonesGameBoard;
    private JFrame frame;
    private boolean isLastMove;

    /**
     * Default constructor that initializes a ThreeStonesClient instance
     */
    public ThreeStonesGUI() {
        this.threeStonesClnt = new ThreeStonesClient();
        this.clientGameBoard = this.threeStonesClnt.getBoard();
        clientGameBoard.setGui(this);
        this.isLastMove = false;
    }

    /**
     * Builds the User Interface of the Three Stones game to display to the user
     * containing the board game, the points, the number of stones and other
     * info such as the ip address of the server
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

        clientStoneCountTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientStoneCountTV.setForeground(Color.WHITE);

        serverStoneCountTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverStoneCountTV.setForeground(Color.BLACK);
        buildServerScoreLbl();
        buildClientStoneCountLbl();
        buildServerStoneCountLbl(); //build threeStonesGameBoard instance and add it to the main layout
        threeStonesGameBoard = new JPanel(new GridLayout(0, 11));
        threeStonesGameBoard.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        mainRootPane.add(threeStonesGameBoard, BorderLayout.CENTER);

        buildGameBoard();
        addButtonsListInBoard();
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

    /**
     * Helper method to build the three stones game board with the cells or
     * slots with the appropriate initial cell state. Each slot are represented
     * by a button on the UI and the content of the board is taken from the
     * board saved in the ThreeStonesClient instance.
     */
    private void buildGameBoard() {
        //loop through the 2D array which represents the game board and fill the array 
        for (int x = 0; x < gameBoardCells.length; x++) {
            for (int y = 0; y < gameBoardCells.length; y++) {
                //fill the empty gameBoardCells array according to the board saved in the client instance
                switch (clientGameBoard.getBoard()[x][y]) {
                    case VACANT: //if vacant slot, make the button color orange
                        gameBoardCells[x][y] = new JButton();
                        gameBoardCells[x][y].setPreferredSize(new Dimension(60, 60));
                        //gameBoardCells[x][y].setText(clientGameBoard.getBoard()[x][y].toString().substring(0, 1));
                        gameBoardCells[x][y].setBackground(Color.ORANGE);
                        break;
                    case AVAILABLE: //if available slot, then set button color to white and set click listeners
                        gameBoardCells[x][y] = new JButton();
                        gameBoardCells[x][y].setPreferredSize(new Dimension(60, 60));
                        //gameBoardCells[x][y].setText(clientGameBoard.getBoard()[y][x].toString().substring(0, 1));
                        gameBoardCells[x][y].setBackground(Color.YELLOW);
                        final int positionX = x;
                        final int positionY = y;
                        gameBoardCells[x][y].addActionListener(e -> {
                            try {
                                threeStonesClnt.clickBoardCell(positionX, positionY);
                            } catch (IOException ex) {
                                Logger.getLogger(ThreeStonesGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                }
                gameBoardCells[x][y].setEnabled(false);
            }
        }

        /*for (int i = 0; i < gameBoardCells[0].length; i++) {
            for (int j = 0; j < gameBoardCells[0].length; j++) {
                gameBoardCells[i][j].setText(clientGameBoard.getBoard()[i][j].toString().substring(0, 1) + "\n" + i + j);
            }
        }*/
    }

    private void addButtonsListInBoard() {
        //the following loop is to add the gameBoardCells array to the UI 
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                threeStonesGameBoard.add(gameBoardCells[i][j]);
            }
        }
    }

    /**
     * Builds the text area in the UI where information are displayed to the
     * user such as game over, game win or tie.
     */
    private void buildTextArea() {
        textArea = new JTextArea(1, 1);
        textArea.setLineWrap(true);
        textArea.setPreferredSize(new Dimension(200, frame.getHeight()));
        frame.getContentPane().add(textArea, BorderLayout.EAST);
    }

    /**
     * Builds the panel containing the scores information
     *
     * @param scoresPanel JPanel which to build
     */
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

    /**
     * Builds the tool bar containing the scores information
     *
     * @param scoresToolbar JToolBar to build
     */
    private void buildScoresToolBar(JToolBar scoresToolbar) {
        scoresToolbar.setFloatable(false);
        scoresToolbar.setBackground(Color.decode("#e67e22"));
        scoresToolbar.add(clientScoreTV);
        scoresToolbar.add(clientScorePnts);
        scoresToolbar.add(serverScoreTV);
        scoresToolbar.add(serverScorePnts);

        scoresToolbar.add(clientStoneCountTV);
        scoresToolbar.add(clientStoneCount);
        scoresToolbar.add(serverStoneCountTV);
        scoresToolbar.add(serverStoneCount);

        scoresToolbar.setAlignmentX(0);
    }

    /**
     * Builds the Label containing the server score title
     */
    private void buildServerScoreLbl() {
        serverScorePnts.setHorizontalAlignment(JTextField.CENTER);
        serverScorePnts.setMaximumSize(new Dimension(100, 40));
        serverScorePnts.setPreferredSize(new Dimension(100, 40));
        serverScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScorePnts.setForeground(Color.BLACK);
    }

    /**
     * Builds the Label containing the client score title
     */
    private void buildClientScoreLbl() {
        clientScorePnts.setHorizontalAlignment(JTextField.CENTER);
        clientScorePnts.setPreferredSize(new Dimension(100, 40));
        clientScorePnts.setMaximumSize(new Dimension(100, 40));
        clientScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScorePnts.setForeground(Color.WHITE);
    }

    private void buildClientStoneCountLbl() {
        clientStoneCount.setHorizontalAlignment(JTextField.CENTER);
        clientStoneCount.setPreferredSize(new Dimension(100, 40));
        clientStoneCount.setMaximumSize(new Dimension(100, 40));
        clientStoneCount.setFont(new Font("Monospace", Font.BOLD, 15));
        clientStoneCount.setForeground(Color.WHITE);
    }

    private void buildServerStoneCountLbl() {
        serverStoneCount.setHorizontalAlignment(JTextField.CENTER);
        serverStoneCount.setMaximumSize(new Dimension(100, 40));
        serverStoneCount.setPreferredSize(new Dimension(100, 40));
        serverStoneCount.setFont(new Font("Monospace", Font.BOLD, 15));
        serverStoneCount.setForeground(Color.BLACK);
    }

    /**
     * Builds the label containing the host number title
     *
     * @param settingsToolbar JToolBar in which to add the label
     */
    private void buildHostNumField(JToolBar settingsToolbar) {
        hostNum.setHorizontalAlignment(JTextField.CENTER);
        hostNum.setBorder(new LineBorder(Color.decode("#e67e22"), 1));
        hostNum.setFont(new Font("Monospace", Font.BOLD, 15));
        settingsToolbar.add(hostNum);
        settingsToolbar.setAlignmentX(0);
    }

    /**
     * Builds the label containing the port number
     *
     * @param settingsToolbar JToolBar in which to add the label
     */
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

    /**
     * Builds the button that connects to the server
     *
     * @param settingsToolbar JToolBar in which to add the label
     */
    private void buildConnectBtn(JToolBar settingsToolbar) {
        connectBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        connectBtn.setPreferredSize(new Dimension(100, 30));
        connectBtn.addActionListener(e -> {
            onConnectClick();
        });
        settingsToolbar.add(connectBtn);
    }

    /**
     * Builds the button that quits the game
     *
     * @param settingsToolbar JToolBar in which to add the label
     */
    private void buildQuitBtn(JToolBar settingsToolbar) {
        quitBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        quitBtn.setPreferredSize(new Dimension(100, 30));
        quitBtn.addActionListener(e -> {
            onQuitGameClick();
        });
        quitBtn.setEnabled(false);
        settingsToolbar.add(quitBtn);
    }

    /**
     * Builds the button that play the game
     *
     * @param settingToolBar JToolBar in which to add the label
     */
    private void buildPlayAgainBtn(JToolBar settingToolBar) {
        playAgainButton.setPreferredSize(new Dimension(100, 30));
        playAgainButton.setFont(new Font("Monospace", Font.BOLD, 15));
        playAgainButton.addActionListener(e -> {
            onPlayAgainClick();
        });
        playAgainButton.setEnabled(false);
        settingToolBar.add(playAgainButton);
    }

    /**
     * Returns the JComponent main root pane layout
     *
     * @return JComponent object
     */
    public final JComponent getMainView() {
        return mainRootPane;
    }

    /**
     * Returns the text area of the UI
     *
     * @return JComponent object
     */
    public JComponent getGameLog() {
        return textArea;
    }

    /**
     * Returns the port number text field from the UI
     *
     * @return String
     */
    public String getPortNumber() {
        return portNum.getText();
    }

    /**
     * Returns the ip text field from the UI
     *
     * @return String
     */
    public String getIp() {
        return hostNum.getText();
    }

    /**
     * Event handler method that handles the click event of the connect button.
     * It enables all button on the board and displays the to the user that
     * connection has been established and user can start playing.
     */
    private void onConnectClick() {
        log.debug("inside onConnectClick");
        if (this.threeStonesClnt.getClientPacket().sendStartGameRequestToServer()) {
            this.textArea.setText("Connection successful! You can start the game");
            this.connectBtn.setEnabled(false);
            this.connectBtn.setBackground(Color.GREEN);
            enableBoard(true);
        } else {
            this.textArea.setText("Connection unsuccessfull! Please try again");
        }
    }

    /**
     * Enables or disables the game board by making all available slots/button
     * clickable or not clickable
     */
    private void enableBoard(boolean isEnabled) {
        log.debug("start enableBoard ");
        CellState[][] board = clientGameBoard.getBoard();

        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != CellState.VACANT) {
                    gameBoardCells[i][j].setEnabled(isEnabled);
                }
            }
        }
    }

    /**
     * Event handler method that handles the click event of the quit game by
     * closing the connection with the server which ends the game
     */
    private void onQuitGameClick() {
        log.debug("inside onQuitGameClick");
        this.threeStonesClnt.getClientPacket().sendQuitGameRequestToServer();

        this.playAgainButton.setEnabled(false);
        this.quitBtn.setEnabled(false);
        this.connectBtn.setEnabled(true);
        this.connectBtn.setBackground(null);

        reinitializeBoard();
        textArea.setText("");
    }

    /**
     * Event handler method for the play again button which resets the boards
     * and the points
     */
    private void onPlayAgainClick() {
        log.debug("inside onPlayAgainClick");
        if (this.threeStonesClnt.getClientPacket().sendPlayAgainRequestToServer()) {
            this.textArea.setText("A new game has started.");
            this.playAgainButton.setEnabled(false);
            this.quitBtn.setEnabled(false);
            reinitializeBoard();
        } else {
            this.textArea.setText("A new game cannot be started. Please try again.");
        }
    }

    private void reinitializeBoard() {
        this.clientGameBoard.startNewGame();
        buildGameBoard();
        threeStonesGameBoard.removeAll();
        addButtonsListInBoard();
        threeStonesGameBoard.revalidate();
        displayPointsAndStoneCount();
        enableBoard(true);
    }

    public void updateView(int x, int y, CellState color) {
        if (color == CellState.WHITE) {
            this.gameBoardCells[x][y].setBackground(Color.WHITE);
        } else if (color == CellState.BLACK) {
            this.gameBoardCells[x][y].setBackground(Color.BLACK);
        }

        for (int i = 0; i < gameBoardCells[0].length; i++) {
            for (int j = 0; j < gameBoardCells[0].length; j++) {
                //gameBoardCells[i][j].setText(clientGameBoard.getBoard()[i][j].toString().substring(0, 1) + "\n" + i + j);
                displayPointsAndStoneCount();

                if (this.clientGameBoard.getBoard()[i][j] == CellState.UNAVAILABLE) {
                    gameBoardCells[i][j].setBackground(Color.YELLOW);
                    gameBoardCells[i][j].setEnabled(false);
                } else if (this.clientGameBoard.getBoard()[i][j] == CellState.AVAILABLE) {
                    gameBoardCells[i][j].setBackground(Color.GREEN);
                    gameBoardCells[i][j].setEnabled(true);
                } else {
                    gameBoardCells[i][j].setEnabled(false);
                }
            }
        }
        //gameBoardCells[x][y].setText(clientGameBoard.getBoard()[x][y].toString().substring(0, 1));
    }

    private void displayPointsAndStoneCount() {
        clientScorePnts.setText(clientGameBoard.getWhiteScore() + "");
        serverScorePnts.setText(clientGameBoard.getBlackScore() + "");
        clientStoneCount.setText(clientGameBoard.getWhiteStoneCount() + "");
        serverStoneCount.setText(clientGameBoard.getBlackStoneCount() + "");
    }

    public void notifyPlayerWon() {
        this.textArea.setText(this.textArea.getText() + "\n\nYou won the game!\n\nPlay Again or Quit?");
        playAgainButton.setEnabled(true);
        quitBtn.setEnabled(true);
        enableBoard(false);
    }

    public void notifyServerWon() {
        this.textArea.setText(this.textArea.getText() + "\n\nYour opponent won the game!\n\nPlay Again or Quit?");
        playAgainButton.setEnabled(true);
        quitBtn.setEnabled(true);
        enableBoard(false);
    }

    public void notifyTieGame() {
        this.textArea.setText(this.textArea.getText() + "\n\nThe game is a Tie.\n\nPlay Again or Quit?");
        playAgainButton.setEnabled(true);
        quitBtn.setEnabled(true);
        enableBoard(false);
    }

    public void handlePlayerLastMove() {
        this.textArea.setText(this.textArea.getText() + "\n\nYou have one move left.");
        // this.isLastMove = true;
    }
}
