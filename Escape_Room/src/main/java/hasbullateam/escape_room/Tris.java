package hasbullateam.escape_room;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tris extends JPanel implements ActionListener {
    private final JButton[][] buttons;
    private final JLabel playerWinsLabel;
    private final JLabel computerWinsLabel;

    private int playerWins;
    private int computerWins;
    private boolean gameOver;

    public Tris() {
        super();
        buttons = new JButton[3][3];
        playerWinsLabel = new JLabel("Giocatore: 0");
        computerWinsLabel = new JLabel("Computer: 0");

        playerWins = 0;
        computerWins = 0;
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
        scorePanel.add(playerWinsLabel);
        scorePanel.add(computerWinsLabel);
        scorePanel.setPreferredSize(new Dimension(100,150));
        Font font = playerWinsLabel.getFont();
        Font nuovaDimensione = font.deriveFont(font.getSize() + 10f); // Aggiungi 10 punti alla dimensione corrente
        playerWinsLabel.setFont(nuovaDimensione);
         font= computerWinsLabel.getFont();
         nuovaDimensione = font.deriveFont(font.getSize() + 10f); // Aggiungi 10 punti alla dimensione corrente
        computerWinsLabel.setFont(nuovaDimensione);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(scorePanel, BorderLayout.SOUTH);
        this.add(mainPanel);
        setVisible(true);
        
    }

    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        if (clickedButton.getText().equals("") && !gameOver) {
            clickedButton.setText("X");

            if (checkWin("X")) {
                playerWins++;
                JOptionPane.showMessageDialog(this,"Vittoria del giocatore");
                playerWinsLabel.setText("Giocatore: " + playerWins);

                if (playerWins == 3) {
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
                  JOptionPane.showMessageDialog(this,"Vittoria dell' avversario");
                computerWinsLabel.setText("Computer: " + computerWins);

                if (computerWins == 3) {
                    gameOver = true;
                    JOptionPane.showMessageDialog(this, "Il computer ha vinto! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }

                resetGame();
            }
        }
    }

    private void computerMove() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    buttons[row][col].setText("O");
                    return;
                }
            }
        }
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
//risoluzione caso di pareggio
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

  
   

   

   
}