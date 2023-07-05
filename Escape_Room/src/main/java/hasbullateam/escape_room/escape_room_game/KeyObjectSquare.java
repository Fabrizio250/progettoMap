
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class KeyObjectSquare extends CollectableObjectSquare implements Serializable{
    
    public KeyObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(0,0),
            jsonObj.getString("pathImage")
        );
    }
    
    public KeyObjectSquare(String name, Cord position, String pathImage){
        super(name, position, pathImage);

    }
    
    
}
