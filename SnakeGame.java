import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel  implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardwidth;
    int boardheight;
    int titlesize=25;

    //snake
    Tile snakehead;
    ArrayList<Tile> snakebody;
    //Food
    Tile Food;
    Random random;
    //logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameover=false;

    SnakeGame(int boardwidth,int boardheight){
        this.boardwidth=boardwidth;
        this.boardheight=boardheight;
        setPreferredSize(new Dimension(this.boardwidth,this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakehead =new Tile(5,5);
        snakebody = new ArrayList<Tile>();
        Food =new Tile(10,10);
        random=new Random();
        placefood();
        velocityX=0;
        velocityY=0;
        gameloop=new Timer(100,this);
        gameloop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        /*Grid
        for(int i=0;i<boardwidth/titlesize;i++){
            g.drawLine(i*titlesize, 0, i*titlesize, boardheight);
            g.drawLine(0, i*titlesize, boardwidth, i*titlesize);
        }*/

        //food
        g.setColor(Color.red);
        g.fillRect(Food.x*titlesize, Food.y*titlesize, titlesize, titlesize);

        //snake Head
        g.setColor(Color.blue);
        g.fillRect(snakehead.x*titlesize, snakehead.y*titlesize, titlesize, titlesize);
        //snake Body
        for(int i=0;i<snakebody.size();i++){
            Tile snakepart=snakebody.get(i);
            g.fillRect(snakepart.x*titlesize, snakepart.y*titlesize, titlesize, titlesize);
        }
        //score
        g.setFont(new Font("Times New Roman",Font.PLAIN,16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game Over:"+String.valueOf(snakebody.size()), titlesize-16, titlesize);
        }
        else{
            g.drawString("score:"+String.valueOf(snakebody.size()), titlesize-16, titlesize);
        }
    }
    public void placefood(){
        Food.x=random.nextInt(boardwidth/titlesize);
        Food.y=random.nextInt(boardheight/titlesize);
    }


    public boolean collision(Tile t1,Tile t2){
        return t1.x==t2.x && t1.y==t2.y;
    }


    public void move(){

        //To eat food
        if(collision(snakehead, Food)){
            snakebody.add(new Tile(Food.x,Food.y));
            placefood();
        }
        //To increase snakebody
        for(int i=snakebody.size()-1;i>=0;i--){
            Tile snakepart=snakebody.get(i);
            if(i==0){
                snakepart.x=snakehead.x;
                snakepart.y=snakehead.y;
            }
            else{
                Tile prevsnakepart=snakebody.get(i-1);
                snakepart.x=prevsnakepart.x;
                snakepart.y=prevsnakepart.y;
            }

        }

        // snake Head
        snakehead.x+=velocityX;
        snakehead.y+=velocityY;

        //game over conditions
        for(int i=0;i<snakebody.size();i++){
            Tile snakepart=snakebody.get(i);
            if(collision(snakehead, snakepart)){
                gameover=true;
            }
        }
        if(snakehead.x*titlesize<0 || snakehead.x*titlesize>boardwidth || snakehead.y*titlesize<0|| snakehead.y*titlesize>boardheight){
            gameover=true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
       repaint();
       if(gameover){
        gameloop.stop();
       }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if((e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1)){
            velocityX=0;
            velocityY=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
        
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
       
    }
}
