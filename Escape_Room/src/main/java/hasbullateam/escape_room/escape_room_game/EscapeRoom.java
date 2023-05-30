
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
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.json.*;

/**
 *
 * @author giuse
 */


// TODO: interagire con oggetti
// TODO: pulire il backup delle stanze ad ogni avvio


public class EscapeRoom extends GridPanel{
    static final int GRID_SIZE = 10;
    public PlayerSquarePanel player;
    public Room room;
    MultipleChoiceDialog dialog;
    Inventory inventory;
    
    Command cmd;
    
    private static int _backupFileCounter = 0;
    
    private Boolean _showDialog = false;
    
    public EscapeRoom() {
        super(GRID_SIZE);
        
        this.addKeyListener(new KeyboardInput());
        
        //this.dialog = new MultipleChoiceDialog(this);//new TextDialog(this);
        //dialog.setBrief("le mie palle le mie palle le <br>mie palle le mie palle le mie palle le <br>mie palle le mie palle le mie palle le mie palle le mie <br>palle le mie palle le mie palle le mie palle le mie palle ");
        //dialog.setChoices("Palle1","Palle2","Palle3","Palle4");
        //dialog.assembleText();
        
        
        this.inventory = new Inventory(GRID_SIZE-1,0,GRID_SIZE-1);
        
        this.loadRoomFromJSON("rooms\\atrio.json");
        
        SwingUtilities.invokeLater( ()-> startGame() );
        
    }
    
    public void startGame(){

        // in base a dove si trova il player, l'inventario, e lo stato della stanza fai cose
        (new Thread(()->{
            
            
            
            
            try{
                while(true){
                    
                   _highlightFacingObject();
                   
                   ObjectSquare obj = this.room.getFacingObject();
                   
                   
                    if(obj != null){
                    
                        if(obj instanceof ContainerObjectSquare){
                            ContainerObjectSquare _obj = (ContainerObjectSquare)obj;
                           
                           
                            if(this.cmd instanceof Command.Player){
                                Command.Player _cmd = (Command.Player)this.cmd;
                                if(_cmd == Command.Player.INTERACT){
                                    if(!this._showDialog){ // TODO levare sta roba e trovare una soluzione migliore
                                        
                                        System.out.println(_obj.getBrief());
                                        System.out.println(_obj.getObjectsString());
                                        
                                        
                                        this._showDialog = true;
                                    }
                                    //dialog = new MultipleChoiceDialog(this);
                                    //System.out.println(dialog.text);
                                    //dialog.setBrief( _obj.getBrief() );
                                    //dialog.setChoices( _obj.getObjectsString().toArray(new String[_obj.objectList.size()]) );
                                    //dialog.assembleText();
                                    //dialog.show(true);
                                    //dialog.reWriteText(true);
                                }
                            }
                           
                           
                        }
                       
                       
                       
                       
                       
                       
                    }
                   
                   
                   
                   
                   
                    Thread.sleep(100);
                }
            }catch(InterruptedException e){
                
            }
            
            
        })).start();
        
    }
    
    private class HighlightFacingObjectManger implements Runnable{
        static Boolean selectedObj = false;
            
        static final Color selectedBackground = new Color(200,200,200,80);
        static Color previousBackground = selectedBackground;
        
        static ObjectSquare facingObj = null;
        static ObjectSquare previousObject = null;

        public static void setFacingObj(ObjectSquare facingObj) {
            HighlightFacingObjectManger.facingObj = facingObj;
        }
        
        public void setPreviousObject(ObjectSquare prevObj){
            this.previousObject = prevObj;
        }

        @Override
        public void run() {
            if( facingObj != null ){

                if(previousObject == null){
                    previousObject = facingObj;
                }

                if(!facingObj.equals(previousObject) && selectedObj){
                    previousObject.setBackgroundColor(previousBackground);
                    EscapeRoom.this.loadObjectSquare(previousObject);
                    selectedObj = false;
                    
                    EscapeRoom.this.revalidate();
                    EscapeRoom.this.repaint();
                    
                }
                if(facingObj.isInteractable){

                    if(!selectedObj){
                        previousBackground = facingObj.backgroundColor;
                        previousObject = facingObj;
                        facingObj.backgroundColor = selectedBackground ;
                        EscapeRoom.this.loadObjectSquare(facingObj);
                        selectedObj = true;
                        EscapeRoom.this.revalidate();
                        EscapeRoom.this.repaint();
                    }
                }
            }else{

                if(selectedObj){  

                    previousObject.setBackgroundColor(previousBackground);
                    EscapeRoom.this.loadObjectSquare(previousObject);
                    selectedObj = false;

                    EscapeRoom.this.revalidate();
                    EscapeRoom.this.repaint();

                }
            }
        }
    }
    
