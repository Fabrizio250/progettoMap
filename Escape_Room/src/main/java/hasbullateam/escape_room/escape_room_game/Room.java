
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuse
 */
public class Room {
    String backGroundPath;
    Map<String,ObjectSquare> objects;
    Cord playerStarPosition;
    String playerPathImage;

    public Room( String backGroundPath, String playerPathImage ) {
        this.backGroundPath = backGroundPath;
        this.playerPathImage = playerPathImage;
        this.playerStarPosition = new Cord(0,0);
        objects = new HashMap<>();
    }
    
    public Room(String backGroundPath, Cord playerStartPosition, String playerPathImage){
        this(backGroundPath, playerPathImage);
        this.playerStarPosition = playerStartPosition;
    }
    
    
    public void addObject(String name, ObjectSquare obj){
        objects.put(name, obj);
    }
   
}
