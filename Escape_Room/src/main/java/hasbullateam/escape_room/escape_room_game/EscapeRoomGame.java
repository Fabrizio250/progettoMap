
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Command;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */
public class EscapeRoomGame extends EscapeRoom{
    Thread _thread = null;
    Object lock = new Object();
    
    ObjectSquare obj;
    MultipleChoiceDialog _dialog = null;

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
            
            }else if(this.cmd instanceof Command.Generic genericCommand){
                
                if(genericCommand == Command.Generic.ESC){
                    
                    if(this.dialog != null && this.dialog.isVisible()){
                        this.dialog.dispose();
                        this.dialog = null;
                    }else{
                        // .... tutti gli altri casi in cui viene premuto ESC
                    }
                    
                }else if(_dialog != null){
                    
                    if(genericCommand == Command.Generic.UP){
                        _dialog.select(_dialog.selectedIndx-1);

                    }else if(genericCommand == Command.Generic.DOWN){
                        _dialog.select(_dialog.selectedIndx+1);
                    }else if(genericCommand == Command.Generic.ENTER){

                        System.out.println(_dialog.getChoice());                            
                        _dialog.dispose();
                        this.dialog = null; // => _dialog = null
                    }
                }
            }
            
            else{
                // ottiene l'oggetto che il player ha difronte
                obj = this.room.getFacingObject();

                if(obj != null){
                    
                    if(obj instanceof ContainerObjectSquare _obj){

                        if(this.cmd instanceof Command.Player _cmd){
                            if(_cmd == Command.Player.INTERACT){
                                
                                if(this.dialog == null){
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