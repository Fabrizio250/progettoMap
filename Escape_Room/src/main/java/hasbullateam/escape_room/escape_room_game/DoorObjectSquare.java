
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class DoorObjectSquare extends ObjectSquare implements Serializable{
    String message;
    Boolean isExit;
    Boolean isOpen;
    String openDoorPathImage;
    String closedDoorPathImage;
    
    public DoorObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        this.message = jsonObj.getString("message");
        this.isExit = jsonObj.getBoolean("isExit");
        this.isOpen = jsonObj.getBoolean("isOpen");
        
        if(this.isOpen){ // se la porta è chiusa deve avere il path della porta aperta
            this.openDoorPathImage = this.pathImage;
            this.closedDoorPathImage = jsonObj.getString("closed_door_path_image");
        }else{
            this.closedDoorPathImage = this.pathImage;
            this.openDoorPathImage = jsonObj.getString("open_door_path_image");

        }
    
    }
    
    public void open(){
        this.isOpen = true;
        this.pathImage = this.openDoorPathImage;
    }
    
    public void close(){
        this.isOpen = false;
        this.pathImage = this.closedDoorPathImage;
    }
    
    
    
    
}
