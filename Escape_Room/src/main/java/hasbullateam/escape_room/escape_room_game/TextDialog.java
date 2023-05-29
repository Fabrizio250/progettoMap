
package hasbullateam.escape_room.escape_room_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    Thread _writeTextThread;
    
    
    static final Color BACKGROUNDCOLOR = new Color(40,200,255,208);
    static final Color TEXTCOLOR = Color.DARK_GRAY;

    public TextDialog(JPanel parent) {
        
        this.parent = parent;
        this.text = "";
        
        
        SwingUtilities.invokeLater(()->{ 
            this.frame = (JFrame) parent.getTopLevelAncestor();
            // fixa la posizione al centro del frame quando si muove
            this.frame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    if(TextDialog.this.dialog != null){
                        TextDialog.this.dialog.setLocationRelativeTo(TextDialog.this.parent);
                    }
                }
            }); 
            dialog = new JDialog(this.frame, false);
            dialog.setSize(500, 200);
            
            dialog.setUndecorated(true);
            dialog.setBackground(this.BACKGROUNDCOLOR); 
            dialog.setLocationRelativeTo(this.parent);
            dialog.setFocusableWindowState(false);
            
            label = new JLabel();
            setText(this.text);
            label.setForeground(this.TEXTCOLOR); 
            dialog.add(label);
            
        });

    }
    
    public void setText(String str){
        
        if( (_writeTextThread == null) || (!_writeTextThread.isAlive())  ){
            
            _writeTextThread = new Thread(
                ()->{
                    try{
                        this.text = "";
                        for(char c: str.toCharArray()){
                            this.text += c;

                            setLabelText(this.text);
                            
                            Thread.sleep(5);
                        }
                    
                    }catch (InterruptedException e){
                        this.text = "";
                        setLabelText(this.text);
                    }
                }
            );
            
            _writeTextThread.start();
           
            
        }else{
            _writeTextThread.interrupt();
            System.out.println("interrotto");
        }
        
    }
    
    private void setLabelText(String str){
        label.setText(String.format("<html><body style='width: %dpx'>%s</body></html>",
                            this.dialog.getWidth()-50,str));
    }
    
    public void show( boolean  val){
        this.dialog.setVisible(val);
    }
    
    public boolean isVisible(){
        return this.dialog.isVisible();
    }
    
    
}
