/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    static final Color selectionColor = new Color(10,10,255,128);
    
    public Inventory(ObjectSquare... items) {
        this.items = new ArrayList<>(items.length);
        this.items.addAll(Arrays.asList(items));
        
        this.itemPanels = new ArrayList<>(items.length);
        
        for( ObjectSquare item: this.items ){
            this.itemPanels.add( new SquarePanel(item.position, item.backgroundColor,item.pathImage) );
        }
        
        
        this.select(0);
    }
    
    public void select(int n){
        itemPanels.get(this.selected).setBackground(new Color(0,0,0,0));
        this.selected = n;
        itemPanels.get(this.selected).setBackground(selectionColor);
        
    }
    public List<SquarePanel> getItemPanels(){
        return this.itemPanels;
    }
    
}
