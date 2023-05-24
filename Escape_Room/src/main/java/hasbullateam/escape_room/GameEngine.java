
package hasbullateam.escape_room;


import java.util.function.Consumer;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class GameEngine{
    
    public GameEngine( Consumer<JPanel> setPanel ){
        
        Menu menu = new Menu();
        
        menu.setGotoEscapeRoom( () -> { setPanel.accept(new EscapeRoom()); }  );
        menu.setGotoBattleShip( () -> { setPanel.accept(new BattleShip()); }  );
        menu.setGotoTris(       () -> { setPanel.accept(new Tris()); }        );
        menu.setGotoPingPong(   () -> { setPanel.accept(new PingPong()); }    );
        
        
        setPanel.accept( menu );
        
    }
    

}
