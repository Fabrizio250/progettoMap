
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.Tris;
import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import hasbullateam.escape_room.type.RoomNotFoundException;
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
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    TextDialog dialog;
    Inventory inventory = new Inventory(GRID_SIZE-1,0,GRID_SIZE-1);;
    public Thread loopThread;
    final String BACKUP_FILE_PATH = "rooms\\roomsBackup.dat";
    
    Command cmd = Command.Invalid.NONE;
    
    public Integer _backupFileCounter = 0;
    
    private Boolean _showDialog = false;
    
    public EscapeRoom() {
        super(GRID_SIZE);
        
        this.addKeyListener(new KeyboardInput());
        resetBackupFile();
        
    }
    
    public void resetBackupFile(){
        try {
            FileOutputStream file = new FileOutputStream(BACKUP_FILE_PATH);
            file.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public void gameLoop(){
        
    }
    
    
    public void startGame(){
        this.loopThread = new Thread(()->{

            try{
                while(true){
                    
                    SwingUtilities.invokeAndWait(
                        ()->{gameLoop(); }
                    
                    );

                    Thread.sleep(50);
                    
                }
            }catch(InterruptedException e){
                System.err.println(e.getMessage());
            } catch (InvocationTargetException ex) {
                System.err.println(ex.getMessage());
            }  
        });
        this.loopThread.start();
    }
    
    
    public void changePanel(JPanel newPanel){
        JFrame frame = (JFrame) this.getTopLevelAncestor();
        removeKeyListener(this.getKeyListeners()[0]);
        frame.setContentPane(newPanel);
        frame.revalidate();
        frame.repaint();
        this.loopThread.interrupt();
        this.loopThread = null;
    }
    
    
    public void refresh(){
        this.revalidate();
        this.repaint();
    }
    
    
    //TODO: mettere in una classe a parte e passargli la lamda di refresh e loadObj
    protected class HighlightFacingObjectManger implements Runnable{
        static Boolean selectedObj = false;
            
        static final Color selectedBackground = new Color(200,200,200,80);
        static Color previousBackground = selectedBackground;
        
        static ObjectSquare facingObj = null;
        static ObjectSquare previousObject = null;

        public static void setFacingObj(ObjectSquare facingObj) {
            HighlightFacingObjectManger.facingObj = facingObj;
        }
        
        public static void reset(){
            HighlightFacingObjectManger.selectedObj = false;
            HighlightFacingObjectManger.previousBackground = selectedBackground;
            HighlightFacingObjectManger.facingObj = null;
            HighlightFacingObjectManger.previousObject = null;
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
    
    public void highlightFacingObject(){
        
        HighlightFacingObjectManger h = new HighlightFacingObjectManger();
        
        if(h.previousObject == null){
            h.setPreviousObject(this.room.getFacingObject());
        }
        h.setFacingObj(this.room.getFacingObject());
        
        h.run();
        
    }
    
    public void resetHighlightFObjcet(){
        //HighlightFacingObjectManger h = new HighlightFacingObjectManger();
        //h.reset();
        HighlightFacingObjectManger.reset();
    }
    
    
    public void backupRoom(String filePath, Room room) {
        Map<Integer,Room> rooms = new HashMap<>();
        
        // leggi dal file tutti gli oggetti room e counter e mettili nell' hash map
        try {
            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(filePath));

            while (true) {
                try {
                    int counter = inStream.readInt();
                    Room current_room = (Room) inStream.readObject();
                    
                    rooms.put(counter, current_room);
                    

                } catch (EOFException e) {
                    //System.out.println("aeeeeeeee");
                    break;
                } catch (ClassNotFoundException e) {
                    //System.err.println("Classe non trovata durante la lettura dell'oggetto Room dal file: " + filePath);
                    e.printStackTrace();
                    break;
                }
            }

            inStream.close();

        } catch (FileNotFoundException e) {
 
        } catch (IOException e) {
            //System.err.println("Errore durante la lettura dell'oggetto Room dal file: " + filePath+ "  ehhh già");
            //e.printStackTrace();
        }
        
        rooms.put(this._backupFileCounter++, room);
        
        // riscrvi rooms nel file
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(filePath));
            
            for( Integer counter: rooms.keySet() ){
                outStream.writeInt(counter);
                outStream.writeObject(rooms.get(counter));
            }
            
            outStream.close();

            //System.out.println("Oggetto Room scritto con successo nel file: " + filePath);
        } catch (IOException e) {
            //System.err.println("Errore durante la scrittura dell'oggetto Room nel file: " + filePath);
            e.printStackTrace();
        }
    }
    
    
    public void loadRoomFromBackupFile(String filePath, String roomName) throws RoomNotFoundException {
        Room searchedRoom = null;
        int maxCounter = -1;

        try {
            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(filePath));

            while (true) {
                try {
                    int counter = inStream.readInt();
                    Room current_room = (Room) inStream.readObject();

                    if (current_room.name.equals(roomName) && counter > maxCounter) {
                        maxCounter = counter;
                        searchedRoom = current_room;
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    //System.err.println("Classe non trovata durante la lettura dell'oggetto Room dal file: " + filePath);
                    e.printStackTrace();
                    break;
                }
            }

            inStream.close();

        } catch (FileNotFoundException e) {
            //System.err.println("File non trovato: " + filePath);
            throw new RoomNotFoundException();
        } catch (IOException e) {
            //System.err.println("Errore durante la lettura dell'oggetto Room dal file: " + filePath);
            e.printStackTrace();
        }

        if(searchedRoom != null){
            //System.out.println("Room trovata!!"+" name:"+searchedRoom.name);
            loadRoom(searchedRoom);
        }else{
            //System.out.println("room non trovata");
            throw new RoomNotFoundException();
        }
    }
    
    
    public void loadRoomFromJSON(String jsonPathRoom){
        System.out.println(jsonPathRoom);
        try{
            JSONObject jsonObj = new JSONObject(new String(Files.readAllBytes(Paths.get(jsonPathRoom))));
            this.loadRoom(new Room( jsonObj ));
        }catch (IOException e){
            System.err.println(jsonPathRoom+ " non valido");
        }catch (Exception e){
            e.printStackTrace();
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
    
    public void removeObjectSquare(ObjectSquare obj){
        // rimuovi dalla room
        this.room.removeObject(obj.position);
        this.setSquare(new SquarePanel(obj.position));
        
    }
    
    public void loadInventory(){
            
        this.inventory.refreshSelected();
        
        // aggiungi gli item dell'inventario alla room
        for( ObjectSquare item: this.inventory.items ){
            this.room.addObject(item );
        }
        
        // aggiungi i panel dell'inventario
        for(SquarePanel panel: this.inventory.getItemPanels()){
            this.setSquare(panel);
        }
        
        
    }
    
  
    
    public void movePlayer(Command.Move command){
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
    
    public void selectInventory(Command.InventorySelection inventorySelection){
        int indx = inventorySelection.ordinal()-1; // -1 perchè l'indice 0 è NONE
        this.inventory.select(   indx );
    }
    
    
    private void changePlayerPosition(Cord newPostion){
        
        
        if (!this.isCordInGrid(newPostion)||
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
    
    
    
    public class KeyboardInput implements KeyListener{

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
                case 'q':
                    cmd = Command.Player.DROP_OBJECT;
                    break;
                    
                case 'n':
                    cmd = Command.Test.NEXT_ROOM;
                    break;
                case 'p':
                    cmd = Command.Test.PREVIOUS_ROOM;
                    break;
            }
            
            switch (keyCode){
                case KeyEvent.VK_UP:
                    cmd = Command.Generic.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    cmd = Command.Generic.DOWN;
                    break;
                case KeyEvent.VK_ESCAPE:
                    cmd = Command.Generic.ESC;
                    break;
                case KeyEvent.VK_ENTER:
                    cmd = Command.Generic.ENTER;
                    break;
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