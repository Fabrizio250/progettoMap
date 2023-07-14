package hasbullateam.minigame.ping_pong;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    int id;     //1 = player1, 2 = player2
    int yVelocity;
    int speed = 10;

    Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
        super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
        this.id = id;
    }

    public void keyPressed(KeyEvent e){
        switch (id){
            case 1:
                if(e.getKeyCode()==KeyEvent.VK_W){
                    setYDirection(-speed);
                    move();
                }
                if(e.getKeyCode()==KeyEvent.VK_S){
                    setYDirection(speed);
                    move();
                }
                break;
            case 2:
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    setYDirection(-speed);
                    move();
                }
                if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    setYDirection(speed);
                    move();
                }
                break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (id){
            case 1:
                if(e.getKeyCode()==KeyEvent.VK_W){
                    setYDirection(0);
                    move();
                }
                if(e.getKeyCode()==KeyEvent.VK_S){
                    setYDirection(0);
                    move();
                }
                break;
            case 2:
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    setYDirection(0);
                    move();
                }
                if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    setYDirection(0);
                    move();
                }
                break;
        }


    }

    public void setYDirection(int yDirection){
        yVelocity = yDirection;

    }

    public void move(){
        y = y + yVelocity;
    }
    
    public void automaticMove(int ballY){
        if (ballY < y){
            setYDirection(-speed);
            y = y + yVelocity;
        }
        if (ballY == y){
            setYDirection(0);
            y = y + yVelocity;
        }
        if (ballY > y){
            setYDirection(speed);
            y = y + yVelocity;
        }
    }

    public void draw(Graphics g){
        if (id == 1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.fillRect(x,y,width,height);
    }
}
