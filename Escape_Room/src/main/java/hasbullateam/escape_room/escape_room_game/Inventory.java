
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.InventoryFullException;
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
    static final String FREE_CONSTANT = "FREE";
    
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

        for(int col = startColumn; col<=endColumn; col++){
            this.items.add(new ObjectSquare( FREE_CONSTANT,
                                            new Cord(col,row),DEFAULTCOLOR,
                                            null,false,false) );
            
        }
        
        this.itemPanels = new ArrayList<>();
        
        for( ObjectSquare item: this.items ){
            this.itemPanels.add( new SquarePanel(item.position, item.backgroundColor,item.pathImage) );
        }
        
        
        this.select(0);
        
    }
    
    public void refreshSelected(){
        this.select(this.selected);
    }
    
    public void select(int n){
        itemPanels.get(this.selected).setBackground(DEFAULTCOLOR);
        this.selected = n;
        itemPanels.get(this.selected).setBackground(SELECTIONCOLOR);
        
    }
    public List<SquarePanel> getItemPanels(){
        return this.itemPanels;
    }
    
    public void putObjectSquare(ObjectSquare obj) throws InventoryFullException{
        for(int i=0; i<items.size(); i++){
            
            if( items.get(i).name.contains(FREE_CONSTANT) ){
                ObjectSquare _obj = this.items.get(i);
                
                obj.position = _obj.position;
                obj.backgroundColor = _obj.backgroundColor;
                obj.isInteractable = false;
                obj.isPassable = false;
                
                this.items.set(i, obj);
                
                this.itemPanels.set(i, new SquarePanel(obj.position, obj.backgroundColor, obj.pathImage));
                
                this.select(this.selected);
                
                return;
            } 
        }
        
        throw new InventoryFullException();
    }
    
    public ObjectSquare getSelected(){
        return this.items.get(selected);
    }
    
    public void removeObject(int indx){
        ObjectSquare voidObj = new ObjectSquare( FREE_CONSTANT,
                this.items.get(indx).position,DEFAULTCOLOR,null,false,false);
        this.items.set(indx, voidObj);
        this.itemPanels.set(indx,new SquarePanel(voidObj.position, 
                voidObj.backgroundColor,voidObj.pathImage) );
    }
    
    public Integer containObject(String name){
        for(int i=0; i<this.items.size(); i++){
            if(items.get(i).name.equalsIgnoreCase(name)){
                return i;
            }
        }
        return null;
    }
    
}
