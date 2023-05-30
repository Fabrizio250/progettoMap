
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class ObjectSquare implements Serializable{
    Cord position;
    String pathImage;
    String name;
    Color backgroundColor;
    boolean isPassable;
    boolean isInteractable;
    
    public ObjectSquare(String name, Cord position, String pathImage, boolean isPassable, boolean isInteractable) {
        this.name = name;
        this.position = position;
        this.pathImage = pathImage;
        this.isPassable = isPassable;
        this.isInteractable = isInteractable;
    }
    
    public ObjectSquare(String name, Cord position, Color backgroundColor, String pathImage, boolean isPassable, boolean isInteractable) {
        this(name,position,pathImage,isPassable, isInteractable);
        this.backgroundColor = backgroundColor;
    }
    
    public ObjectSquare( JSONObject jsonObj ){
        //TODO: levare che bisonga per forza mettere il color
        this(
                jsonObj.getString("name"),
                new Cord(jsonObj.getJSONObject("position").getInt("x"),
                         jsonObj.getJSONObject("position").getInt("y")),
                new Color(jsonObj.getJSONObject("color").getInt("r"), 
                          jsonObj.getJSONObject("color").getInt("g"), 
                          jsonObj.getJSONObject("color").getInt("b"), 
                          jsonObj.getJSONObject("color").getInt("a")),
                jsonObj.getString("pathImage"),
                jsonObj.getBoolean("isPassable"),
                jsonObj.getBoolean("isInteractable")
        );
    }
    
    
    
    public class DoorObjectSquare extends ObjectSquare implements Serializable{
        public DoorObjectSquare(JSONObject jsonObj){
            super(jsonObj);
            
        }
    }
    
    public void setBackgroundColor( Color color ){
        this.backgroundColor = color;
    }

    
    
    
    
    
    
    
}
