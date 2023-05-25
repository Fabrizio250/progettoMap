
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;

/**
 *
 * @author giuse
 */
public class PlayerSquarePanel extends SquarePanel{
    SquarePanel occupiedSquare;
    
    public PlayerSquarePanel(Cord initialPosition){
        super(initialPosition, new Color(200, 200, 200));
    }
    
    
    public void setOccupiedSquare( SquarePanel square ){
        this.occupiedSquare = square.clone();
    }
    
}
