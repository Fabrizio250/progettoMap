/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.type;

/**
 *
 * @author giuse
 */


public class Command{
    
    public enum Move{
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }
    
    public enum Facing{
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
}
