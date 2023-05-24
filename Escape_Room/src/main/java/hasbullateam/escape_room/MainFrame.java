
package hasbullateam.escape_room;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */
public class MainFrame extends JFrame{
    final int SIZE = 800;
    JPanel mainPanel = new JPanel();
    
    public MainFrame(){
        setTitle("Escape Room");
        setSize(SIZE,SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        setContentPane(mainPanel);
        setVisible(true);
        
        new GameEngine(mainPanel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {new MainFrame();} );

    }
}
