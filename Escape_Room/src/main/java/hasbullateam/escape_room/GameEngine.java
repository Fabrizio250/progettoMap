/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room;

import java.awt.Color;
import java.util.function.Consumer;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class GameEngine{
    
    public GameEngine( Consumer<JPanel> setPanel ){
        JPanel lol = new JPanel();
        lol.setBackground(Color.red);
        
        setPanel.accept( lol );
        
    }
    

}
