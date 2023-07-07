
package hasbullateam.escape_room;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MorraCinese extends JPanel implements ActionListener {

    private final JButton jButtonRock, jButtonPaper, jButtonScissors;
    private final JLabel player1Label, player2Label, player1WinsLabel, player2WinsLabel , turnLabel, computerWinsLabel;
    private final JLabel vsLabel;
    private int player1Wins, player2Wins,modalitaScelta;
    private int sceltaPlayer1, sceltaPlayer2; //memorizzano dei giocatori 1 e 2
    private Random random;

    private final ImageIcon rockIcon = new ImageIcon("images\\sasso.png");
    private final ImageIcon paperIcon = new ImageIcon("images\\carta.png");
    private final ImageIcon scissorsIcon = new ImageIcon("images\\forbici.png");
    private final ImageIcon questionMarkIcon = new ImageIcon("images\\puntoInterrogativo.png");

    private final int WINNING_SCORE = 3;

    public MorraCinese(int modalitaScelta) {
        super(new GridLayout(3, 1));
        this.modalitaScelta = modalitaScelta;
        setPreferredSize(new Dimension(800, 800));
        random = new Random();

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setPreferredSize(new Dimension(800, 150));
        rockIcon.setImage(rockIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        paperIcon.setImage(paperIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        scissorsIcon.setImage(scissorsIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        questionMarkIcon.setImage(questionMarkIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));


        jButtonRock = new JButton();
        jButtonPaper = new JButton();
        jButtonScissors = new JButton();


        jButtonRock.setIcon(rockIcon);
        jButtonPaper.setIcon(paperIcon);
        jButtonScissors.setIcon(scissorsIcon);

        buttonPanel.add(jButtonRock);
        buttonPanel.add(jButtonPaper);
        buttonPanel.add(jButtonScissors);

        JPanel scorePanel = new JPanel(new GridLayout(1, 3));
        scorePanel.setPreferredSize(new Dimension(800, 200));

        player1WinsLabel = new JLabel("Giocatore 1: 0");
        setFont(player1WinsLabel);
        player2WinsLabel = new JLabel("Giocatore 2: 0");
        setFont(player2WinsLabel);
        computerWinsLabel = new JLabel("Computer: 0");
        setFont(computerWinsLabel);
        turnLabel = new JLabel();
        turnLabel.setText("TURNO GIOCATORE 1");
        setFont(turnLabel);
        
        if (modalitaScelta == 1) {
            scorePanel.add(player1WinsLabel);
            scorePanel.add(turnLabel);
            scorePanel.add(player2WinsLabel);
        }

        if (modalitaScelta == 2) {
            scorePanel.add(player1WinsLabel);
            scorePanel.add(turnLabel);
            scorePanel.add(computerWinsLabel);
        }

        JPanel playerPanel = new JPanel(new GridLayout(1, 3));
        playerPanel.setPreferredSize(new Dimension(800, 200));

        player1Label = new JLabel();
        player2Label = new JLabel();


        vsLabel = new JLabel("vs");
        setFont(vsLabel);

        playerPanel.add(player1Label);
        playerPanel.add(vsLabel);
        playerPanel.add(player2Label);


        add(scorePanel);
        add(playerPanel);
        add(buttonPanel);

        player1Wins = 0;
        player2Wins = 0;
        sceltaPlayer1 = 0;
        sceltaPlayer2 = 0;

        jButtonRock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modalitaScelta == 1) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO GIOCATORE 2");
                        sceltaPlayer1 = 1;
                    } else if (sceltaPlayer2 == 0) {
                        player2Label.setIcon(questionMarkIcon);
                        sceltaPlayer2 = 1;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                } else if (modalitaScelta == 2) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO COMPUTER");
                        sceltaPlayer1 = 1;
                        player2Label.setIcon(questionMarkIcon);
                        int computerChoice = random.nextInt(3) + 1;
                        sceltaPlayer2 = computerChoice;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                }
            }
        });

        jButtonPaper.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                if (modalitaScelta == 1) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO GIOCATORE 2");
                        sceltaPlayer1 = 2;
                    } else if (sceltaPlayer2 == 0) {
                        player2Label.setIcon(questionMarkIcon);
                        sceltaPlayer2 = 2;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                } else if (modalitaScelta == 2) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO COMPUTER");
                        sceltaPlayer1 = 2;
                          player2Label.setIcon(questionMarkIcon);
                        int computerChoice = random.nextInt(3) + 1;
                        sceltaPlayer2 = computerChoice;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                }
            }
        });

        jButtonScissors.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                if (modalitaScelta == 1) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO GIOCATORE 2");
                        sceltaPlayer1 = 3;
                    } else if (sceltaPlayer2 == 0) {
                        player2Label.setIcon(questionMarkIcon);
                        sceltaPlayer2 = 3;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                } else if (modalitaScelta == 2) {
                    if (sceltaPlayer1 == 0) {
                        player1Label.setIcon(questionMarkIcon);
                        turnLabel.setText("TURNO COMPUTER");
                        sceltaPlayer1 = 3;
                          player2Label.setIcon(questionMarkIcon);
                        int computerChoice = random.nextInt(3) + 1;
                        sceltaPlayer2 = computerChoice;
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                checkWin();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                }
            }
        });
    }


    private void checkWin() {
        if (sceltaPlayer1 == sceltaPlayer2) {
            showchoice(getIconByChoice(sceltaPlayer1), getIconByChoice(sceltaPlayer2));
            JOptionPane.showMessageDialog(this, "pareggio");
            resetGame();
        }
        if (sceltaPlayer1 == 1 && sceltaPlayer2 == 2) {
            showchoice(getIconByChoice(1), getIconByChoice(2));
            player2Wins++;
            if (modalitaScelta == 1) {
                 JOptionPane.showMessageDialog(this, "vittoria Giocatore 2");
                player2WinsLabel.setText("Giocatore 2: " + player2Wins);
            } else if (modalitaScelta == 2) {
                 JOptionPane.showMessageDialog(this, "vittoria Computer");
                computerWinsLabel.setText("Computer: " + player2Wins);
            }
            resetGame();
        }
        if (sceltaPlayer1 == 1 && sceltaPlayer2 == 3) {
            showchoice(getIconByChoice(1), getIconByChoice(3));
            JOptionPane.showMessageDialog(this, "vittoria Giocatore 1");
            player1Wins++;
            player1WinsLabel.setText("Giocatore 1: " + player1Wins);
            resetGame();
        }
        if (sceltaPlayer1 == 2 && sceltaPlayer2 == 1) {
            showchoice(getIconByChoice(2), getIconByChoice(1));
            JOptionPane.showMessageDialog(this, "vittoria Giocatore 1");
            player1Wins++;
            player1WinsLabel.setText("Giocatore 1: " + player1Wins);
            resetGame();
        }
        if (sceltaPlayer1 == 2 && sceltaPlayer2 == 3) {
            showchoice(getIconByChoice(2), getIconByChoice(3));
            player2Wins++;
            if (modalitaScelta == 1) {
                 JOptionPane.showMessageDialog(this, "vittoria Giocatore 2");
                player2WinsLabel.setText("Giocatore 2: " + player2Wins);
            } else if (modalitaScelta == 2) {
                 JOptionPane.showMessageDialog(this, "vittoria Computer");
                computerWinsLabel.setText("Computer: " + player2Wins);
            }
            resetGame();
        }
        if (sceltaPlayer1 == 3 && sceltaPlayer2 == 1) {
            showchoice(getIconByChoice(3), getIconByChoice(1));
            player2Wins++;
            if (modalitaScelta == 1) {
                 JOptionPane.showMessageDialog(this, "vittoria Giocatore 2");
                player2WinsLabel.setText("Giocatore 2: " + player2Wins);
            } else if (modalitaScelta == 2) {
                 JOptionPane.showMessageDialog(this, "vittoria Computer");
                computerWinsLabel.setText("Computer: " + player2Wins);
            }
            resetGame();
        }
        if (sceltaPlayer1 == 3 && sceltaPlayer2 == 2) {
            showchoice(getIconByChoice(3), getIconByChoice(2));
            JOptionPane.showMessageDialog(this, "vittoria Giocatore 1");
            player1Wins++;
            player1WinsLabel.setText("Giocatore 1: " + player1Wins);
            resetGame();
        }
        if (player1Wins == WINNING_SCORE) {
            JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 1! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if (player2Wins == WINNING_SCORE) {
            if (modalitaScelta == 1) {
                JOptionPane.showMessageDialog(this, "Vittoria del Giocatore 2! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
            } else if (modalitaScelta == 2) {
                JOptionPane.showMessageDialog(this, "Vittoria del Computer! Gioco terminato.", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }
    }

    private void resetGame() {
        player1Label.setIcon(null);
        player2Label.setIcon(null);
        turnLabel.setText("TURNO GIOCATORE 1");
        sceltaPlayer1 = 0;
        sceltaPlayer2 = 0;
    }

    private void showchoice(ImageIcon icon1, ImageIcon icon2) {
        player1Label.setIcon(icon1);
        player2Label.setIcon(icon2);
    }

    private void setFont(JLabel label) {
        Font font = label.getFont();
        Font newFont = font.deriveFont(Font.BOLD, 24f);
        label.setFont(newFont);
    }

    private ImageIcon getIconByChoice(int choice) {
        switch (choice) {
            case 1:
                return rockIcon;
            case 2:
                return paperIcon;
            case 3:
                return scissorsIcon;
            default:
                return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}