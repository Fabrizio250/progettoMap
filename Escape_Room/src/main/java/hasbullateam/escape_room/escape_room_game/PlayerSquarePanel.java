
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author giuse
 */
public class PlayerSquarePanel extends SquarePanel{
    SquarePanel occupiedSquare;
    Direction direction;
    Map<Direction, String> directionImages;
    
    public static enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
    
    public PlayerSquarePanel(Cord position, String pathImageUP, 
            String pathImageDOWN, String pathImageLEFT, String pathImageRIGHT, Direction defaultDir){
        super(position);
        this.directionImages = new HashMap(4);
        this.directionImages.put(Direction.UP, pathImageUP);
        this.directionImages.put(Direction.DOWN, pathImageDOWN);
        this.directionImages.put(Direction.LEFT, pathImageLEFT);
        this.directionImages.put(Direction.RIGHT, pathImageRIGHT);
        
        this.setBackgroundImage(this.directionImages.get(defaultDir));
    }

    public PlayerSquarePanel(Cord position, String pathImage) {
        super(position, pathImage);
        
    }
    
    public void setOccupiedSquare( SquarePanel square ){
        this.occupiedSquare = square.clone();
        this.setBackground(this.occupiedSquare.getBackground());
        
    }
    
    @Override
    protected void paintComponent(Graphics g){
        
        if( this.occupiedSquare != null ){
            if(this.occupiedSquare.backgroundImage != null){
                this.occupiedSquare.backgroundImage.resizeOnFirstDrawn(this.getWidth(), this.getHeight());
                g.drawImage(this.occupiedSquare.backgroundImage.getImage(), 0, 0, null);
            }
        }
        super.paintComponent(g);
    }
    
}
