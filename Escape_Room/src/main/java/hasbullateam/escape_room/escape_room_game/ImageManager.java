
package hasbullateam.escape_room.escape_room_game;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author giuse
 */
public class ImageManager extends ImageIcon{
    private boolean firstDraw = true;
    
    public ImageManager(String path){
        super(path);
    }
    
    public void resizeOnFirstDrawn(int width, int height){
        if(this.firstDraw){
            this.setImage(  this.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH) );
            this.firstDraw = false;
        }
        
    }
}
