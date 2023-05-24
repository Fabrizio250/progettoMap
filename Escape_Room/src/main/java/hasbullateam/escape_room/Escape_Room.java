/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package hasbullateam.escape_room;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Image;
import javax.swing.*;

/**
 *
 * @author giuse
 */
public class Escape_Room extends JFrame{
    
    JPanel redPanel;
    JPanel bluPanel;
    
    JPanel gridPanel;
    JPanel menu;
    
    JDialog dialog;
    
    private class MyKeyListener implements KeyListener {
        
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            
            if( keyCode == 82 ){ // r
                bluPanel.setBackground(Color.pink);
                
                ImageIcon gabbiano = new ImageIcon("images\\gabbiano.jpg");
                
                Image _gabbiano = gabbiano.getImage().getScaledInstance(redPanel.getWidth(), redPanel.getHeight(), Image.SCALE_SMOOTH);
                
                gabbiano = new ImageIcon(_gabbiano);
                JLabel label = new JLabel(gabbiano);
                
                label.setSize( redPanel.getSize() );
                
                redPanel.add(  label);
                revalidate();
            }
            if( keyCode == 83){ // s
                
                setContentPane(menu);
                revalidate();
            }
            
            if (keyCode == 84){ // t
                setContentPane(gridPanel);
                revalidate();
            }
            
            if (keyCode == 85){ // u
                dialog.setVisible(false);
                revalidate();
            }
            
            if (keyCode == 86){ // v
                
                Dimension frameSize = getSize();
                Dimension dialogSize = dialog.getSize();
                int x = getLocation().x + (frameSize.width - dialogSize.width) / 2;
                int y = getLocation().y + (frameSize.height - dialogSize.height) / 2;
                dialog.setLocation(x, y);
                
                dialog.setVisible(true);
                revalidate();
            }
            
            System.out.println("Tasto premuto: " + keyCode);
        }
        
        @Override
        public void keyReleased(KeyEvent e) {

        }
        
        @Override
        public void keyTyped(KeyEvent e) {

        }
    }
    
    public Escape_Room() {
            
        setTitle("Lol");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menu = new NewJPanel();
        
        dialog = new JDialog(this, "ZIOPERA", true);
        dialog.add(new JLabel("asgarra graffy!!!"));
        dialog.setBounds(100, 100, 300, 200);        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        


        gridPanel = new JPanel(new GridLayout(2, 2));

        redPanel = new JPanel();
        redPanel.setBackground(Color.red);
        gridPanel.add(redPanel);


        JPanel greenPanel = new JPanel();
        greenPanel.setBackground(Color.green);
        gridPanel.add(greenPanel);


        bluPanel = new JPanel();
        bluPanel.setBackground(Color.blue);
        gridPanel.add(bluPanel);


        JPanel yellowPanel = new JPanel();
        yellowPanel.setBackground(Color.yellow);
        gridPanel.add(yellowPanel);


        MyKeyListener keyl = new MyKeyListener();

        addKeyListener( keyl);
        setContentPane(gridPanel); 

        setFocusable(true);
        requestFocus();
        
        dialog.addKeyListener(keyl);
        dialog.setFocusable(true);
        dialog.requestFocus();
        
        
        setResizable(false);
        setVisible(true);
            
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {new Escape_Room();} );

    }
}
