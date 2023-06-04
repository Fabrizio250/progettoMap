
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.BattleShip;
import hasbullateam.escape_room.PingPong;
import hasbullateam.escape_room.Tris;
import hasbullateam.escape_room.type.Cord;
import java.io.Serializable;
import javax.swing.JPanel;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class BossObjectSquare extends ObjectSquare implements Serializable{
    
    JPanel minigame;
    String minigameTag;
    String message;
    
    public BossObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
            jsonObj.getString("pathImage"),
            false, true
        );
        
        this.minigameTag = jsonObj.getString("minigame");
        this.message = jsonObj.getString("message");
        
    }
    
    
    
    public JPanel getMinigame( JPanel previousPanel ){
        
        switch(minigameTag){
            
            case "BattleShip" -> {
                return new BattleShip(previousPanel);
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
    
    
    
}
