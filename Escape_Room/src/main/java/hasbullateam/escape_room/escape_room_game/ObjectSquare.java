
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import org.json.JSONObject;

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
    
    public ObjectSquare( JSONObject jsonObj ){
        this(
                jsonObj.getString("name"),
                new Cord(jsonObj.getJSONObject("position").getInt("x"),
                jsonObj.getJSONObject("position").getInt("y")),
                jsonObj.getString("pathImage"),
                jsonObj.getBoolean("isPassable")
        );
    }
    
}
