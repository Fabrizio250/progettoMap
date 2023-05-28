
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class GridPanel extends JPanel{
    private SquarePanel matrix[][];
    private int size;
    private ImageManager backgroundImage;

    public GridPanel( int size ) {
        super();
        this.size = size;
        
        matrix = new SquarePanel[this.size][this.size];
        this.setLayout(new GridLayout(this.size,this.size));
        initMatrix();
        this.setFocusable(true);
        this.addAncestorListener(new RequestFocusListener());
    
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(this.backgroundImage != null){
            this.backgroundImage.resizeOnFirstDrawn(this.getWidth(), this.getHeight());
            g.drawImage(backgroundImage.getImage(), 0, 0, null);
        }  
    }
    
    public void setBackgroundImage(String pathImage){
        this.backgroundImage = new ImageManager(pathImage);
    }
    
    
    public void initMatrix(){
        Random rd = new Random();
        for(int y = 0; y<this.size; y++){
            for(int x = 0; x<this.size; x++){
                setMatrixSquare(  new SquarePanel( new Cord(x,y)));
                this.add( getMatrixSquare(x, y), getMatrixSquare(x, y).position.getIndex(this.size) );
            }
        }
    }
    
    // ritorna l'indirizzo dello SquarePanel presente nel matrix a cord
    public SquarePanel getMatrixSquare(Cord cord){
        return getMatrixSquare(cord.x, cord.y);
    }
    public SquarePanel getMatrixSquare(int x, int y){
        return this.matrix[y][x];
    }
    
    // setta nel matrix l'indirizzo di square alla posizione square.position
    public void setMatrixSquare(SquarePanel square){
        this.matrix[ square.position.y ][ square.position.x ] = square;
    }
    
    // imposta l'indirizzo newPanel nel matrix e lo aggiunge al gridLayout nelle posizione newPanel.position
    public void setSquare(SquarePanel newPanel){
        int indx = newPanel.position.getIndex(this.size);
        this.remove(indx);
        this.add(newPanel, indx); 
        setMatrixSquare(newPanel);
        this.repaint();
    }
    
    public void clearSquare(Cord cord){
        this.setSquare(new SquarePanel(cord) );
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
