/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.type;

/**
 *
 * @author giuse
 */


public interface Command{
    
    public enum Invalid implements Command{
        NONE;
    }
    
    public enum Move implements Command{
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
    
    public enum InventorySelection implements Command{
        NONE,
        SELECT_0,
        SELECT_1,
        SELECT_2,
        SELECT_3,
        SELECT_4,
        SELECT_5,
        SELECT_6,
        SELECT_7,
        SELECT_8,
        SELECT_9;
    }
    
    public enum Test implements Command{
        LOAD,
        BACKUP,
        MOD_ROOM;
    }
}
