
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.BattleShip;
import hasbullateam.escape_room.Tris;
import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import hasbullateam.escape_room.type.InventoryFullException;
import hasbullateam.escape_room.type.RoomNotFoundException;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */

// TODO: "non è presente nessun oggetto" nel container


public class EscapeRoomGame extends EscapeRoom{
    
    CollectableObjectSquare obj_collectable = null;
    MultipleChoiceDialog dialog_collectable = null;
    
    ContainerObjectSquare obj_container = null;
    MultipleChoiceDialog dialog_container = null;
    TextDialog dialog_invFull = null;
    
    TextDialog dialog_door = null;
    
    MultipleChoiceDialog dialog_drop_obj_chose = null;
    TextDialog dialog_drop_obj_impossible = null;
    Cord cord_drop_obj = null;
    
    MultipleChoiceDialog dialog_boss = null;
    public BossObjectSquare obj_boss = null;
    
     

    public EscapeRoomGame() {
        super();
        SwingUtilities.invokeLater( ()-> {
            
            // carica la prima stanza ed il player
            this.loadRoomFromJSON("rooms\\atrio.json");
            this.refresh();
            
            startGame(); 
        });
    }
    
    @Override
    public void gameLoop(){
        
        highlightFacingObject();
        
        switch(this.cmd){
            
            // ------------------------------------------------------------------
            case Command.Invalid cmd_invalid -> {break;}
            
            case Command.Move cmd_move -> {
            
                this.movePlayer(cmd_move);
                
                break;
            
            }
            
            case Command.InventorySelection cmd_inventory -> {
                
                selectInventory(cmd_inventory);
                break; 
            }
            // ----------------------------------------------------------- Generic
            
            case Command.Generic cmd_generic -> {
                
                switch (cmd_generic) {
                    
                    case ESC -> {
                        
                        // se c'è una dialog qualsiasi aperta chiudila
                        // altrimenti ...
                        
                        if(this.dialog != null){
                            // chiudi tutte le dialog
                            removeAllDialog();

                        }else{  
                            // ... visualizza menu di uscita 
                        }
                        
                        
                        break;
                    }
                    
                    case UP -> {
                        
                        MultipleChoiceDialog dialogMultiChoice_generic = getActiveMultiChoiceDialog();

                        if(dialogMultiChoice_generic != null){
                            dialogMultiChoice_generic.select(dialogMultiChoice_generic.selectedIndx-1);
                        }
                        
                        break;
                    }
                    
                    
                    case DOWN -> {
                        
                        MultipleChoiceDialog dialogMultiChoice_generic = getActiveMultiChoiceDialog();

                        if(dialogMultiChoice_generic != null){
                            dialogMultiChoice_generic.select(dialogMultiChoice_generic.selectedIndx+1);
                        }
                        
                        
                        break;
                    }
                    
                    
                    case ENTER -> {
                        if( dialog_collectable != null ){
                            
                            // TODO: 
                            
                            if( dialog_collectable.getChoice().equals("sì") ){
                                // rimuovi dalla room
                                this.room.removeObject(obj_collectable.position);
                                this.setSquare(new SquarePanel(obj_collectable.position));

                                try {
                                    // metti nell'inventario
                                    this.inventory.putObjectSquare(obj_collectable);

                                } catch (InventoryFullException ex) {
                                    System.out.println("inventario pieno");
                                }
                                
                                loadInventory();
                            }
                            
                            obj_collectable = null;
                            removeAllDialog();
                            
                        }else if( dialog_container != null){ // --------------------------------------------------------- Enter Dialog Container
                            // setta nell'inventario l'oggetto selezionato dal container
                            try {
                                
                                this.inventory.putObjectSquare(obj_container.getFromName(dialog_container.getChoice() ));
                                this.loadInventory();
                                obj_container.removeFromName( obj_container.getFromName(dialog_container.getChoice()).name );
                                removeAllDialog();
                                
                            } catch (InventoryFullException ex) {
                                
                                removeAllDialog();
                                dialog_invFull = new TextDialog(this);
                                dialog_invFull.setText("   Inventario pieno .... <br>   libera uno spazio");
                                this.dialog = dialog_invFull;
                            
                            }
                            
                            
                        }else if ( dialog_drop_obj_chose != null){ // ----------------------------------------------------------- Enter Dialog Drop Obj
                            
                            if(dialog_drop_obj_chose.getChoice().equalsIgnoreCase("sì")){
                                
                                // se è selezionato effettivamente un oggetto
                                if( !(this.inventory.getSelected().name.equalsIgnoreCase(Inventory.FREE_CONSTANT)) ){
                                    
                                    ObjectSquare selectedObj = this.inventory.getSelected().clone();
                                    selectedObj.position = cord_drop_obj;
                                    selectedObj.backgroundColor = new Color(0,0,0,0);
                                    selectedObj.isInteractable = true;
                                    this.room.addObject( selectedObj );
                                    this.loadObjectSquare( this.room.getObject(cord_drop_obj) );
                                    this.inventory.removeObject( this.inventory.selected );
                                    this.inventory.select(this.inventory.selected);
                                    loadInventory();       
                                            
                                            
                                }else{
                                    System.out.println("nessun oggetto da droppare");
                                }
                            }
                            removeAllDialog();  
                        
                        
                        
                        }else if (dialog_boss != null){
                            
                            if(dialog_boss.getChoice().equalsIgnoreCase("sì")){
                                backupRoom(BACKUP_FILE_PATH, this.room);
                                JFrame frame = (JFrame) this.getTopLevelAncestor();
                                removeKeyListener(this.getKeyListeners()[0]);
                                frame.setContentPane(obj_boss.getMinigame(this));
                                frame.revalidate();
                                frame.repaint();

                            }else{
                                System.out.println("pff pivello");
                            }
                            
                            removeAllDialog();
                            
                        }
                        
                        break;
                    }
                    
                    
                }
                
                
                
                break;
            }
            // ----------------------------------------------------------- Player
            case Command.Player cmd_player -> {
                
                switch (cmd_player) {
                    
                    // -------------------------------------------------- Intercat
                    case INTERACT -> {
                        
                        switch( this.room.getFacingObject() ){
                            
                            
                            
                            case null -> {break;}

                            case ContainerObjectSquare _containerObj -> {
                                
                                // --------------------------------------------------------------------------------------------- Container Interact
                                /*
                                    se non c'è nessuna dialog visibile
                                    setta e mostra la dialog relativa al container
                                    setta obj_container
                                */
                                
                                
                                if(this.dialog == null){
                                    dialog_container = new MultipleChoiceDialog(this);
                                    dialog_container.setBrief( _containerObj.getBrief() );
                                    dialog_container.setChoices( _containerObj.getObjectsString().toArray(
                                            new String[_containerObj.objectList.size()]) );
                                    dialog_container.assembleText();
                                    dialog_container.reWriteText(true);  
                                    this.dialog = dialog_container;
                                    obj_container = _containerObj;
                                }
                                break;
                            }
                            
                            case DoorObjectSquare _doorObj -> { // ------------------------------------------------------------ Door Intercat
                                
                                // se porta aperta fallo passare alla prossima stanza
                                // se la porta è chiusa mostra dialog_door
                                
                                if(_doorObj.isOpen){
                                    // passa all'altra stanza
                                    
                                    _doorObj.setBackgroundColor(new Color(0,0,0,0));
                                    Direction oldDirection = this.room.playerDirection;
                                    resetHighlightFObjcet();
                                    backupRoom(this.BACKUP_FILE_PATH, this.room);
                                    
                                    if(_doorObj.isExit){
                                        
                                        try {
                                            
                                            loadRoomFromBackupFile(this.BACKUP_FILE_PATH, this.room.nextRoomName);
                                            this.player.setFaceDirection(oldDirection);
                                        } catch (RoomNotFoundException ex) {
                                            loadRoomFromJSON(this.room.nextRoomPath);
                                            
                                        }
                                    }else{
                                        
                                        try {
                                            
                                            loadRoomFromBackupFile(this.BACKUP_FILE_PATH, this.room.previousRoomName);
                                            this.player.setFaceDirection(oldDirection);
                                        } catch (RoomNotFoundException ex) {
                                            loadRoomFromJSON(this.room.previousRoomPath);
                                        }
                                    }
                                    
                                    
                                    
                                }else{
                                    // apri la porta se hai selezionato la chiave
                                    // mostra dialog se non è già presente una
                                    if(this.dialog == null){
                                        
                                        if (this.inventory.getSelected() instanceof KeyObjectSquare){
                                            // apri la porta
                                            _doorObj.open();
                                            loadObjectSquare(_doorObj);
                                            refresh();
                                            // rimuovi la chiave dall'inventario
                                            this.inventory.removeObject(this.inventory.selected);
                                            loadInventory();
                                            // mostra dialog porta aperta
                                            dialog_door = new TextDialog(this);
                                            dialog_door.setText("la porta è stata aperta !");
                                            this.dialog = dialog_door;
                                        
                                        }else{
                                            dialog_door = new TextDialog(this);
                                            dialog_door.setText(_doorObj.message);
                                            this.dialog = dialog_door;
                                        
                                        }
                                    }
                                    
                                    
                                }
                                
                                
                                
                                
                                
                                break;
                            }
                            
                            case CollectableObjectSquare _collectableObj -> { // ------------------------------------------ Intercat Collectable
                                
                                // setta e mostra dialog_collectable
                                // setta obj_collectable
                                
                                if(this.dialog == null){
                                    dialog_collectable = new MultipleChoiceDialog(this);
                                    dialog_collectable.setBrief("desidere raccogliere l'oggetto davanti a te?");
                                    dialog_collectable.setChoices("sì","no");
                                    dialog_collectable.assembleText();
                                    dialog_collectable.reWriteText(true);


                                    this.dialog = dialog_collectable;
                                    obj_collectable = _collectableObj;
                                }
                                
                                
                                break;
                            }
                            
                            
                            
                            case BossObjectSquare _bossObj ->{ // ------------------------------------------------------- Intercat Boss
                                
                                if(this.dialog == null){
                                    dialog_boss = new MultipleChoiceDialog(this);
                                    dialog_boss.setBrief(_bossObj.message);
                                    dialog_boss.setChoices("sì","no");
                                    dialog_boss.assembleText();
                                    dialog_boss.reWriteText(true);

                                    obj_boss = _bossObj;
                                    this.dialog = dialog_boss; 
                                }
                                
                                break;
                            }
                            
                            
                            default -> {break;}
 
                        }
                        
                        
                        break;
                    }

                    // ------------------------------------------------------------------------------------------------------ Drop Object Dialog
                    case DROP_OBJECT -> {
                        // setta dialog_drop_obj_chose
                        if(this.dialog == null){
                            
                            
                            if(isCordInGrid( this.room.getFacingCord()) && this.room.getFacingObject()==null){
                                dialog_drop_obj_chose = new MultipleChoiceDialog(this);
                                dialog_drop_obj_chose.setBrief("  Desideri lasciare l'oggetto selezionato davanti a te?");
                                dialog_drop_obj_chose.setChoices("sì","no");
                                dialog_drop_obj_chose.assembleText();
                                dialog_drop_obj_chose.reWriteText(true);
                                this.dialog = dialog_drop_obj_chose;
                                cord_drop_obj = this.room.getFacingCord();
                            }else{
                                dialog_drop_obj_impossible = new TextDialog(this);
                                dialog_drop_obj_impossible.setText("impossibile lasciare l'oggetto davanti a te perchè sembra esserci un'ostacolo");
                                this.dialog = dialog_drop_obj_impossible;
                            } 
                        }
                        break;
                    }
                       
                }
                
                
                break;
            }
            

            default -> {break;} 
        }
        
        
        
        this.cmd = Command.Invalid.NONE;
        this.refresh();
        
    }
    
