
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Command;
import hasbullateam.escape_room.type.Cord;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class EscapeRoom extends JPanel{
    static final int GRID_SIZE = 10;
    SquarePanel matrix[][];
    PlayerSquarePanel player;
    ImageManager backgroundImage;
    
    public EscapeRoom() {
        //------------- init GridPanel -------------
        super();
        matrix = new SquarePanel[GRID_SIZE][GRID_SIZE];
        this.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE));
        initMatrix();
        this.setFocusable(true);
        this.addAncestorListener(new RequestFocusListener());
        this.addKeyListener(new KeyboardInput());
        
        //-------- stanza ------
        Room atrio = new Room("images\\prigione.jpg", new Cord(GRID_SIZE/2, GRID_SIZE/2),"images\\player2.png");
        atrio.addObject("lol", new ObjectSquare(new Cord( 4,4 ), "images\\player.png" )  );
        loadRoom(atrio);
        
        setSquare(new SquarePanel(new Cord(6,6), Color.red));

    }
    
    public void startGame(){
        
    }
    
    public void loadRoom(Room room){
        // carica background
        this.backgroundImage = new ImageManager(room.backGroundPath);
        
        // carica object
        for(ObjectSquare obj: room.objects.values()){
            loadObjectSquare(obj);
        }
        
        // carica player
        player = new PlayerSquarePanel( room.playerStarPosition, room.playerPathImage);
        player.setOccupiedSquare(getMatrixSquare(player.position));
        setSquare(player);
        
        //changePlayerPosition(room.playerStarPosition);
    }
    
    public void loadObjectSquare(ObjectSquare obj){
        this.setSquare(new SquarePanel(obj.position, obj.pathImage) );
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //------- disegna background ---------
        this.backgroundImage.resizeOnFirstDrawn(this.getWidth(), this.getHeight());
        g.drawImage(backgroundImage.getImage(), 0, 0, null);
        //------------------------------------
    }
    
    private void movePlayer(Command command){
        Cord newPosition = player.position.clone();
        
        if( command == Command.MOVE_UP ){
            newPosition.y--;
        }
        if( command == Command.MOVE_DOWN ){
            newPosition.y++;
        }
        if( command == Command.MOVE_LEFT ){
            newPosition.x--;
        }
        if( command == Command.MOVE_RIGHT ){
            newPosition.x++;
        }
        
        changePlayerPosition(newPosition);
    }
    
    
    private void changePlayerPosition(Cord newPostion){
        
        if (newPostion.x < 0){newPostion.x = 0;}
        if (newPostion.y < 0){newPostion.y = 0;}
        if (newPostion.x > GRID_SIZE-1){newPostion.x = GRID_SIZE-1;}
        if (newPostion.y > GRID_SIZE-1){newPostion.y = GRID_SIZE-1;}
        
        setSquare(player.occupiedSquare);
        player.setOccupiedSquare( getMatrixSquare(newPostion).clone() );
        player.position = newPostion;
        setSquare(player);
        
        revalidate();
    }
    
    
    // imposta l'indirizzo newPanel nel matrix e lo aggiunge al gridLayout nelle posizione newPanel.position
    private void setSquare(SquarePanel newPanel){
        int indx = newPanel.position.getIndex(GRID_SIZE);
        this.remove(indx);
        this.add(newPanel, indx); 
        setMatrixSquare(newPanel);
    }
    
    private void initMatrix(){
        Random rd = new Random();
        for(int y = 0; y<GRID_SIZE; y++){
            for(int x = 0; x<GRID_SIZE; x++){
                
                setMatrixSquare(  new SquarePanel( new Cord(x,y)));
                //new Color( rd.nextInt(100), rd.nextInt(100), rd.nextInt(100) ) ));
                
                this.add( getMatrixSquare(x, y), getMatrixSquare(x, y).position.getIndex(GRID_SIZE) );
            }
        }
    }
    
    // ritorna l'indirizzo dello SquarePanel presente nel matrix a cord
    private SquarePanel getMatrixSquare(Cord cord){
        return getMatrixSquare(cord.x, cord.y);
    }
    private SquarePanel getMatrixSquare(int x, int y){
        return this.matrix[y][x];
    }
    
    // setta nel matrix l'indirizzo di square alla posizione square.position
    private void setMatrixSquare(SquarePanel square){
        this.matrix[ square.position.y ][ square.position.x ] = square;
    }
    
    
    private class KeyboardInput implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            System.out.println(key);
            
            Command cmd = Command.NONE;
            
            if(key == 'w'){
                cmd = Command.MOVE_UP;
            }
            if (key == 's'){
                cmd = Command.MOVE_DOWN;
            }
            if (key == 'a'){
                cmd = Command.MOVE_LEFT;
            }
            if (key == 'd'){
                cmd = Command.MOVE_RIGHT;
            }
            
            movePlayer(cmd);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }
    
    
    private static class RequestFocusListener implements javax.swing.event.AncestorListener {
        @Override
        public void ancestorAdded(javax.swing.event.AncestorEvent e) {
            JComponent component = e.getComponent();
            component.requestFocusInWindow();
            component.removeAncestorListener(this);
        }

        @Override
        public void ancestorMoved(javax.swing.event.AncestorEvent e) {}

        @Override
        public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
    }   
    
}
