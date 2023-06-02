
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import hasbullateam.escape_room.type.InventoryFullException;
import hasbullateam.escape_room.type.RoomNotFoundException;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */

// TODO: aprire le porte raccogliendo oggetti dai Container
// TODO: chiave
// TODO: sistemare gameLoop perchè non si capisce niente

public class EscapeRoomGame extends EscapeRoom{
    Thread _thread = null;
    Object lock = new Object();
    
    ObjectSquare obj;
    ContainerObjectSquare containerObj;
    MultipleChoiceDialog _dialog = null;
    Cord dropObjCord;
    DoorObjectSquare doorObj;

    public EscapeRoomGame() {
        super();
        SwingUtilities.invokeLater( ()-> {
            
            // carica la prima stanza ed il player
            this.loadRoomFromJSON("rooms\\atrio.json");
            this.refresh();
            
            startGame(); 
        });
    }
    
    //@Override
    public void gameLoop(){
        
        
        // se è stato premuto un comando valido
        if(!(this.cmd instanceof Command.Invalid)){
            
            // movimento player
            if( this.cmd instanceof Command.Move move){  
                movePlayer(move);
                
                // evidenzia l'oggetto interagibile che ha di fronte
                highlightFacingObject();
            
            // selezione inventario
            }else if (this.cmd instanceof Command.InventorySelection inventorySelection){
                selectInventory(inventorySelection);
            
            }else if(this.cmd instanceof Command.Test testCmd){ //TODO: da levare è solo per i test
                // se il path è != "null"
                // se è già presente nel file backup caricala
                // altrimenti caricala dal json
                
                if(testCmd == Command.Test.NEXT_ROOM){
                    
                    if(this.room.nextRoomName.equalsIgnoreCase("null")||
                        (this.room.nextRoomPath.equalsIgnoreCase("null"))){
                        
                        System.out.println("nessuna next room");
                        
                    }else{
                        backupRoom("rooms\\roomsBackup.dat", this.room);
                        
                        
                        try {
                            loadRoomFromBackupFile("rooms\\roomsBackup.dat", this.room.nextRoomName);
                        } catch (RoomNotFoundException ex) {
                            
                            loadRoomFromJSON(this.room.nextRoomPath);
                        }
                        
                        
                    }
                    
                }else if(testCmd == Command.Test.PREVIOUS_ROOM){
                         
                    if(this.room.previousRoomName.equalsIgnoreCase("null")||
                    (this.room.previousRoomPath.equalsIgnoreCase("null"))){

                        System.out.println("nessuna previous room");
                    
                    }else{
                        backupRoom("rooms\\roomsBackup.dat", this.room);
                        
                        
                        try {
                            loadRoomFromBackupFile("rooms\\roomsBackup.dat", this.room.previousRoomName);
                        } catch (RoomNotFoundException ex) {
                            loadRoomFromJSON(this.room.previousRoomPath);
                        }
                        
                        
                    }
                    
                }
            }
            
            else if(this.cmd instanceof Command.Generic genericCommand){
                
                if(genericCommand == Command.Generic.ESC){
                    
                    if(_dialog != null){
                        _dialog.dispose();
                        if(_dialog instanceof MultipleChoiceDialog){
                            containerObj = null;
                            dropObjCord = null;
                        }
                        _dialog = null;
                    }
                    
                    if(this.dialog != null){
                        this.dialog.dispose();
                        if(this.dialog instanceof MultipleChoiceDialog){
                            containerObj = null;
                            dropObjCord = null;
                        }
                        this.dialog = null;
                    }else{
                        // .... tutti gli altri casi in cui viene premuto ESC
                    }
                    
                }else if(_dialog != null){ // se c'è una dialog di tipo container aperta
                    
                    if(genericCommand == Command.Generic.UP){
                        _dialog.select(_dialog.selectedIndx-1);

                    }else if(genericCommand == Command.Generic.DOWN){
                        _dialog.select(_dialog.selectedIndx+1);
                    
                    // se è stato scelto un elemto dalla multiple choiche dialog
                    }else if(genericCommand == Command.Generic.ENTER){ 

                        if( containerObj != null){
                            // se la dialog è associata ad un oggeto di tipo container
                            
                            
                            try {
                                
                                this.inventory.putObjectSquare(containerObj.getFromName(_dialog.getChoice() ));
                                this.loadInventory();
                                containerObj.removeFromName( containerObj.getFromName(_dialog.getChoice()).name );
                                _dialog.dispose();
                                _dialog = null;
                                this.dialog = null;
                                
                                
                            } catch (InventoryFullException ex) {
                                
                                _dialog.dispose();
                                _dialog = null;
                                this.dialog = null;
                                this.dialog = new TextDialog(this);
                                this.dialog.show(true);
                                this.dialog.setText("   Inventario pieno .... <br>   libera uno spazio");
                            }
                            
                            
                            
                            containerObj = null;
                        
                        
                        }else{
                            
                            System.out.println(_dialog.getChoice());
                            
                            if(_dialog.getChoice().equalsIgnoreCase("sì")){
                                
                                // se è selezionato effettivamente un oggetto
                                if( !(this.inventory.getSelected().name.equalsIgnoreCase(Inventory.FREE_CONSTANT)) ){
                                    
                                    ObjectSquare selectedObj = this.inventory.getSelected().clone();
                                    selectedObj.position = dropObjCord;
                                    selectedObj.backgroundColor = new Color(0,0,0,0);
                                    selectedObj.isInteractable = true;
                                    
                                    // rendi selectedObj un oggetto raccoglibile

                                    this.room.addObject( selectedObj );
                                    this.loadObjectSquare( this.room.getObject(dropObjCord) );

                                    this.inventory.removeObject( this.inventory.selected );
                                    this.inventory.select(this.inventory.selected);
                                    loadInventory();       
                                            
                                            
                                }else{
                                    System.out.println("nessun oggetto da droppare");
                                }
                                
                                
                            }
                            
                            _dialog.dispose();
                            _dialog = null;
                            this.dialog = null;
                            dropObjCord = null;
                        }
                        
                        
                        
                    }
                
                }
            }
            
            else{
                // ottiene l'oggetto che il player ha difronte
                obj = this.room.getFacingObject();
                
                
                if(this.cmd instanceof Command.Player _cmd){
                    if(_cmd == Command.Player.DROP_OBJECT){
                        
                        if(_dialog != null){
                            _dialog.dispose();
                            _dialog = null;
                        }
                        if(this.dialog != null){
                            this.dialog.dispose();
                            this.dialog = null;
                        }
                        
                        if( isCordInGrid( this.room.getFacingCord()) && obj==null ){ // se è possibile droppare l'oggetto
                            _dialog = new MultipleChoiceDialog(this);
                            _dialog.setBrief("Desideri lasciare l'oggetto selezionato davanti a te?");
                            _dialog.setChoices("Sì","No");
                            _dialog.assembleText();
                            _dialog.show(true);
                            _dialog.reWriteText(true);
                            dropObjCord = this.room.getFacingCord();
                        }else{
                            dialog = new TextDialog(this);
                            dialog.show(true);
                            dialog.setText("non è possibile lasciare l'oggetto davanti a te perchè sembra ci sia un ostacolo ....");
                        }
                        
                        
                    }
                }
                

                if(obj != null){
                    
                    if(obj instanceof ContainerObjectSquare _obj){
                        

                        if(this.cmd instanceof Command.Player _cmd){
                            if(_cmd == Command.Player.INTERACT){
                                
                                if(this.dialog == null){
                                    containerObj = _obj;
                                    this.dialog = new MultipleChoiceDialog(this);
                                    _dialog = (MultipleChoiceDialog)dialog;


                                    _dialog.setBrief( _obj.getBrief() );
                                    _dialog.setChoices( _obj.getObjectsString().toArray(new String[_obj.objectList.size()]) );
                                    _dialog.assembleText();
                                    _dialog.show(true);
                                    _dialog.reWriteText(true);  
                                }
                                
                                    
                            }
                        }
                    
                    }else if(obj instanceof DoorObjectSquare _obj){
                        if(!_obj.isOpen){
                            
                            if(this.cmd instanceof Command.Player _cmd){
                                if(_cmd == Command.Player.INTERACT){
                                    
                                    if(this.dialog == null){
                                        
                                        this.dialog = new TextDialog(this);
                                        this.dialog.show(true);
                                        
                                        this.dialog.setText(_obj.message);
                                        
                                    }
                                }
                            }
                            
                            
                        }else{
                            
                            if(this.cmd instanceof Command.Player _cmd){
                                if(_cmd == Command.Player.INTERACT){
                                    _obj.setBackgroundColor(new Color(0,0,0,0));
                                    Direction oldDirection = this.room.playerDirection;
                                    backupRoom("rooms\\roomsBackup.dat", this.room);
                                    
                                    if(_obj.isExit){
                                        
                                        try {
                                            loadRoomFromBackupFile("rooms\\roomsBackup.dat", this.room.nextRoomName);
                                            this.player.setFaceDirection(oldDirection);
                                        } catch (RoomNotFoundException ex) {
                                            loadRoomFromJSON(this.room.nextRoomPath);
                                            
                                        }
                                    }else{
                                        try {
                                            loadRoomFromBackupFile("rooms\\roomsBackup.dat", this.room.previousRoomName);
                                            this.player.setFaceDirection(oldDirection);
                                        } catch (RoomNotFoundException ex) {
                                            loadRoomFromJSON(this.room.previousRoomPath);
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            }
            
            
            
            this.refresh();
            
            // resetta il comando
            this.cmd = Command.Invalid.NONE;
        }   
    } 
}


/*if(_thread!=null && _thread.isAlive()){
                System.out.println("Notificato");
                synchronized (lock) {
                    lock.notify();
                    
                    //while (_thread.isAlive()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EscapeRoomGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    //}
                }
                System.out.println("aooooo");
                
            }*/



/*
// quando cambia il valore di cmd awake
                                    _thread = new Thread(()->{
                                        
                                        synchronized (lock) {
                                            while(this.cmd != Command.Generic.ENTER && _dialog!=null){
                                            
                                                System.out.println(this.cmd);

                                                if(this.cmd == Command.Generic.UP){
                                                    _dialog.select(_dialog.selectedIndx-1);

                                                }else if(this.cmd == Command.Generic.DOWN){
                                                    _dialog.select(_dialog.selectedIndx+1);
                                                }

                                                try {
                                                    lock.notify(); // al primo ciclo notifica chi ??
                                                    lock.wait();
                                                } catch (InterruptedException ex) {
                                                    break;
                                                }
                                            }    
                                            
                                            System.out.println("fuori");
                                            if(_dialog!=null){
                                                System.out.println(_dialog.getChoice());
                                                
                                                
                                                _dialog.dispose();
                                                
                                                this.dialog = null;
                                                
                                            }
                                            
                                            System.out.println("palleeeeeeeeeee");
                                            lock.notifyAll();
                                        }
                                       
                                        
                                        
                                    }
                                    );
                                    
                                    
                                    if( _thread == null || !_thread.isAlive()){
                                        _thread.start();
                                        loopThread.
                                        //System.out.println("cazzoooooooooooo");
                                    }

*/