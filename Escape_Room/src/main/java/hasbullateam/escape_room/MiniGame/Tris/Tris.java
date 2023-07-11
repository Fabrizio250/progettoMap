package hasbullateam.escape_room.MiniGame.Tris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tris extends JPanel implements ActionListener {
    private final JButton[][] buttons;
    private final JLabel player1WinsLabel;
    private final JLabel player2WinsLabel;
    private final JLabel computerWinsLabel;

    private int player1Wins;
    private int player2Wins;
    private int computerWins;
    private boolean gameOver;
    private boolean player1Turn;
    private int modalitaScelta;

    public Tris(int modalitaScelta) {
        super();
        this.modalitaScelta=modalitaScelta;
        buttons = new JButton[3][3];
        player1WinsLabel = new JLabel();
        player2WinsLabel = new JLabel();
        computerWinsLabel = new JLabel();

        if (modalitaScelta == 1) {
            player1WinsLabel.setText("Giocatore 1: 0");
            player2WinsLabel.setText("Giocatore 2: 0");
            player1Wins = 0;
            player2Wins = 0;
            player1Turn = true;
        } else if (modalitaScelta == 2) {
            player1WinsLabel.setText("Giocatore: 0");
            computerWinsLabel.setText("Computer: 0");
            player1Wins = 0;
            computerWins = 0;
        }
        gameOver = false;

        JPanel gamePanel = new JPanel(new GridLayout(3, 3));
        gamePanel.setPreferredSize(new Dimension(600, 600));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
                buttons[row][col].addActionListener(this);
                gamePanel.add(buttons[row][col]);
            }
        }

        JPanel scorePanel = new JPanel(new GridLayout(1, 2));
        if (modalitaScelta == 1) {
            setWinsLabels(player1WinsLabel, player2WinsLabel, scorePanel);
        } else if (modalitaScelta == 2) {
            setWinsLabels(player1WinsLabel, computerWinsLabel, scorePanel);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(scorePanel, BorderLayout.SOUTH);
        this.add(mainPanel);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        if(modalitaScelta == 1){
            if (clickedButton.getText().equals("") && !gameOver) {
                if (player1Turn) {
                    clickedButton.setText("X");
                    player1Turn = false;
                } else {
                    clickedButton.setText("O");
                    player1Turn = true;
                }

                if(checkWin("X")) {
                    player1Wins++;
                    JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 1");
                    player1WinsLabel.setText("Giocatore 1: " + player1Wins);

                    if (player1Wins == 3) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(this, "Hai vinto! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    resetGame();
                    return;
                }

                if (checkWin("O")) {
                    player2Wins++;
                    JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 2");
                    player2WinsLabel.setText("Giocatore 2: " + player2Wins);

                    if (player2Wins == 3) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 2! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    resetGame();
                    return;
                }

                if (checkTie()) {
                    resetGame();
                    return;
                }
            }
        }

        else if (modalitaScelta == 2 ) {


            if (clickedButton.getText().equals("")  && !gameOver) {
                clickedButton.setText("X");


                if(checkWin("X")) {
                    player1Wins++;
                    JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 1");
                    player1WinsLabel.setText("Giocatore 1: " + player1Wins);

                    if (player1Wins == 3) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(this, "Hai vinto! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    resetGame();
                    return;

                }

                if (checkTie()) {
                    resetGame();
                    return;
                }

                computerMove();
                if (checkWin("O")) {
                    computerWins++;
                    JOptionPane.showMessageDialog(this, "Vittoria del Computer");
                    computerWinsLabel.setText("Computer: " + computerWins);

                    if (computerWins == 3) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(this, "Il computer ha vinto! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }

                    resetGame();

                    if (checkTie()) {
                        resetGame();
                        return;
                    }
                }
            }
        }
    }


    private void computerMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText("O");
        player1Turn = true;
    }

    private boolean checkWin(String symbol) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals(symbol) &&
                    buttons[row][1].getText().equals(symbol) &&
                    buttons[row][2].getText().equals(symbol)) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals(symbol) &&
                    buttons[1][col].getText().equals(symbol) &&
                    buttons[2][col].getText().equals(symbol)) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][2].getText().equals(symbol)) {
            return true;
        }

        if (buttons[0][2].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][0].getText().equals(symbol)) {
            return true;
        }

        return false;
    }

    private boolean checkTie() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Pareggio! Partita Terminata.", "Pareggio", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
    }

    private void setWinsLabels(JLabel label1, JLabel label2, JPanel scorePanel) {
        scorePanel.add(label1);
        scorePanel.add(label2);
        scorePanel.setPreferredSize(new Dimension(100, 150));
        Font font = label1.getFont();
        Font nuovaDimensione = font.deriveFont(font.getSize() + 10f);
        label1.setFont(nuovaDimensione);
        font = label2.getFont();
        nuovaDimensione = font.deriveFont(font.getSize()+ 10f);
        label2.setFont(nuovaDimensione);
    }
}
