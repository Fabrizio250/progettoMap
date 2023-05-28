
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuse
 */
public class Room {
    String backGroundPath;
    Map<Cord,ObjectSquare> objects;
    Cord playerStarPosition;
    String playerPathImageUP;
    String playerPathImageDOWN;
    String playerPathImageLEFT;
    String playerPathImageRIGHT;
    Direction playerStartDirection;

    public Room( String backGroundPath, String playerPathImage ) {
        this(backGroundPath, new Cord(0,0), playerPathImage);
    }
    
    public Room(String backGroundPath, Cord playerStartPosition, String playerPathImage){
        this(backGroundPath, playerStartPosition, playerPathImage, 
                playerPathImage, playerPathImage, playerPathImage, Direction.UP);
    }
    
    public Room(String backGroundPath, Cord playerStartPosition,
    String playerPathImageUP, String playerPathImageDOWN, String playerPathImageLEFT, String playerPathImageRIGHT, Direction playerStartDirection){
        this.backGroundPath = backGroundPath;
        this.playerStarPosition = playerStartPosition;
        this.playerPathImageUP = playerPathImageUP;
        this.playerPathImageDOWN = playerPathImageDOWN;
        this.playerPathImageLEFT = playerPathImageLEFT;
        this.playerPathImageRIGHT = playerPathImageRIGHT;
        this.playerStartDirection = playerStartDirection;
        
        objects = new HashMap<>();
    }
    
    
    public void addObject(ObjectSquare obj){
        objects.put(obj.position, obj);
    }
    
    public ObjectSquare getObject(Cord cord){
        return this.objects.get(cord);
    }
    
    public boolean containsObject(Cord cord){
        return this.objects.containsKey(cord);
    }
 
}
