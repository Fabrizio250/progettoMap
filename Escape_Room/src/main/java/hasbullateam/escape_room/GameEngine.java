/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class GameEngine{
    JPanel mainPanel;
    
    public GameEngine(JPanel jPanel){
        this.mainPanel = jPanel;
        mainPanel.setBackground(Color.red);
        refresh();
        
    }
    
    private void refresh(){
        mainPanel.revalidate();
    }
}
