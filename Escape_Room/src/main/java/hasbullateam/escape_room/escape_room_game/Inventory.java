/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuse
 */
public class Inventory {
    List<SquarePanel> items;
    int selected;
    static final Color selectionColor = new Color(10,10,255,128);
    
    public Inventory(SquarePanel... items) {
        this.items = new ArrayList<>(items.length);
        for(SquarePanel item: items){
            this.items.add(item);
        }
        this.select(0);
    }
    
    public void select(int n){
        items.get(this.selected).setBackground(new Color(0,0,0,0));
        this.selected = n;
        items.get(this.selected).setBackground(selectionColor);
        
    }
    
}
