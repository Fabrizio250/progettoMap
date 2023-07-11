
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.BattleShip;
import hasbullateam.escape_room.type.Cord;
import java.io.Serializable;
import javax.swing.JPanel;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class BossObjectSquare extends ObjectSquare implements Serializable{
    String minigameTag;
    String entryMessage;
    String defeatMessage;
    String loserMessage;
    public Boolean defeated;
    Cord defeatPosition;
    
    public BossObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        
        this.minigameTag = jsonObj.getString("minigame");
        this.entryMessage = jsonObj.getString("entry_message");
        this.defeatMessage = jsonObj.getString("defeat_message");
        this.loserMessage = jsonObj.getString("loser_message");
        this.defeatPosition = new Cord(
                jsonObj.getJSONObject("defeat_position").getInt("x"),
                jsonObj.getJSONObject("defeat_position").getInt("y")
        );
        this.defeated = false;
        
    }
    
    
    
    public JPanel getMinigame( JPanel previousPanel ){
        
        switch(minigameTag){
            
            case "BattleShip" -> {
                return new BattleShip(previousPanel, this);
            }
            
            case "Tris" -> {
                //return new Tris(previousPanel);
                break;
            }
            
            case "Snake" -> {
                //return new Snake(previousPanel);
                break;
            }
            
            
        }
        return null;
        
    }
    
    public Boolean isDefeated(){
        return this.defeated;
    }
    
    
    
}
