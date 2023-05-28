
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuse
 */
public class Inventory {
    List<ObjectSquare> items;
    List<SquarePanel> itemPanels;
 
    int selected;
    static final Color SELECTIONCOLOR = new Color(10,10,255,128);
    static final Color DEFAULTCOLOR = new Color(10,10,10,128);
    
    public Inventory(ObjectSquare... items) {
        this.items = new ArrayList<>(items.length);
        this.items.addAll(Arrays.asList(items));
        
        this.itemPanels = new ArrayList<>(items.length);
        
        for( ObjectSquare item: this.items ){
            this.itemPanels.add( new SquarePanel(item.position, item.backgroundColor,item.pathImage) );
        }
        
        
        this.select(0);
    }
    
    public Inventory( int row, int startColumn, int endColumn ){
        
        this.items = new ArrayList<>();
        
        Integer c=0;
        for(int col = startColumn; col<=endColumn; col++){
            this.items.add(new ObjectSquare( ("INVENTORY_"+(c++).toString()),
                                            new Cord(col,row),DEFAULTCOLOR,
                                            null,false) );
            
        }
        
        this.itemPanels = new ArrayList<>();
        
        for( ObjectSquare item: this.items ){
            this.itemPanels.add( new SquarePanel(item.position, item.backgroundColor,item.pathImage) );
        }
        
        
        this.select(0);
        
    }
    
    public void select(int n){
        itemPanels.get(this.selected).setBackground(DEFAULTCOLOR);
        this.selected = n;
        itemPanels.get(this.selected).setBackground(SELECTIONCOLOR);
        
    }
    public List<SquarePanel> getItemPanels(){
        return this.itemPanels;
    }
    
}
