
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class BrokenDoorObjectSquare extends ObjectSquare {
    String brokeDoor_message;
    String fixedDoor_message;
    String fixableWith_nameObj;
    DoorObjectSquare fixedDoor;
    
    public BrokenDoorObjectSquare(JSONObject jsonObj) {
        
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        this.brokeDoor_message = jsonObj.getString("broke_door_message");
        this.fixedDoor_message = jsonObj.getString("fixed_door_message");
        this.fixableWith_nameObj = jsonObj.getString("fixable_with_name_object");
        this.fixedDoor = new DoorObjectSquare(jsonObj.getJSONObject("fixed_door"));
    }
   
    
    
}