    public void _highlightFacingObject(){
        
        HighlightFacingObjectManger h = new HighlightFacingObjectManger();
        
        if(h.previousObject == null){
            h.setPreviousObject(this.room.getFacingObject());
        }
        h.setFacingObj(this.room.getFacingObject());
        
        h.run();
        
    }
    
    
    public void backupRoom(String pathBackupFile, Room room){
        
        try{
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(pathBackupFile));
            outStream.writeObject(this._backupFileCounter++);
            outStream.writeObject(room);
            outStream.close();
        }catch (IOException e){
            System.err.println(pathBackupFile+" path non valido");
        }
    }
    
    
    public void loadRoomFromBackupFile(String pathBackupFile, String roomName){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathBackupFile));
            Room searchedRoom = null;
            Room currentRoom = null;
            int maxCounterBackup = 0;
            int counter;
            try{
                while(true){
                    try {
 
                        counter = (Integer) inputStream.readObject();
                        currentRoom = (Room) inputStream.readObject();
                        
                        if(currentRoom.name.equals(roomName) && (counter >= maxCounterBackup)){
                            maxCounterBackup = counter;
                            searchedRoom = currentRoom;
                        }
  
                    } catch (ClassNotFoundException ex) {
                        
                    }
                }
            }catch (EOFException e){
                
            }
            if(searchedRoom != null){
                loadRoom(searchedRoom);
            }else{
                System.err.println(roomName+" non trovata");
            }
            
            
        } catch (FileNotFoundException e) {
            System.err.println(pathBackupFile+" path non valido");
        } catch (IOException e){
            System.out.println("io exception");
        }
    }
    
    
    public void loadRoomFromJSON(String jsonPathRoom){
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
        player = new PlayerSquarePanel( newRoom.playerPosition, 
                newRoom.playerPathImageUP_0, newRoom.playerPathImageDOWN_0, newRoom.playerPathImageLEFT_0, newRoom.playerPathImageRIGHT_0, 
                newRoom.playerPathImageUP_1, newRoom.playerPathImageDOWN_1, newRoom.playerPathImageLEFT_1, newRoom.playerPathImageRIGHT_1,
                newRoom.playerPathImageUP_2, newRoom.playerPathImageDOWN_2, newRoom.playerPathImageLEFT_2, newRoom.playerPathImageRIGHT_2,newRoom.playerDirection);
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
                
                if(this.player.direction.equals(Direction.UP)){
                    newPosition.y--;
                    this.player.setFaceDirection(this.player.direction);
                }else{
                    
                    this.player.setFaceDirection(Direction.UP);
                }
                
            }
            if( command == Command.Move.DOWN ){
                
                if(this.player.direction.equals(Direction.DOWN)){
                    newPosition.y++;
                    this.player.setFaceDirection(this.player.direction);
                }else{
                    
                    this.player.setFaceDirection(Direction.DOWN);
                }
                
            }
            if( command == Command.Move.LEFT){
                
                if(this.player.direction.equals(Direction.LEFT)){
                    newPosition.x--;
                    this.player.setFaceDirection(this.player.direction);
                }else{
                    
                    this.player.setFaceDirection(Direction.LEFT);
                }
                
            }
            if( command == Command.Move.RIGHT ){
                
                if(this.player.direction.equals(Direction.RIGHT)){
                    newPosition.x++;
                    this.player.setFaceDirection(this.player.direction);
                    
                }else{
                    this.player.setFaceDirection(Direction.RIGHT);
                }
                
            }
            
            if(!newPosition.equals(this.player.position)){
                changePlayerPosition(newPosition);
                
                // aggiorna la posizione del player nella room se è stata cambiata
                if(!this.player.position.equals(this.room.playerPosition)){
                    this.room.playerPosition = this.player.position;
                }
            }
            
            // aggiorna la direzione del player nella room se è stata cambiata
            if(!this.player.direction.equals(this.room.playerDirection)){
                this.room.playerDirection = this.player.direction;
            }
            
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
        if( cmd instanceof Command.Move move){    
            movePlayer(move);
        
        }else if (cmd instanceof Command.InventorySelection inventorySelection){
            int indx = inventorySelection.ordinal()-1; // -1 perchè l'indice 0 è NONE
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
            int keyCode = e.getKeyCode();
            
            
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
                    
                case 'e':
                    cmd = Command.Player.INTERACT;
                    break;
            }
            
            switch (keyCode){
                case KeyEvent.VK_UP:
                    cmd = Command.Dialog.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    cmd = Command.Dialog.DOWN;
                    break;
                case KeyEvent.VK_ESCAPE:
                    cmd = Command.Dialog.ESC;
                    break;
                case KeyEvent.VK_ENTER:
                    cmd = Command.Dialog.ENTER;
                    break;
            }
            
            if(cmd != Command.Invalid.NONE){
                EscapeRoom.this.executeCommand(cmd);
            }
            
            EscapeRoom.this.cmd = cmd;

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }  
}




/*

    
    public void changePanel(){
        JFrame frame = (JFrame) this.getTopLevelAncestor();
        this.dialog.dialog.dispose();
        frame.setContentPane(new Tris());
        frame.revalidate();
        frame.repaint();
    }
    */






