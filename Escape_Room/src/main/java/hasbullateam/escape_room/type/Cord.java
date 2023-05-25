
package hasbullateam.escape_room.type;

/**
 *
 * @author giuse
 */
public class Cord implements Cloneable{
    public int x;
    public int y;
    
    public Cord(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Cord(){
        this.x = 0;
        this.y = 0;
    }

    public int getIndex( int gridSize ){  
        return (gridSize * (this.y)) + this.x;
    }
    
    @Override
    public Cord clone(){
        return new Cord(this.x, this.y);
    } 
}
