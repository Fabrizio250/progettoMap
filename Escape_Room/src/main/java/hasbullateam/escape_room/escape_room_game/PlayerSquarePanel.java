
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author giuse
 */
public class PlayerSquarePanel extends SquarePanel{
    SquarePanel occupiedSquare;

    public PlayerSquarePanel(Cord position, String pathImage) {
        super(position, pathImage);
        //this.setLayout( new BorderLayout() );
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
