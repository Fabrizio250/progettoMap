
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cord other = (Cord) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }
    
    
}
