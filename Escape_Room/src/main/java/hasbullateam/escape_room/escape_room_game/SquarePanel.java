
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.BackingStoreException;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class SquarePanel extends JPanel implements Cloneable{
    
    Cord position;
    ImageManager backgroundImage;
    
    public SquarePanel(Cord position, Color color){
        this(position, color, null);
    }
    
    public SquarePanel(Cord position){
        this(position, null, null);
    } 
    
    public SquarePanel(Cord position, String pathImage){
        this(position, null, pathImage);
    }
    
    public SquarePanel(Cord position, Color color, String pathImage){
        super();
        this.position = position;
        
        if(color != null){
            this.setBackground(color);
        }else{
            this.setBackground( new Color(0,0,0,0) );
        }
        
        if(pathImage != null){
            backgroundImage = new ImageManager(pathImage);
        }
    }
    
    public void setBackgroundImage(String pathImage){
        this.backgroundImage = new ImageManager(pathImage);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(this.backgroundImage != null){
            this.backgroundImage.resizeOnFirstDrawn(this.getWidth(), this.getHeight());
            g.drawImage(backgroundImage.getImage(), 0, 0, null);
        }   
    }

    @Override
    public SquarePanel clone(){
        if(this.backgroundImage == null){
            return new SquarePanel(this.position, this.getBackground());
        }else{
            return new SquarePanel(this.position, this.getBackground(), this.backgroundImage.pathImage );
        }
    }
    

}
