
package hasbullateam.escape_room.escape_room_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.util.prefs.BackingStoreException;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */
public class TextDialog {
    
    JPanel parent;
    JFrame frame;
    String text;
    JDialog dialog;
    JLabel label;
    
    static final Color backGroundColor = new Color(0,0,255,128);

    public TextDialog(JPanel parent) {
        
        this.parent = parent;
        
        // fixa la posizione al centro del frame
        SwingUtilities.invokeLater(()->{ 
            this.frame = (JFrame) parent.getTopLevelAncestor();
            this.frame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    if(TextDialog.this.dialog != null){
                        TextDialog.this.dialog.setLocationRelativeTo(TextDialog.this.parent);
                    }
                }
            }); 
            this.setText();
        });

    }
    
    public void setText(){
        dialog = new JDialog(this.frame, false);
        dialog.setSize(200, 200);
        this.text = "";
        label = new JLabel(String.format("<html><body style='width: %dpx'>%s</body></html>",this.dialog.getWidth()-50,this.text));
        
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0,0,255,128));
        label.setForeground(Color.RED); 
        dialog.add(label);
        
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
    }
    
    public void changeText(String str){
        this.text = str;
        label.setText(String.format("<html><body style='width: %dpx'>%s</body></html>",this.dialog.getWidth()-50,this.text));
    }
    
    
}
