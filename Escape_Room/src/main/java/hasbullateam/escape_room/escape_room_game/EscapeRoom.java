
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


/**
 *
 * @author giuse
 */

// TODO: facing player
// TODO: interagire con oggetti
// TODO: dialog
// TODO: mettere il keylistener in un altro file e passargli un espressione lambda
// TODO: room parser
// TODO: inventario


public class EscapeRoom extends GridPanel{
    static final int GRID_SIZE = 10;
    PlayerSquarePanel player;
    Room room;
    TextDialog dialog;
    Inventory inventory;
    //JDialog dialog;
    
    public EscapeRoom() {
        super(GRID_SIZE);
        this.addKeyListener(new KeyboardInput());
        
        this.dialog = new TextDialog(this);
        
        inventory = new Inventory( new SquarePanel(new Cord(3,GRID_SIZE-1)),
                                   new SquarePanel(new Cord(4,GRID_SIZE-1)),
                                   new SquarePanel(new Cord(5,GRID_SIZE-1)),
                                   new SquarePanel(new Cord(6,GRID_SIZE-1)));
        loadInventory();

        room = new Room("images\\prigione.jpg", new Cord(GRID_SIZE/2, GRID_SIZE/2),"images\\player2.png");
        room.addObject(new ObjectSquare("lol", new Cord( 4,4 ), "images\\player2.png" , false)  );
        room.addObject(new ObjectSquare("lol", new Cord( 2,2 ), "images\\player.png" , true)  );
        loadRoom(room);
        
        
        
        

    }
    
    
    
    
    
    
    public void startGame(){
        
        this.dialog.setText("<u>sottolineatura</u> io lo so che <em>corsivo</em> non sono solo anche quando sono solo e rido e piango e mi confondo con il cielo e con il fango eeeeeee la vita la vita e la vita è bella");
        this.dialog.show(!this.dialog.isVisible());
    }
    
    public void loadInventory(){
        for( SquarePanel item: this.inventory.items ){
            this.setSquare(item);
        }
    }
    
    
    public void loadRoom(Room newRoom){
        
        // rimuovi vecchio player se presente
        if(this.player != null){
            setSquare( player.occupiedSquare );
        }
        
        // rimuovere tutti gli Oggeti della stanza precendete
        for(ObjectSquare obj: this.room.objects.values()){
            this.setSquare(new SquarePanel(obj.position));
        }
        
        // carica background
        this.setBackgroundImage(newRoom.backGroundPath);
        
        // carica object
        for(ObjectSquare obj: newRoom.objects.values()){
            System.err.println("aoooo");
            loadObjectSquare(obj);
        }
        
        // carica player
        player = new PlayerSquarePanel( newRoom.playerStarPosition, newRoom.playerPathImage);
        player.setOccupiedSquare(getMatrixSquare(player.position));
        setSquare(player);
        
        // aggiorna il riferimento a room
        this.room = newRoom;

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
        
        
        if ((newPostion.x < 0) || (newPostion.y < 0) || 
           (newPostion.x > GRID_SIZE-1)|| (newPostion.y > GRID_SIZE-1) ||
           ( this.room.containsObject(newPostion) && !this.room.getObject(newPostion).isPassable)){
            // spostamento non valido
            
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
            
            if(key == '1'){
                EscapeRoom.this.inventory.select(0);
            }
            if(key == '2'){
                EscapeRoom.this.inventory.select(1);
            }
            if(key == '3'){
                EscapeRoom.this.inventory.select(2);
            }
            if(key == '4'){
                EscapeRoom.this.inventory.select(3);
            }
            
            
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
                //EscapeRoom.this.setSquare(new SquarePanel(new Cord(6,6), "images\\player2.png"));
                //EscapeRoom.this.revalidate();
                
                Room rm = new Room("images\\sfondo2.jpg", new Cord(0,0),"images\\player.png");
                rm.addObject(new ObjectSquare("ll", new Cord( 4,2 ), "images\\player2.png" , false)  );
                rm.addObject(new ObjectSquare("lol", new Cord( 1,4 ), "images\\player.png" , true)  );
                EscapeRoom.this.loadRoom( rm);

            }
            
            if (key == 'g'){
                EscapeRoom.this.startGame();
            }
            
            movePlayer(cmd);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }  
}
