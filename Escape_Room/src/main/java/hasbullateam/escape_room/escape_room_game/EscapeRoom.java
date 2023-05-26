
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author giuse
 */


public class EscapeRoom extends GridPanel{
    static final int GRID_SIZE = 10;
    PlayerSquarePanel player;
    Room room;
    
    public EscapeRoom() {
        super(GRID_SIZE);
        this.addKeyListener(new KeyboardInput());
        

        room = new Room("images\\prigione.jpg", new Cord(GRID_SIZE/2, GRID_SIZE/2),"images\\player2.png");
        room.addObject(new ObjectSquare("lol", new Cord( 4,4 ), "images\\player2.png" , false)  );
        room.addObject(new ObjectSquare("lol", new Cord( 2,2 ), "images\\player.png" , true)  );
        loadRoom(room);
        
        // TODO: levare sto coso
        setSquare(new SquarePanel(new Cord(6,6), Color.red));
        

    }
    
    public void startGame(){
        
    }
    
    public void loadRoom(Room room){
        // carica background
        this.setBackgroundImage(room.backGroundPath);
        
        // carica object
        for(ObjectSquare obj: room.objects.values()){
            loadObjectSquare(obj);
        }
        
        // carica player
        player = new PlayerSquarePanel( room.playerStarPosition, room.playerPathImage);
        player.setOccupiedSquare(getMatrixSquare(player.position));
        setSquare(player);

    }
    
    public void loadObjectSquare(ObjectSquare obj){
        this.setSquare(new SquarePanel(obj.position, obj.pathImage) );
    }
    
    
    private void movePlayer(Command.Move command){
        Cord newPosition = player.position.clone();
        
        if( command == Command.Move.UP ){
            newPosition.y--;
        }
        if( command == Command.Move.DOWN ){
            newPosition.y++;
        }
        if( command == Command.Move.LEFT){
            newPosition.x--;
        }
        if( command == Command.Move.RIGHT ){
            newPosition.x++;
        }
        
        changePlayerPosition(newPosition);
    }
    
    
    private void changePlayerPosition(Cord newPostion){
        boolean vaildChange;
        
        
        if ((newPostion.x < 0) || (newPostion.y < 0) || 
           (newPostion.x > GRID_SIZE-1)|| (newPostion.y > GRID_SIZE-1) ||
           ( this.room.containsObject(newPostion) && !this.room.getObject(newPostion).isPassable)){

            
        }else{
            
            // metti dove sta adesso il player lo square che sta occupando
            setSquare(player.occupiedSquare);
            
            // salva lo square in cui dovrà andare
            player.setOccupiedSquare( getMatrixSquare(newPostion) );
            
            // metti il player nel nuovo square
            player.position = newPostion;
            setSquare(player);

            revalidate();
        }
        
        
        
    }
    
    
    private class KeyboardInput implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            System.out.println(key);
            
            Command.Move cmd = Command.Move.NONE;
            
            if(key == 'w'){
                cmd = Command.Move.UP;
            }
            if (key == 's'){
                cmd = Command.Move.DOWN;
            }
            if (key == 'a'){
                cmd = Command.Move.LEFT;
            }
            if (key == 'd'){
                cmd = Command.Move.RIGHT;
            }
            
            if (key == 'f'){
                EscapeRoom.this.setSquare(new SquarePanel(new Cord(6,6), "images\\player2.png"));
                EscapeRoom.this.revalidate();
                /*
                EscapeRoom.this.room.objects.put(new Cord(4,4),
                new ObjectSquare("lol", new Cord(4,4), "images\\player2.png", false));
                EscapeRoom.this.loadObjectSquare( EscapeRoom.this.room.getObject(new Cord(4,4)));
                */
            }
            
            movePlayer(cmd);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }  
}
