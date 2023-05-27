
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;

/**
 *
 * @author giuse
 */
public class ObjectSquare {
    Cord position;
    String pathImage;
    String name;
    Color backgroundColor;
    boolean isPassable;
    /*
    interagibile
    raccoglibile
    frugabile
    
    */
    
    public ObjectSquare(String name, Cord position, String pathImage, boolean isPassable) {
        this.name = name;
        this.position = position;
        this.pathImage = pathImage;
        this.isPassable = isPassable;
    }
    
    public ObjectSquare(String name, Cord position, Color backgroundColor, String pathImage, boolean isPassable) {
        this(name,position,pathImage,isPassable);
        this.backgroundColor = backgroundColor;
    }
    
}
