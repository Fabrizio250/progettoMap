
package hasbullateam.escape_room.escape_room_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author giuse
 */
public class NumericKeypadDialog {
    JPanel parent;
    JDialog dialog;
    JPanel keypadPanel;
    JFrame frame;
    Integer maxDigit;
    String goalNumberString;
    Boolean exit;
    Boolean goalReached;
    
    
    
    public NumericKeypadDialog(JPanel parent, String goalNumberString) {
        this.parent = parent;
        this.goalNumberString = goalNumberString;
        this.maxDigit = goalNumberString.length();
        this.exit = false;
        this.goalReached = false;
        
        this.frame = (JFrame) parent.getTopLevelAncestor();
        
        dialog = new JDialog(this.frame, true);
        this.dialog.setSize(600,600);
        

        this.keypadPanel = new NumericKeypadPanel(maxDigit, 
            (String str) -> {
            
                this.goalReached = str.equals(this.goalNumberString);
                this.exit = true;
                this.dialog.dispose();
            }
        );
        
        
        this.dialog.add(this.keypadPanel);
        this.dialog.setLocationRelativeTo(this.frame);
        
        this.dialog.setVisible(true);
        this.dialog.setResizable(false);

        this.dialog.revalidate();
        this.dialog.repaint();
        
        
        
    }
    
    public boolean isExit(){
        return this.exit;
    }
    
    private class NumericKeypadPanel extends JPanel{
        JPanel keyPanel;
        JTextField numberField;
        Integer maxDigit;
        
        String numericString;
        String[] buttonLabels = {
                    "1", "2", "3",
                    "4", "5", "6",
                    "7", "8", "9",
                    "0", "Clear", "Enter"
                    };
        
        public NumericKeypadPanel(Integer maxDigit, Consumer<String> onEnter) {
            super(new BorderLayout());
            this.setVisible(true);
            this.maxDigit = maxDigit;
            this.numericString = "";
            
            this.numberField = new JTextField();
            this.numberField.setEditable(false);
            this.add( this.numberField, BorderLayout.NORTH);
            
            this.keyPanel = new JPanel( new GridLayout(4,3) );
            this.add(keyPanel, BorderLayout.CENTER);
            
            for( String label : this.buttonLabels ) {
                JButton button = new JButton(label);
                this.keyPanel.add( button );
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String btnPressed = button.getText();
                        
                        if(btnPressed.equals(buttonLabels[10])){ // Clear
                            NumericKeypadPanel.this.numericString = "";
                            NumericKeypadPanel.this.numberField.setText("");
                            
                        }else if(btnPressed.equals(buttonLabels[11])){ // Enter
                            onEnter.accept(NumericKeypadPanel.this.numericString);
                            
                        }else if( NumericKeypadPanel.this.numberField.getText().length() 
                                < NumericKeypadPanel.this.maxDigit ){
                            
                            NumericKeypadPanel.this.numericString += btnPressed; 
                            NumericKeypadPanel.this.numberField.setText( NumericKeypadPanel.this.numericString );
                        }
                        
                        
                    }
                });
                
                
            }
            
        }
        
        
    }
    
   
    
    
}
