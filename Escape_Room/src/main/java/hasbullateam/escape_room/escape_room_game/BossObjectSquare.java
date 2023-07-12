
package hasbullateam.escape_room.escape_room_game;


import minigame.ping_pong.PingPongPanel;
import hasbullateam.escape_room.type.BossStatus;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.GameMode;
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
    Cord defeatPosition;
    public BossStatus bossStatus;
    
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
        this.bossStatus = BossStatus.NOT_IN_GAME; 
        
    }
    
    
    
    public JPanel getMinigame( JPanel previousPanel ){
        
        switch(minigameTag){
            
            case "Ping_Pong" -> {
                return new PingPongPanel(previousPanel, GameMode.MODE_STORIA, this);
            }
            
            
        }
        return null;
        
    }
    
    
    
    
}
