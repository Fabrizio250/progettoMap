
package hasbullateam.escape_room.escape_room_game;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


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
    int width;
    int height;
    
    
    Thread _writeTextThread;
    
    
    static final Color BACKGROUNDCOLOR = new Color(40,200,255,208);
    static final Color TEXTCOLOR = Color.DARK_GRAY;
    static final int DEFAULT_WIDTH = 500;
    static final int DEFAULT_HEIGHT = 200;

    public TextDialog(JPanel parent) {
        
        this.parent = parent;
        this.text = "";

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

        dialog.setUndecorated(true);
        dialog.setBackground(this.BACKGROUNDCOLOR);
        dialog.setLocationRelativeTo(this.parent);
        dialog.setFocusableWindowState(false);

        label = new JLabel();
        label.setForeground(this.TEXTCOLOR);
        dialog.add(label);

        setText(this.text);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
                
        show(true);

    }
    
    public void reWriteText(boolean withDelay){
        if(withDelay){
           this.setText(this.text); 
        }else{
            this.setLabelText(this.text);
        }   
    }

    
    public void setSize(int width, int height){
        this.dialog.setSize(width, height);
        this.label.setSize(width, height);
        this.width = width;
        this.height = height;
        this.dialog.setLocationRelativeTo(this.parent);
        this.reWriteText(false);
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
        label.setText(String.format("<html><body style='width: %dpx'> %s </body></html>",
                            380,str));
    }
    
    public void show( boolean  val){
        this.dialog.setVisible(val);
    }
    
    public boolean isVisible(){
        return this.dialog.isVisible();
    }
    
    public void dispose(){
        this.dialog.dispose();
    }
    
}
