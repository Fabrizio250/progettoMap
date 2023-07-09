package PingPong;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GamePanel gamePanel;

    public GameFrame () {
        gamePanel = new GamePanel(2);
        this.add(gamePanel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();    //Il nostro JFrame si adattera di conseguenza alle dimensioni di cui abbiamo bisogno
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
