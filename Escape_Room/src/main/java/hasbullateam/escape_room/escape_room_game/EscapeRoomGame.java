
package hasbullateam.escape_room.escape_room_game;


import hasbullateam.escape_room.Menu;

import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import hasbullateam.escape_room.type.Direction;
import hasbullateam.escape_room.type.InventoryFullException;
import hasbullateam.escape_room.type.RoomNotFoundException;

import java.awt.Color;

import javax.swing.JFrame;

import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */

public class EscapeRoomGame extends EscapeRoom{
    
    CollectableObjectSquare obj_collectable = null;
    MultipleChoiceDialog dialog_collectable = null;
    
    ContainerObjectSquare obj_container = null;
    MultipleChoiceDialog dialog_container = null;
    TextDialog dialog_invFull = null;
    
    TextDialog dialog_door = null; // TODO: renderlo dialog_generic
    
    MultipleChoiceDialog dialog_drop_obj_chose = null;
    TextDialog dialog_drop_obj_impossible = null;
    Cord cord_drop_obj = null;
    
    MultipleChoiceDialog dialog_boss = null;
    public BossObjectSquare obj_boss = null;
    
    TextDialog dialog_generic = null;
    
    NumericKeypadObjectSquare obj_numericKeypad = null;
    NumericKeypadDialog numericKeyDialog = null;
    
    MultipleChoiceDialog dialog_backToMenu = null;
     

    public EscapeRoomGame() {
        super();
        
        SwingUtilities.invokeLater( ()-> {
            this.setWindowSize();
            // carica la prima stanza ed il player
            this.loadRoomFromJSON("rooms\\atrio.json");
            this.refresh();
            startGame(); 
        });
    }
    
