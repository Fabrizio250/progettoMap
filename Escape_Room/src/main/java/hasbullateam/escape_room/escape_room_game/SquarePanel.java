
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class SquarePanel extends JPanel implements Cloneable{
    
    Cord position;
    
    public SquarePanel(Cord position, Color color){
        super();
        this.setBackground(color);
        this.position = position;
    }

    @Override
    public SquarePanel clone(){
        return new SquarePanel(this.position, this.getBackground() );
    }

}
