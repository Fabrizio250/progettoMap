
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.Image;
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
    }
    
    public void setOccupiedSquare( SquarePanel square ){
        this.occupiedSquare = square.clone();
    }
    
}
