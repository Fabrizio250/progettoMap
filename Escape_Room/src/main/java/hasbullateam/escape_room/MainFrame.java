
package hasbullateam.escape_room;

import javax.swing.*;

/**
 *
 * @author giuse
 */
public class MainFrame extends JFrame{
    static final int SIZE = 800;
    
    public MainFrame(){
        setTitle("Escape Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setFocusable(true);
        
        new GameEngine((JPanel p) -> {
            setContentPane(p);
            pack();
            revalidate();
            repaint();
        }  );
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {new MainFrame();} );

    }
}
