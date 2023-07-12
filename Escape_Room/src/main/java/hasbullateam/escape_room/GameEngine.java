
package hasbullateam.escape_room;


import hasbullateam.escape_room.escape_room_game.EscapeRoomGame;
import minigame.ping_pong.PingPongPanel;
import hasbullateam.escape_room.type.GameMode;

import javax.swing.*;
import java.util.function.Consumer;

/**
 *
 * @author giuse
 */
public class GameEngine{
    
    public GameEngine( Consumer<JPanel> setPanel ){

        

        
        TabbedMenu menu = new TabbedMenu();
        
        menu.setGotoEscapeRoom( () -> { setPanel.accept(new EscapeRoomGame(menu)); }  );
        menu.setGotoPingPong1vCPU( () -> { setPanel.accept(new PingPongPanel(menu,GameMode.MODE_1vCPU));}  );
        menu.setGotoPingPong1v1( () -> { setPanel.accept(new PingPongPanel(menu,GameMode.MODE_1v1));}  );
        //menu.setGotoMorraCinese(() -> { setPanel.accept(new BattleShip()); }  );
        //menu.setGotoTris(       () -> { setPanel.accept(new Tris()); }        );
        //menu.setGotoPingPong(   () -> { setPanel.accept(new PingPongPanel()); }    );
        
        
        setPanel.accept(menu);
        
    }
    

}
