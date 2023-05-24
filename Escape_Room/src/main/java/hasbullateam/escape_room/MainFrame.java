
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
    
    public MainFrame(){
        setTitle("Escape Room");
        setSize(SIZE,SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        
        new GameEngine( (JPanel p) -> {
            setContentPane(p);
            revalidate();
        }  );
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {new MainFrame();} );

    }
}
