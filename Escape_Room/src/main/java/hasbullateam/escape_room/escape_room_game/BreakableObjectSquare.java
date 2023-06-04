
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class BreakableObjectSquare extends ObjectSquare {
    
    List<String> brokeStage_pathImages;
    List<String> brokeStage_message;
    Boolean isBroken = false;
    Integer brokeStage_counter = 0;
    String breakableWith_objName;
    String message;
    
    public BreakableObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        
        this.breakableWith_objName = jsonObj.getString("breakable_with_obj_name");
        this.message = jsonObj.getString("message");
        
        this.brokeStage_pathImages = new ArrayList<>();
        this.brokeStage_pathImages.add(this.pathImage);
        
        this.brokeStage_message = new ArrayList<>();
        
        JSONArray objArray = jsonObj.getJSONArray("broke_stage");

        for(int i=0; i<objArray.length(); i++){
            this.brokeStage_pathImages.add(objArray.getJSONObject(i).getString("path_image"));
            this.brokeStage_message.add(objArray.getJSONObject(i).getString("message"));
        }
        
    }
    
    public void broke(){
        if(!isBroken){
            brokeStage_counter++;
            this.pathImage = brokeStage_pathImages.get(brokeStage_counter);
            
            if(brokeStage_counter == brokeStage_pathImages.size()-1){
                this.isBroken = true;
                this.isInteractable = false;
                this.isPassable = true;
            }
        }
    }
    
    public String getBrokeMessage(){
        return this.brokeStage_message.get(this.brokeStage_counter-1);
    }
    
}
