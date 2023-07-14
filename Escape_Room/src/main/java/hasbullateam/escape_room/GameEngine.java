
package hasbullateam.escape_room;


import hasbullateam.escape_room.escape_room_game.EscapeRoomGame;
import hasbullateam.minigame.ping_pong.PingPongPanel;
import hasbullateam.escape_room.type.GameMode;

import javax.swing.*;
import java.util.function.Consumer;
import hasbullateam.minigame.MorraCinese;
import hasbullateam.minigame.Tris;

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
        menu.setGotoTris1vCPU( () -> {setPanel.accept(new Tris(menu, GameMode.MODE_1vCPU));} );
        menu.setGotoTris1v1( () -> {setPanel.accept(new Tris(menu, GameMode.MODE_1v1));} );
        menu.setGotoMorraCinese1vCPU( () -> {setPanel.accept(new MorraCinese(menu, GameMode.MODE_1vCPU));} );
        menu.setGotoMorraCinese1v1( () -> {setPanel.accept(new MorraCinese(menu, GameMode.MODE_1v1));} );
        
        setPanel.accept(menu);
        
    }
    

}
