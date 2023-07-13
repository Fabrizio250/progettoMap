
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class NumericKeypadObjectSquare extends ObjectSquare {
    String goalString;
    String successMessage;
    String failMessage;
    Cord doorPosition;
    
    public NumericKeypadObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        this.goalString = jsonObj.getString("goal_string");
        this.failMessage = jsonObj.getString("fail_message");
        this.successMessage = jsonObj.getString("success_message");
        this.doorPosition = new Cord(jsonObj.getJSONObject("door_position").getInt("x"),
                     jsonObj.getJSONObject("door_position").getInt("y"));
    }
    
}