    @Override
    public void gameLoop(){
        
        highlightFacingObject();
        
        if(obj_boss != null && obj_boss.isDefeated() && this.dialog == null){
            
            // visualizza dialog oh no mi hai sconfitto
            // sposta boss in maniera tale che liberi il passaggio alla porta
            
            dialog_generic = new TextDialog(this);
            dialog_generic.setText(obj_boss.defeatMessage);
            
            this.room.removeObject(obj_boss.position);
            this.clearSquare(obj_boss.position);
            obj_boss.position = obj_boss.defeatPosition;
            this.room.addObject(obj_boss);
            this.loadObjectSquare(obj_boss);
            
            
            this.dialog = dialog_generic;
            obj_boss = null;
            
        }
        
        
        if ( numericKeyDialog != null && obj_numericKeypad != null && this.dialog == null) {
            if(numericKeyDialog.isExit()){
                dialog_generic = new TextDialog(this);
                if(numericKeyDialog.goalReached){
                    
                    dialog_generic.setText(obj_numericKeypad.successMessage);
                    
                    // apri la porta
                    
                    // setta obj_numericKeypad non interagibile
                    
                }else{
                    dialog_generic.setText(obj_numericKeypad.failMessage);
                }
                this.dialog = dialog_generic;
                obj_numericKeypad = null;
                numericKeyDialog = null;
            }
        }
        
        
        
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
                            dialog_backToMenu = new MultipleChoiceDialog(this);
                            dialog_backToMenu.setBrief("desideri tornare al menu principale ?");
                            dialog_backToMenu.setChoices("sì","no");
                            dialog_backToMenu.assembleText();
                            dialog_backToMenu.reWriteText(true);
                            
                            this.dialog = dialog_backToMenu;
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
                        
                        if(dialog_backToMenu != null){
                            
                            if(dialog_backToMenu.getChoice().equals("sì")){
                                removeAllDialog();
                                resetHighlightFObjcet();
                                changePanel(new Menu());
                                
                            }
                            removeAllDialog();
                            
                            
                        }else if( dialog_collectable != null ){
                            
                            // TODO: 
                            
                            if( dialog_collectable.getChoice().equals("sì") ){
                                

                                try {
                                    // metti nell'inventario
                                    this.inventory.putObjectSquare(obj_collectable.clone());
                                    loadInventory();
                                    
                                    this.removeObjectSquare(obj_collectable);
                                    resetHighlightFObjcet();

                                } catch (InventoryFullException ex) {
                                    System.out.println("inventario pieno");
                                }
                                
                                
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
                                JFrame frame = (JFrame) this.getTopLevelAncestor();
                                removeKeyListener(this.getKeyListeners()[0]);
                                frame.setContentPane(obj_boss.getMinigame(this));
                                frame.revalidate();
                                frame.repaint();
                                removeAllDialog();
                                
                            }else{
                                removeAllDialog();
                                dialog_generic = new TextDialog(this);
                                dialog_generic.setText(obj_boss.loserMessage);
                                this.dialog = dialog_generic;
                            }
                            
                            
                            
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
                            
                            
                            case NumericKeypadObjectSquare _numericKeypadObj -> {
                                
                                if(this.dialog == null && this.numericKeyDialog == null){
                                    
                                    numericKeyDialog = new NumericKeypadDialog(this, _numericKeypadObj.goalString);
                                    obj_numericKeypad = _numericKeypadObj;
                                }
                                break;
                            }
                            
                            case BreakableObjectSquare _breakableObj -> {
                                
                                if(this.dialog == null && _breakableObj.isBroken==false){
                                    
                                    if(this.inventory.getSelected().name.equals(_breakableObj.breakableWith_objName)){
                                        
                                        // rompi
                                        // visualizza brokeMessage
                                        
                                        _breakableObj.broke();
                                        this.loadObjectSquare(_breakableObj);
                                        
                                        dialog_generic = new TextDialog(this);
                                        dialog_generic.setText(_breakableObj.getBrokeMessage());
                                        
                                        this.dialog = dialog_generic;
                                        
                                        
                                    }else{
                                        
                                        // text dialog con message
                                        dialog_generic = new TextDialog(this);
                                        dialog_generic.setText(_breakableObj.message);
                                        
                                        this.dialog = dialog_generic;
                                        
                                    }
                                    
                                }
                                
                                
                                break;
                            }

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
                            
                            case BrokenDoorObjectSquare _brokenDoorObj -> {
                                
                                if(this.dialog == null){
                                        
                                    if( this.inventory.getSelected().name.equals(_brokenDoorObj.fixableWith_nameObj) ){
                                        // aggiusta la porta
                                        // rimpiazza la porta rotta con la porta aggiustata

                                        this.room.removeObject(_brokenDoorObj.position);
                                        this.clearSquare(_brokenDoorObj.position);

                                        this.room.addObject(_brokenDoorObj.fixedDoor);
                                        this.loadObjectSquare(_brokenDoorObj.fixedDoor);

                                        dialog_generic = new TextDialog(this);
                                        dialog_generic.setText(_brokenDoorObj.fixedDoor_message);
                                        this.dialog = dialog_generic;

                                    }else{
                                        // visualizza dialog che dice di trovare qualcosa per aggiustarla
                                        dialog_generic = new TextDialog(this);
                                        dialog_generic.setText(_brokenDoorObj.brokeDoor_message);
                                        this.dialog = dialog_generic;

                                    }
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
                                    
                                    if(!_bossObj.isDefeated()){
                                        
                                        dialog_boss = new MultipleChoiceDialog(this);
                                        dialog_boss.setBrief(_bossObj.entryMessage);
                                        dialog_boss.setChoices("sì","no");
                                        dialog_boss.assembleText();
                                        dialog_boss.reWriteText(true);

                                        obj_boss = _bossObj;
                                        this.dialog = dialog_boss; 
                                    
                                    }else{
                                        
                                        dialog_generic = new TextDialog(this);
                                        dialog_generic.setText(_bossObj.defeatMessage);
                                        this.dialog = dialog_generic;
                                        
                                    }
                                    
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
        dialog_generic = null;
        dialog_backToMenu = null;
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
        
        }else if(dialog_backToMenu != null){
            return dialog_backToMenu;
        }
        
        return null;
    }
}
