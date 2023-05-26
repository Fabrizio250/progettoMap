
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class SquarePanel extends JPanel implements Cloneable{
    
    Cord position;
    ImageManager backgroundImage;
    
    public SquarePanel(Cord position, Color color){
        super();
        this.setBackground(color);
        this.position = position;
        
    }
    
    public SquarePanel(Cord position){
        super();
        this.position = position;
        this.setBackground( new Color(0,0,0,0) );
        
    } 
    
    public SquarePanel(Cord position, String pathImage){
        this(position);
        backgroundImage = new ImageManager(pathImage);
    }
    
    public SquarePanel(Cord position, Color color, String pathImage){
        this(position, color);
        backgroundImage = new ImageManager(pathImage);
        
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
