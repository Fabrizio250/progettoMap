
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class CollectableObjectSquare extends ObjectSquare implements Serializable, Cloneable{

    public CollectableObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
    }
    
    public CollectableObjectSquare(String name, Cord position, String pathImage){
        super(name, position, pathImage, false, true);

    }
    
    
    @Override
    public CollectableObjectSquare clone(){
        return new CollectableObjectSquare( this.name, this.position, this.pathImage);
    }
    
}
