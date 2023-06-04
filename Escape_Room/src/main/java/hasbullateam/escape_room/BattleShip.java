
package hasbullateam.escape_room;

import hasbullateam.escape_room.escape_room_game.BossObjectSquare;
import hasbullateam.escape_room.escape_room_game.EscapeRoom;
import hasbullateam.escape_room.escape_room_game.EscapeRoomGame;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author giuse
 */
public class BattleShip extends JPanel{
    JPanel previousPanel;
    BossObjectSquare boss;
    
    public BattleShip(){
        super();
    }

    public BattleShip(JPanel previousPanel) {
        super();
        this.previousPanel = previousPanel;
        
        SwingUtilities.invokeLater(()->{
        
            JButton btn = new JButton("lol");
            btn.addActionListener(new ActionListener(){
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    if(BattleShip.this.boss != null){
                        BattleShip.this.boss.defeated = true;
                    }
                    
                    
                    JFrame frame = (JFrame) BattleShip.this.getTopLevelAncestor();
                    if(BattleShip.this.previousPanel instanceof EscapeRoomGame){
                        EscapeRoomGame escGame = (EscapeRoomGame)BattleShip.this.previousPanel;
                        
                        frame.setContentPane(escGame);
                        frame.revalidate();
                        frame.repaint();
                        
                        BattleShip.this.previousPanel.addKeyListener( escGame.new KeyboardInput() );
                        BattleShip.this.previousPanel.requestFocus();
                    }
                    
                    
                }
            
            });
            this.add(btn);
            this.revalidate();
            this.repaint();
        });
        
    }
    
    public BattleShip (JPanel previuosPanel, BossObjectSquare boss) {
        this(previuosPanel);
        this.boss = boss;
        
    }
    
    
}
