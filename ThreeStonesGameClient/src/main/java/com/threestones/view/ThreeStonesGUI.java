package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesClientGameController;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
 * stones left, and the game board.
 *
 * @author Eric Hughes
 * @author Lyrene Labor
 * @author Jacob Riendeau
 */
public class ThreeStonesGUI {

    //for logging
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    //the parent container of the whole window UI
    private final JPanel mainRootPane = new JPanel(new BorderLayout(0, 0));

    // score labels and values
    private final JLabel clientScorePnts = new JLabel("0");
    private final JLabel serverScorePnts = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent's Score");

    //stone count labels and values
    private final JLabel clientStoneCount = new JLabel("15");
    private final JLabel serverStoneCount = new JLabel("15");
    private final JLabel clientStoneCountTV = new JLabel("Your Stones");
    private final JLabel serverStoneCountTV = new JLabel("Opponent's Stones");

    //buttons
    private final JButton quitBtn = new JButton("Quit Game");
    private final JButton connectBtn = new JButton("Connect");
    private final JButton startGameBtn = new JButton("Start Game");
    private final JButton playAgainButton = new JButton("Play Again");

    //the cells representing the slots in the three stones game board
    private final JButton[][] gameBoardCells = new JButton[11][11];

    //text area containing messages during the game
    JTextArea textArea = new JTextArea(5, 5);

    private final ThreeStonesClientGameController threeStonesClntCont;
    private final ThreeStonesClientGameBoard clientGameBoard;
    private JPanel guiGameBoard;
    private JFrame frame;

    /**
     * Default constructor that initializes a ThreeStonesClientGameController instance, the
 client game board and passes it a handle to the current instance
     */
    public ThreeStonesGUI() {
        this.threeStonesClntCont = new ThreeStonesClientGameController();
        this.clientGameBoard = this.threeStonesClntCont.getBoard();
        clientGameBoard.setGui(this);
    }