/*
        else if (cmd instanceof Command.Test){ //TODO: da levare
            
            Command.Test cmd2 = (Command.Test)cmd;
            
            if(cmd2 == Command.Test.MOD_ROOM){
                this.room.getObject(new Cord(4,4)).position = new Cord(1,1);
                this.room.addObject( this.room.getObject(new Cord(4,4)) );
                this.room.removeObject(new Cord(4,4));

                this.loadObjectSquare(this.room.getObject(new Cord(1,1)));
                this.clearSquare(new Cord(4,4));
                this.room.addObject(new ObjectSquare("palle",new Cord(0,0),"images\\player2.png",false));
                this.loadObjectSquare(this.room.getObject(new Cord(0,0)));
            
            }else if(cmd2 == Command.Test.BACKUP){
                this.backupRoom("rooms\\roomsBackup.dat", this.room);
            
            }else if(cmd2 == Command.Test.LOAD){
                this.loadRoomFromBackupFile("rooms\\roomsBackup.dat", "atrio" );
            
            }else if(cmd2 == Command.Test.SET_TEXT){
                this.dialog.show(!this.dialog.isVisible());
                if(this.dialog.isVisible()){ 
                    this.dialog.assembleText();
                    this.dialog.reWriteText(true);
                    //this.dialog.setText("<pre>   lollol oll <br>   le mie palle</pre>");
                    //this.dialog.setText("<u>sottolineatura</u> io lo dahadhhdhas adh ha haa a  adha h agay yda gfyag fagag yayf yuaga aggfygsgygyaufyugafgyfgyuafygfagyufasygufas afg aygagf uaga gyfayu afyg yuaf aygayu asf gfy ayug uyaffuguag uasgu so che <em>corsivo</em> non sono solo anche quando sono solo e rido e piango e mi confondo con il cielo e con il fango eeeeeee la vita la vita e la vita è bella");
                }else{
                    this.dialog.setText("");
                }
                
            }else if(cmd2 == Command.Test.SET_TEXT_SIZE){
                this.dialog.setSize(200, 600);
                this.dialog.reWriteText(true);
            }
        } else if (cmd instanceof Command.Dialog){
            Command.Dialog cmd2 = (Command.Dialog)cmd;
            
            if(cmd2 == Command.Dialog.ESC){
                this.dialog.show(false);
                
            }else if(this.dialog instanceof MultipleChoiceDialog){
                MultipleChoiceDialog _dialog = (MultipleChoiceDialog)this.dialog;
                
                if(cmd2 == Command.Dialog.UP){
                    _dialog.select(_dialog.selectedIndx-1);
                    
                }else if(cmd2 == Command.Dialog.DOWN){
                    _dialog.select(_dialog.selectedIndx+1);
                    
                }else if(cmd2 == Command.Dialog.ENTER){
                    System.out.println(_dialog.getChoice());
                }
            }
                
                
        }
        */




                    
                /*
                    
                case 'f'://TODO: da levare
                    cmd = Command.Test.MOD_ROOM;
                    break;
                case 'g'://TODO: da levare
                    cmd = Command.Test.BACKUP;
                    break;
                case 'h'://TODO: da levare
                    cmd = Command.Test.LOAD;
                    break;
                case 'i':
                    cmd = Command.Test.SET_TEXT;
                    break;
                case 'l':
                    cmd = Command.Test.SET_TEXT_SIZE;
                    break;
                */









/*

public void startGame(){

        // in base a dove si trova il player, l'inventario, e lo stato della stanza fai cose
        (new Thread(()->{
            Boolean selectedObj = false;
            
            Color selectedBackground = new Color(200,200,200,80);
            Color previousBackground = selectedBackground;
            
            ObjectSquare facingObj;
            ObjectSquare previousObject =this.room.getFacingObject();
            
            
            
            try{
                while(true){
                    facingObj = this.room.getFacingObject();
                    
                    
                    
                    if( facingObj != null ){

                        if(previousObject == null){
                            previousObject = facingObj;
                        }

                        if(!facingObj.equals(previousObject) && selectedObj){
                            previousObject.setBackgroundColor(previousBackground);
                            this.loadObjectSquare(previousObject);
                            selectedObj = false;

                            this.repaint();
                            this.revalidate();
                        }
                        if(facingObj.isInteractable){

                            if(!selectedObj){
                                previousBackground = facingObj.backgroundColor;
                                previousObject = facingObj;
                                facingObj.backgroundColor = selectedBackground ;
                                this.loadObjectSquare(facingObj);
                                selectedObj = true;
                                revalidate();
                                repaint();
                            }
                        }
                    }else{

                        if(selectedObj){  

                            previousObject.setBackgroundColor(previousBackground);
                            this.loadObjectSquare(previousObject);
                            selectedObj = false;

                            this.revalidate();
                            this.repaint();

                        }
                    }
                   
                    Thread.sleep(100);
                }
            }catch(InterruptedException e){
                
            }
            
            
        })).start();
        
    }


*/