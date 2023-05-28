
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.Tris;
import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.json.*;

/**
 *
 * @author giuse
 */

// TODO: facing player
// TODO: interagire con oggetti
// TODO: dialog
// TODO: mettere il keylistener in un altro file e passargli un espressione lambda
// TODO: inventario


public class EscapeRoom extends GridPanel{
    static final int GRID_SIZE = 10;
    public PlayerSquarePanel player;
    public Room room;
    TextDialog dialog;
    Inventory inventory;
    
    public EscapeRoom() {
        super(GRID_SIZE);
        
        this.addKeyListener(new KeyboardInput());
        
        this.dialog = new TextDialog(this);
        
        this.inventory = new Inventory(GRID_SIZE-1,0,GRID_SIZE-1);
        
        // carica room, player e inventario
        this.loadRoom("rooms\\atrio.json");
    }
    
    
    public void loadRoom(String jsonPathRoom){
        try{
            JSONObject jsonObj = new JSONObject(new String(Files.readAllBytes(Paths.get(jsonPathRoom))));
            this.loadRoom(new Room( jsonObj ));
        }catch (IOException e){
            System.err.println(jsonPathRoom+ " non valido");
        }
        
    }
    
    // carica nella Griglia newRoom, il player e l'inventario
    public void loadRoom(Room newRoom){
        
        // rimuovi vecchio player se presente
        if(this.player != null){
            setSquare( player.occupiedSquare );
        }
        
        // rimuovere tutti gli Oggeti della stanza precendete se presenti
        if(this.room != null){
            for(ObjectSquare obj: this.room.objects.values()){
                this.setSquare(new SquarePanel(obj.position));
            }
        }
        
        
        // carica background
        this.setBackgroundImage(newRoom.backGroundPath);
        
        // carica object
        for(ObjectSquare obj: newRoom.objects.values()){
            loadObjectSquare(obj);
        }
         
        
        // carica player
        player = new PlayerSquarePanel( newRoom.playerStarPosition, newRoom.playerPathImageUP, newRoom.playerPathImageDOWN,
        newRoom.playerPathImageLEFT, newRoom.playerPathImageRIGHT, newRoom.playerStartDirection);
        player.setOccupiedSquare(getMatrixSquare(player.position));
        setSquare(player);
        
        // aggiorna il riferimento a room
        this.room = newRoom;
        
        this.loadInventory();

    }
    
    public void loadObjectSquare(ObjectSquare obj){
        this.setSquare(new SquarePanel(obj.position, obj.backgroundColor,obj.pathImage) );
    }
    
    public void loadInventory(){
        // aggiungi gli item dell'inventario alla room
        for( ObjectSquare item: this.inventory.items ){
            this.room.addObject(item );
        }
        
        // aggiungi i panel dell'inventario
        for(SquarePanel panel: this.inventory.getItemPanels()){
            this.setSquare(panel);
        }
    }
    
    
    
    private void movePlayer(Command.Move command){
        if(command != Command.Move.NONE){
            
            Cord newPosition = player.position.clone();
        
            if( command == Command.Move.UP ){
                newPosition.y--;
                this.player.setFaceDirection(Direction.UP);
            }
            if( command == Command.Move.DOWN ){
                newPosition.y++;
                this.player.setFaceDirection(Direction.DOWN);
            }
            if( command == Command.Move.LEFT){
                newPosition.x--;
                this.player.setFaceDirection(Direction.LEFT);
            }
            if( command == Command.Move.RIGHT ){
                newPosition.x++;
                this.player.setFaceDirection(Direction.RIGHT);
            }

            changePlayerPosition(newPosition);
            repaint();
        }
        
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
    
    public void executeCommand(Command cmd){
        if( cmd instanceof Command.Move){
            movePlayer( (Command.Move) cmd);
        }else if (cmd instanceof Command.InventorySelection){
            int indx = ((Command.InventorySelection)cmd).ordinal()-1;
            this.inventory.select(   indx );
        }
        
        this.revalidate();
        this.repaint();
    }
    
    
    private class KeyboardInput implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            
            Command cmd = Command.Invalid.NONE;
            
            switch (key) {
                case '1':
                    cmd = Command.InventorySelection.SELECT_0;
                    break;
                case '2':
                    cmd = Command.InventorySelection.SELECT_1;
                    break;
                case '3':
                    cmd = Command.InventorySelection.SELECT_2;
                    break;
                case '4':
                    cmd = Command.InventorySelection.SELECT_3;
                    break;
                case '5':
                    cmd = Command.InventorySelection.SELECT_4;
                    break;
                case '6':
                    cmd = Command.InventorySelection.SELECT_5;
                    break;
                case '7':
                    cmd = Command.InventorySelection.SELECT_6;
                    break;
                case '8':
                    cmd = Command.InventorySelection.SELECT_7;
                    break;
                case '9':
                    cmd = Command.InventorySelection.SELECT_8;
                    break;
                case '0':
                    cmd = Command.InventorySelection.SELECT_9;
                    break;
                
                    
                case 'w':
                    cmd = Command.Move.UP;
                    break;
                case 's':
                    cmd = Command.Move.DOWN;
                    break;
                case 'a':
                    cmd = Command.Move.LEFT;
                    break;
                case 'd':
                    cmd = Command.Move.RIGHT;
                    break;
                
            }
            
            if(cmd != Command.Invalid.NONE){
                EscapeRoom.this.executeCommand(cmd);
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }  
}




/*
    public void startGame(){
        
        this.dialog.setText("<u>sottolineatura</u> io lo so che <em>corsivo</em> non sono solo anche quando sono solo e rido e piango e mi confondo con il cielo e con il fango eeeeeee la vita la vita e la vita è bella");
        this.dialog.show(!this.dialog.isVisible());
    }
    
    public void changePanel(){
        JFrame frame = (JFrame) this.getTopLevelAncestor();
        this.dialog.dialog.dispose();
        frame.setContentPane(new Tris());
        frame.revalidate();
        frame.repaint();
    }
    */