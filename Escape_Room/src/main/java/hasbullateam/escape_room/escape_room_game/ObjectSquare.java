
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;

/**
 *
 * @author giuse
 */
public class ObjectSquare {
    Cord position;
    String pathImage;
    String name;
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
    
}