    private void removeAllDialog(){
        if(this.dialog != null){
            this.dialog.dispose();
            this.dialog = null;
        }

        dialog_collectable = null;
        dialog_container = null;
        dialog_door = null;
        dialog_drop_obj_chose = null;
        dialog_drop_obj_impossible = null;
        dialog_invFull = null;
        dialog_boss = null;
    }
    
    
    private MultipleChoiceDialog getActiveMultiChoiceDialog(){
        
        if(this.dialog == null){
            return null;
            
        }else if(dialog_collectable != null){
            return dialog_collectable;

        }else if(dialog_container != null){
            return dialog_container;

        }else if(dialog_drop_obj_chose != null){
            return dialog_drop_obj_chose;
        
        }else if(dialog_boss != null){
            return dialog_boss;
        }
        
        return null;
    }
}
    
    /*
    Thread _thread = null;
    Object lock = new Object();
    
    ObjectSquare obj;
    ContainerObjectSquare containerObj;
    MultipleChoiceDialog _dialog = null;
    MultipleChoiceDialog dialogCollectable = null;
    Cord dropObjCord;
    DoorObjectSquare doorObj;
    CollectableObjectSquare collectableObj;
    */
    
    
    /*
    //@Override
    public void gamesLoop(){
        
        
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
                    
                }else if(dialogCollectable != null){
                    
                    if(genericCommand == Command.Generic.UP){
                        dialogCollectable.select(dialogCollectable.selectedIndx-1);
                    
                    }else if(genericCommand == Command.Generic.DOWN){
                        dialogCollectable.select(dialogCollectable.selectedIndx+1);
                        
                    }else if(genericCommand == Command.Generic.ENTER){
                        
                        if( dialogCollectable.getChoice().equals("si") ){
                            // rimuovi dalla room
                            this.room.removeObject(collectableObj.position);
                            this.setSquare(new SquarePanel(collectableObj.position));
                            
                            try {
                                // metti nell'inventario
                                
                                this.inventory.putObjectSquare(collectableObj);
                                
                            } catch (InventoryFullException ex) {
                                System.out.println("inventario pieno");
                            }
                            
                            
                            loadInventory();
                            this.revalidate();
                            this.repaint();
                            
                            dialogCollectable.dispose();
                            dialogCollectable = null;
                            this.dialog = null;
                            
                            collectableObj = null;
                            
                            
                        }else if(dialogCollectable.getChoice().equals("no")){
                            dialogCollectable.dispose();
                            dialogCollectable = null;
                            this.dialog = null;
                            
                            collectableObj = null;
                        }
                        
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
                    
                    
                    if (obj instanceof CollectableObjectSquare _obj){
                        
                        if(this.cmd instanceof Command.Player _cmd){
                            if(_cmd == Command.Player.INTERACT){
                                
                                if(this.dialog == null){
                                    
                                    this.dialog = new MultipleChoiceDialog(this);
                                    dialogCollectable = (MultipleChoiceDialog)dialog;
                                    dialogCollectable.setBrief("desideri raccogliere l'oggetto?");
                                    dialogCollectable.setChoices("si","no");
                                    dialogCollectable.assembleText();
                                    dialogCollectable.show(true);
                                    dialogCollectable.reWriteText(true);
                                    
                                    collectableObj = _obj;
                                    
                                    
                                }
                                
                                
                            }
                            
                            
                        }
                        
                        
                        
                    }else if(obj instanceof ContainerObjectSquare _obj){
                        

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
    }*/



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