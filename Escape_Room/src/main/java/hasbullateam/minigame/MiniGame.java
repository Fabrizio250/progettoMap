
package hasbullateam.minigame;

import hasbullateam.escape_room.escape_room_game.BossObjectSquare;
import hasbullateam.escape_room.escape_room_game.EscapeRoom;
import hasbullateam.escape_room.type.GameMode;
import hasbullateam.escape_room.type.Result;

import javax.swing.*;

/**
 *
 * @author giuse
 */
public abstract class MiniGame extends JPanel {
    protected JPanel parentPanel;
    protected GameMode gameMode;
    protected BossObjectSquare bossObj;

    protected Result result;

    public MiniGame(JPanel parentPanel, GameMode gameMode, BossObjectSquare bossObj){
        this.parentPanel = parentPanel;
        this.gameMode = gameMode;
        this.bossObj = bossObj;
    }
    
    public MiniGame(JPanel parentPanel, GameMode gameMode){
        this(parentPanel, gameMode, null);
    }
    
    public void changeToParentPanel(){
        JFrame frame = (JFrame) this.getTopLevelAncestor();
        if(frame != null){

            frame.setContentPane(parentPanel);
            if(gameMode == GameMode.MODE_STORIA){
                frame.setSize(EscapeRoom.WINDOW_SIZE, EscapeRoom.WINDOW_SIZE);
            }else{
                frame.pack();
            }

            frame.revalidate();
            frame.repaint();
              
        }else{
            System.out.println("frame è nullll");
        }
            
    }
}
