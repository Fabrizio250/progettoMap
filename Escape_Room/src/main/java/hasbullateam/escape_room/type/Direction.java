
package hasbullateam.escape_room.type;

import java.io.Serializable;

/**
 *
 * @author giuse
 */
public enum Direction implements Serializable{
    NONE,
    UP,
    DOWN,
    LEFT,
    RIGHT;
    
    public static Direction fromString(String str){
        Direction dir = NONE;
        
        if(str.equals("UP")){
            dir = UP;
        }else if(str.equals("DOWN")){
            dir = DOWN;
        }else if(str.equals("LEFT")){
            dir = LEFT;
        }else if(str.equals("RIGHT")){
            dir = RIGHT;
        }
        
        return dir;
    }
}
