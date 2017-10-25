/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.view;

import com.threestones.client.gamestate.ThreeStonesClient;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard;
import com.threestones.client.gamestate.ThreeStonesClientGameBoard.CellState;
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
import java.util.Arrays;
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
public class ThreeStonesGUI {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    //gui is the root Pane that supports the entire UI
    private final JPanel mainView = new JPanel(new BorderLayout(0, 0));

    
    private JButton[][] cells = new JButton[11][11];

    
    private JPanel boardGame;

    
    private final JTextField portNumber = new JTextField("50000", 10);

    
    private final JTextField hostNumber = new JTextField("localhost", 10);

    
    private final JLabel portLabel = new JLabel(
            "Port");
    private final JLabel hostLabel = new JLabel(
            "Host");

    private JLabel clientScorePoints = new JLabel("0");
    private JLabel serverScoreValue = new JLabel("0");
    private final JLabel clientScoreTV = new JLabel("Your Score");
    private final JLabel serverScoreTV = new JLabel("Opponent Score");

    
    private JButton quitBtn = new JButton("Quit");

    private JButton connectBtn = new JButton("Connect");

    JTextArea textArea = new JTextArea(5, 5);
    private ThreeStonesClient client;

    public ThreeStonesGUI() {
        this.client = new ThreeStonesClient();

    }

    public void buildView() {

        
        mainView.setBorder(new EmptyBorder(5, 10, 5, 10));
        JPanel settingsPanel = new JPanel();

        settingsPanel.setBackground(Color.decode("#e67e22"));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        mainView.setBackground(Color.decode("#e67e22"));
        
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
        
        
        clientScoreTV.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScoreTV.setForeground(Color.WHITE);
        clientScorePoints.setHorizontalAlignment(JTextField.CENTER);
        clientScorePoints.setPreferredSize(new Dimension(150, 40));
        clientScorePoints.setMaximumSize(new Dimension(100, 150));

        clientScorePoints.setFont(new Font("Monospace", Font.BOLD, 15));
        clientScorePoints.setForeground(Color.WHITE);
        

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

        // create the board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        JPanel pane = new JPanel();
        JTable table = new JTable(11, 11);
        //pane.setLayout(new GridLayout(11, 11));
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
    }

    public void onResetClick() {
// this must clear board and scores but keeps connection

    }

    public final JComponent getMainView() {
        return mainView;
    }

    public JComponent getGameLog() {
        return textArea;
    }

    public String getPortNumber() {
        return portNumber.getText();
    }

    public String getIp() {
        return hostNumber.getText();
    }

    

    private void onConnectClick() {
        log.debug("inside onConnectClick");
        try {
            log.debug("inside onConnectClick before getClientPacket.connectToServer()");
            if (this.client.getClientPacket().connectToServer()) {
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
        CellState[][] board = client.getBoard().getBoard();
        log.debug("board to string  " + Arrays.toString(board));
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != CellState.VACANT) {
                    cells[i][j].setEnabled(true);
                }
            }
        }
    }

    private void onCloseConnectionClick() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
