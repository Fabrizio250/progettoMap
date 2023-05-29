
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author giuse
 */
public class PlayerSquarePanel extends SquarePanel{
    SquarePanel occupiedSquare;
    Direction direction;
    Map<Direction, List<String>> directionImages;
    int _animationIndx = 0;
    
    
    public PlayerSquarePanel(Cord position, 
            String pathImageUP_0,String pathImageDOWN_0,String pathImageLEFT_0,String pathImageRIGHT_0,
            String pathImageUP_1,String pathImageDOWN_1,String pathImageLEFT_1,String pathImageRIGHT_1,
            String pathImageUP_2,String pathImageDOWN_2,String pathImageLEFT_2,String pathImageRIGHT_2, Direction defaultDir){
        super(position);
        this.directionImages = new HashMap(4);
        this.directionImages.put(Direction.UP, new ArrayList<>( Arrays.asList(pathImageUP_0,pathImageUP_1,pathImageUP_2)));
        this.directionImages.put(Direction.DOWN, new ArrayList<>( Arrays.asList(pathImageDOWN_0,pathImageDOWN_1,pathImageDOWN_2)));
        this.directionImages.put(Direction.LEFT, new ArrayList<>( Arrays.asList(pathImageLEFT_0,pathImageLEFT_1,pathImageLEFT_2)));
        this.directionImages.put(Direction.RIGHT, new ArrayList<>( Arrays.asList(pathImageRIGHT_0,pathImageRIGHT_1,pathImageRIGHT_2)));
        this.direction = defaultDir;
        
        this.setBackgroundImage(this.directionImages.get(defaultDir).get(_animationIndx));
    }

    public PlayerSquarePanel(Cord position, String pathImage) {
        this(position, pathImage,pathImage,pathImage,pathImage,
                       pathImage,pathImage,pathImage,pathImage,
                       pathImage,pathImage,pathImage,pathImage,Direction.UP);
        
    }
    
    public void setOccupiedSquare( SquarePanel square ){
        this.occupiedSquare = square.clone();
        this.setBackground(this.occupiedSquare.getBackground());
        
    }
    
    public void setFaceDirection(Direction dir){
        if(this.direction == dir){
            _animationIndx ++;
            if(_animationIndx > 3){
                _animationIndx = 1;
            }
        }else{
            _animationIndx = 1;
        }
        System.out.println(_animationIndx);
        this.setBackgroundImage(this.directionImages.get(dir).get(  _animationIndx-1 ));
        this.direction = dir;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        
        if( this.occupiedSquare != null ){
            if(this.occupiedSquare.backgroundImage != null){
                this.occupiedSquare.backgroundImage.resizeOnFirstDrawn(this.getWidth(), this.getHeight());
                g.drawImage(this.occupiedSquare.backgroundImage.getImage(), 0, 0, null);
            }
        }
        super.paintComponent(g);
    }
    
}
