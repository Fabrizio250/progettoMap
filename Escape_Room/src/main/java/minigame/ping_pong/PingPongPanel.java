package minigame.ping_pong;

import minigame.MiniGame;
import hasbullateam.escape_room.escape_room_game.BossObjectSquare;
import hasbullateam.escape_room.escape_room_game.RequestFocusListener;
import hasbullateam.escape_room.type.BossStatus;
import hasbullateam.escape_room.type.GameMode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingPongPanel extends MiniGame implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    
    public PingPongPanel(JPanel parentPanel, GameMode gameMode, BossObjectSquare bossObj){
        super(parentPanel, gameMode, bossObj);
        
        this.newPaddles();
        this.newBall();
        score = new Score(GAME_WIDTH,GAME_HEIGHT);
        

        this.setFocusable(true);

        this.addAncestorListener(new RequestFocusListener());

        
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(Color.black);
        this.revalidate();

        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public PingPongPanel(JPanel parentPanel, GameMode gameMode){
        this(parentPanel, gameMode, null);
    }

    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }
    

    @Override
    public void paint(Graphics g){
        
        image = this.createImage(this.getWidth(),this.getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
        
    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
        Toolkit.getDefaultToolkit().sync(); //help with the animation
    }

    public void move(){
        //1 VS 1
        if (this.gameMode == GameMode.MODE_1v1){
            paddle1.move();
            paddle2.move();
        }
        
        //1 VS CPU o Storia
        if (this.gameMode == GameMode.MODE_1vCPU || this.gameMode == gameMode.MODE_STORIA){
            paddle1.move();
            paddle2.automaticMove(ball.y);
        }
        ball.move();
    }
    

    public void checkCollition(){
        //bounce ball off top & bottom window edges
        if(ball.y <=0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        //bounce ball off paddles
        if(ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        //stops paddles at window edges
        if(paddle1.y<=0)
            paddle1.y=0;
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
        //give a player 1 point and creates new paddles & ball
        if(ball.x <=0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: "+score.player2);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.player1);
        }
        
        if(score.player1 == 1 || score.player2 == 1){
            
            
            
            try {
                SwingUtilities.invokeAndWait(()->{
                    this.changeToParentPanel();
                });
                     
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(PingPongPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(PingPongPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(score.player1 > score.player2){
                if(gameMode == GameMode.MODE_STORIA){
                    this.bossObj.bossStatus = BossStatus.PLAYER_WIN;
                }
            }else{
                if(gameMode == GameMode.MODE_STORIA){
                    this.bossObj.bossStatus = BossStatus.PLAYER_LOSE;
                }
            }
            
            gameThread.interrupt();
            
        }

    }
  
    
    public void endGame(){

            this.changeToParentPanel();
            System.out.println("-----------lol 1");
            this.gameThread.interrupt();

        
    }

    public void run(){
        
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true){
            
            try{
                long now = System.nanoTime();
                delta += (now - lastTime)/ns;
                lastTime = now;
                if (delta >= 1 ){
                    this.move();
                    this.checkCollition();
                    this.repaint();
                    delta--;
                }
                Thread.sleep(Duration.ZERO);
            }catch(InterruptedException e){
                System.out.println("interrotto!!");
                break;
            }

        }
    }
    
    

    
    /**Inner class che fa da Action Listener**/
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);

        }

        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }

    }
}