    /**
     * Builds the User Interface of the Three Stones game to display to the user
     * containing the board game, the points, the number of stones, the buttons,
     * and the text area in which messages will appear during the game
     */
    public void buildThreeStonesUI() {
        //Create and set up the window.
        frame = new JFrame("ThreeStones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //build the scores panel
        JPanel scoresPanel = new JPanel();
        buildScoresPanel(scoresPanel);

        //build the client and server score ui components
        buildClientScoreLbl();
        buildServerScoreLbl();

        //build the client and server stone count ui components
        buildClientStoneCountLbl();
        buildServerStoneCountLbl();

        //the following is to the build the game board ui components
        guiGameBoard = new JPanel(new GridLayout(0, 11));
        guiGameBoard.setBorder(new LineBorder(Color.decode("#e67e22"), 3));
        mainRootPane.add(guiGameBoard, BorderLayout.CENTER);
        buildGameBoard();
        addButtonsListInBoard();

        buildTextArea(); //build the text area that will the game messages

        //build the panel containing the settings information
        JPanel settingsPanel = new JPanel();
        buildSettingsPanel(settingsPanel);

        //build the toolbar containing the different settings and buttons
        JToolBar settingsToolbar = new JToolBar();
        settingsToolbar.setFloatable(false);
        buildConnectBtn(settingsToolbar); //build and add connect button to toolbar
        buildStartBtn(settingsToolbar);//build and add start game button to toolbar
        buildQuitBtn(settingsToolbar); //build and add quit button to toolbar
        buildPlayAgainBtn(settingsToolbar); //build and add play again button to toolbar
        settingsPanel.add(settingsToolbar);

        //add everything to the parent frame container
        frame.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
        frame.add(mainRootPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Helper method to build the three stones game board having cells or slots
     * with the appropriate initial cell state. Each slot are represented by a
 button on the UI and the content and state of each cells of the board is
 taken from the board saved in the ThreeStonesClientGameController instance.
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
                        gameBoardCells[x][y].setBackground(Color.ORANGE);
                        break;
                    case AVAILABLE: //if available slot, then set button color to yellow and set click listener
                        gameBoardCells[x][y] = new JButton();
                        gameBoardCells[x][y].setPreferredSize(new Dimension(60, 60));
                        gameBoardCells[x][y].setBackground(Color.YELLOW);
                        final int positionX = x;
                        final int positionY = y;
                        gameBoardCells[x][y].addActionListener(e -> {
                            threeStonesClntCont.handleClickBoardCell(positionX, positionY);
                        });
                }
                gameBoardCells[x][y].setEnabled(false);
            }
        }
    }

    /**
     * Adds the array JButtons into the UI
     */
    private void addButtonsListInBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                guiGameBoard.add(gameBoardCells[i][j]);
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
     * Builds the UI components containing the server score title
     */
    private void buildServerScoreLbl() {
        //build the server score text view
        serverScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScoreTV.setForeground(Color.BLACK);

        serverScorePnts.setHorizontalAlignment(JTextField.CENTER);
        serverScorePnts.setMaximumSize(new Dimension(100, 40));
        serverScorePnts.setPreferredSize(new Dimension(100, 40));
        serverScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        serverScorePnts.setForeground(Color.BLACK);
    }

    /**
     * Builds the UI components containing the client score title
     */
    private void buildClientScoreLbl() {
        clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScoreTV.setForeground(Color.WHITE);

        clientScorePnts.setHorizontalAlignment(JTextField.CENTER);
        clientScorePnts.setPreferredSize(new Dimension(100, 40));
        clientScorePnts.setMaximumSize(new Dimension(100, 40));
        clientScorePnts.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScorePnts.setForeground(Color.WHITE);
    }

    /**
     * Builds the UI components containing the client stone count
     */
    private void buildClientStoneCountLbl() {
        clientStoneCountTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientStoneCountTV.setForeground(Color.WHITE);

        clientStoneCount.setHorizontalAlignment(JTextField.CENTER);
        clientStoneCount.setPreferredSize(new Dimension(100, 40));
        clientStoneCount.setMaximumSize(new Dimension(100, 40));
        clientStoneCount.setFont(new Font("Monospace", Font.BOLD, 15));
        clientStoneCount.setForeground(Color.WHITE);
    }

    /**
     * Builds the UI components containing the server stone count
     */
    private void buildServerStoneCountLbl() {
        serverStoneCountTV.setFont(new Font("Monospace", Font.BOLD, 15));
        serverStoneCountTV.setForeground(Color.BLACK);

        serverStoneCount.setHorizontalAlignment(JTextField.CENTER);
        serverStoneCount.setMaximumSize(new Dimension(100, 40));
        serverStoneCount.setPreferredSize(new Dimension(100, 40));
        serverStoneCount.setFont(new Font("Monospace", Font.BOLD, 15));
        serverStoneCount.setForeground(Color.BLACK);
    }

    /**
     * Style the panel containing the settings tool bar.
     *
     * @param settingsPanel JPanel object
     */
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
     * Builds the start button to let the client start playing the game
     *
     * @param settingsToolbar
     */
    private void buildStartBtn(JToolBar settingsToolbar) {
        startGameBtn.setFont(new Font("Monospace", Font.BOLD, 15));
        startGameBtn.setPreferredSize(new Dimension(100, 30));
        startGameBtn.addActionListener(e -> {
            onStartClick();
        });
        startGameBtn.setEnabled(false);
        settingsToolbar.add(startGameBtn);
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
     * Event handler method that handles the click event of the connect button.
     * It enables all button on the board and displays the to the user that
     * connection has been established and user can start playing.
     */
    private void onConnectClick() {
        log.debug("inside onConnectClick");
        String ipAddress = JOptionPane.showInputDialog(frame,
                "Enter IP Address of Server", null);
        try {
            this.threeStonesClntCont.createConnectionWithServer(ipAddress);
            textArea.setText("Connection established sucessfully.");
            connectBtn.setEnabled(false);
            connectBtn.setBackground(Color.GREEN);
            startGameBtn.setEnabled(true);
        } catch (IOException ex) {
            textArea.setText("Connection cannot be established. Please try again.");
            Logger.getLogger(ThreeStonesGUI.class.getName()).log(Level.SEVERE,
                    "Error connecting with Server", ex);
        }
    }

    /**
     * Enables or disables the game board by making all available slots/button
     * clickable or not clickable.
     *
     * @param boolean true or false to enable the board
     */
    private void enableBoard(boolean isEnabled) {
        log.debug("Enabling board...");
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
     * closing the socket connection with the server which ends the game
     */
    private void onQuitGameClick() {
        log.debug("inside onQuitGameClick");
        //send quit game request to the server
        this.threeStonesClntCont.sendQuitGameRequestToServer();

        //disable all buttons except the connect button
        this.playAgainButton.setEnabled(false);
        this.quitBtn.setEnabled(false);
        this.connectBtn.setEnabled(true);
        this.connectBtn.setBackground(null);
        this.startGameBtn.setBackground(null);

        //set up the board to how it was at the beginning
        reinitializeBoard();
        enableBoard(false);
        textArea.setText("");
    }

    /**
     * Event handler method that handles the click event of the start game
     * button by sending a start game request to the server and enabling the
     * board for the user to start playing
     */
    private void onStartClick() {
        log.debug("inside onStartClick");
        if (this.threeStonesClntCont.sendStartGameRequestToServer()) {
            this.textArea.setText(this.textArea.getText() + "\n\nBoard Enabled. "
                    + "You can start your first move");
            this.startGameBtn.setEnabled(false);
            this.startGameBtn.setBackground(Color.CYAN);
            enableBoard(true);
        } else {
            this.textArea.setText("Cannot start game! Please try again");
        }
    }

    /**
     * Event handler method for the play again button which resets the boards
     * and the points
     */
    private void onPlayAgainClick() {
        log.debug("inside onPlayAgainClick");
        if (this.threeStonesClntCont.sendPlayAgainRequestToServer()) {
            this.textArea.setText("A new game has started.");
            this.playAgainButton.setEnabled(false);
            this.quitBtn.setEnabled(false);
            reinitializeBoard();
        } else {
            this.textArea.setText("A new game cannot be started. Please try again.");
        }
    }

    /**
     * Sets up the game board to how it was at the beginning to let the user
     * start playing the game again
     */
    private void reinitializeBoard() {
        //reset the client game board
        this.clientGameBoard.startNewGame();
        //builds the UI game board once again
        buildGameBoard();
        guiGameBoard.removeAll(); //empty the gui game board of previous buttons
        addButtonsListInBoard(); //add the new buttons into the ui
        guiGameBoard.revalidate();
        //display again the initial stone count and points
        displayPointsAndStoneCount();
        enableBoard(true);
    }

    /**
     * Updates the board with the new move's x and y coordinates and the stone
     * color and updates the rest of the board as well. All cells that are
     * available for the next move will be set to green, while all other cells
     * are set back to yellow. The last move is highlighted with red borders.
     *
     * @param x coord x position
     * @param y coord y position
     * @param color CellState stone color
     */
    public void updateView(int x, int y, CellState color) {
        //loop through the game board to update each cell
        for (int i = 0; i < gameBoardCells[0].length; i++) {
            for (int j = 0; j < gameBoardCells[0].length; j++) {
                displayPointsAndStoneCount(); //display the new stone counts and points
                if (this.clientGameBoard.getBoard()[i][j] == CellState.UNAVAILABLE) {
                    gameBoardCells[i][j].setBackground(Color.YELLOW);
                    gameBoardCells[i][j].setEnabled(false);
                } else if (this.clientGameBoard.getBoard()[i][j] == CellState.AVAILABLE) {
                    gameBoardCells[i][j].setBackground(Color.GREEN);
                    gameBoardCells[i][j].setEnabled(true);
                } else if (i == x && j == y && color == CellState.WHITE) {
                    gameBoardCells[i][j].setEnabled(false);
                    gameBoardCells[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    gameBoardCells[i][j].setBackground(Color.WHITE);
                } else if (i == x && j == y && color == CellState.BLACK) {
                    gameBoardCells[i][j].setEnabled(false);
                    gameBoardCells[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    gameBoardCells[i][j].setBackground(Color.BLACK);
                } else {
                    if (this.clientGameBoard.getBoard()[i][j] != CellState.VACANT) {
                        gameBoardCells[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                    }
                }
            }
        }
    }

    /**
     * Displays the updated stone counts and the points to the labels on the ui
     */
    private void displayPointsAndStoneCount() {
        clientScorePnts.setText(clientGameBoard.getWhiteScore() + "");
        serverScorePnts.setText(clientGameBoard.getBlackScore() + "");
        clientStoneCount.setText(clientGameBoard.getWhiteStoneCount() + "");
        serverStoneCount.setText(clientGameBoard.getBlackStoneCount() + "");
    }

    /**
     * Notifies the player that he/she has won the game by displaying a message
     * in the text area. Enables back the play again button and the quit button
     * for the player to choose. Disables the board.
     *
     * @param whitePoints int white points to display
     * @param blackPoints int black points to display
     */
    public void notifyPlayerWon(int whitePoints, int blackPoints) {
        String message = "\n\nYou won the game with "
                + whitePoints + " points and your opponent with " + blackPoints + " points."
                + "\n\nPlay Again or Quit?";
        notifyPlayer(message);
    }

    /**
     * Notifies the player that server/opponent has won by displaying a message
     * in the text area. Enables back the play again button and the quit button
     * for the player to choose. Disables the board.
     *
     * @param whitePoints int white points to display
     * @param blackPoints int black points to display
     */
    public void notifyServerWon(int whitePoints, int blackPoints) {
        String message = "\n\nYour opponent won the game with "
                + blackPoints + " points and you scored " + whitePoints + " points."
                + "\n\nPlay Again or Quit?";
        notifyPlayer(message);
    }

    /**
     * Notifies the game is a tie by displaying a message in the text area.
     * Enables back the play again button and the quit button for the player to
     * choose. Disables the board.
     *
     * @param whitePoints
     * @param blackPoints
     */
    public void notifyTieGame(int whitePoints, int blackPoints) {
        String message = "\n\nThe game is a Tie with "
                + " your score of " + whitePoints + " and your opponent's score of "
                + blackPoints + "\n\nPlay Again or Quit?";
        notifyPlayer(message);
    }

    /**
     * Displays a message to the text area for the user to see concerning the
     * result of the game. Sets the quit and the play again button to enabled
     * for the user to choose to play again or quit.
     *
     * @param message String message to display
     */
    private void notifyPlayer(String message) {
        this.textArea.setText(this.textArea.getText() + message);
        playAgainButton.setEnabled(true);
        quitBtn.setEnabled(true);
        enableBoard(false);
    }

    /**
     * Displays to the player that he/she has one move left
     */
    public void handlePlayerLastMove() {
        this.textArea.setText(this.textArea.getText() + "\n\nYou have one move left.");
    }
}
