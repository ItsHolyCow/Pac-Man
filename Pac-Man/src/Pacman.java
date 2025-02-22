import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction='U';
        int velocityX=0;
        int velocityY=0;

        Block(Image image, int x, int y, int width, int height){
            this.image=image;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.startX=x;
            this.startY=y;
        }

        void updateDirection(char direction){
            char prevDirection=this.direction;
            this.direction=direction;
            updateVelocity(); 
            this.x+=this.velocityX;
            this.y+=this.velocityY;
            for(Block wall: walls){
                if(collision(this, wall)){
                    this.x-=this.velocityX;
                    this.y-=this.velocityY;
                    this.direction=prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            if(this.direction=='U'){
                this.velocityX=0;
                this.velocityY=-tileSize/4;
            }
            else if(this.direction=='D'){
                this.velocityY=tileSize/4;
                this.velocityX=0;
            }
            else if(this.direction=='R'){
                this.velocityX=tileSize/4;
                this.velocityY=0;
            }
            else if(this.direction=='L'){
                this.velocityX=-tileSize/4;
                this.velocityY=0;
            }
        }

        void reset(){
            this.x=this.startX;
            this.y=this.startY;
        }
    }

    private int rowCount=21;
    private int colCount=19;
    private int tileSize=32;
    private int boardHeight = rowCount*tileSize;
    private int boardWidth = colCount*tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image redGhostImage;
    private Image pinkGhostImage;

    private Image rajaUpImage;
    private Image rajaDownImage;
    private Image rajaLeftImage;
    private Image rajaRightImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block raja;

    Timer gameLoop;
    char[] directions={'U', 'L', 'R', 'D'};
    Random random=new Random();
    int score=0;
    int lives=3;
    boolean gameOver=false;

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     R     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    Pacman(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();

        rajaUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        rajaDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        rajaRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        rajaLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();

        loadMap();  
        for(Block ghost: ghosts){
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameLoop=new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap(){
        walls=new HashSet<Block>();
        foods=new HashSet<Block>();
        ghosts=new HashSet<Block>();

        for(int r=0;r<rowCount;r++){
            for(int c=0;c<colCount;c++){
                String row=tileMap[r];
                char tileMapChar=row.charAt(c);

                int x=c*tileSize;
                int y=r*tileSize;

                if(tileMapChar=='X'){
                    Block wall=new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }

                else if(tileMapChar=='b'){
                    Block ghost=new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }

                else if(tileMapChar=='p'){
                    Block ghost=new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }

                else if(tileMapChar=='o'){
                    Block ghost=new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }

                else if(tileMapChar=='r'){
                    Block ghost=new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }

                else if(tileMapChar=='R'){
                    raja=new Block(rajaRightImage, x, y, tileSize, tileSize);
                }
                
                else if(tileMapChar==' '){
                    Block food=new Block(null, x+14, y+14, 4, 4);
                    foods.add(food);    
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(raja.image, raja.x, raja.y, raja.width, raja.height, null);

        for(Block Ghost: ghosts){
            g.drawImage(Ghost.image, Ghost.x, Ghost.y, Ghost.width, Ghost.height, null);
        }

        for(Block wall: walls){ 
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for(Block food: foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else{
            g.drawString("x"+String.valueOf(lives)+String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        raja.x+=raja.velocityX;
        raja.y+=raja.velocityY;

        //wall collisions
        for(Block wall:walls){
            if(collision(raja, wall)){
                raja.x-=raja.velocityX;
                raja.y-=raja.velocityY;
                break;
            }
        }

        //ghost collisions
        for(Block ghost: ghosts){
            if(collision(raja, ghost)){
                lives-=1;
                if(lives==0){
                    gameOver=true;
                    return;
                }
                resetPostions();
            }
            
            if(ghost.y==tileSize*9 && ghost.direction!='U' && ghost.direction!='D'){
                ghost.updateDirection('U');
            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall:walls){
                if(collision(ghost, wall) || ghost.x<=0 || ghost.x+ghost.width>=boardWidth){
                    ghost.x-=ghost.velocityX;
                    ghost.y-=ghost.velocityY;
                    char newDirection=directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;
                }
            }
        }

        //food collision
        Block foodEaten=null;
        for(Block food:foods){
            if(collision(raja, food)){
                foodEaten=food;
                score+=10;
            }
        }
        foods.remove(foodEaten);

        if(foods.isEmpty()){
            loadMap();
            resetPostions();
        }
    }

    public boolean collision(Block a, Block b){
        return a.x<b.x + b.width &&
        a.x+a.width>b.x &&
        a.y<b.y+b.height &&
        a.y+a.height>b.y;
    }

    public void resetPostions(){
        raja.reset();
        raja.velocityX=0;
        raja.velocityY=0;
        for(Block ghost:ghosts){
            ghost.reset();
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameOver){
            loadMap();
            resetPostions();
            lives=3;
            score=0;
            gameOver=false;
            gameLoop.restart();
        }
        
        // System.out.println("Key Event: "+e.getKeyCode());
        if(e.getKeyCode()==KeyEvent.VK_UP){
            raja.updateDirection('U');
        }

        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            raja.updateDirection('L');
        }

        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            raja.updateDirection('R');
        }

        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            raja.updateDirection('D');
        }

        if(raja.direction=='U'){
            raja.image=rajaUpImage;
        }

        else if(raja.direction=='D'){
            raja.image=rajaDownImage;
        }

        else if(raja.direction=='L'){
            raja.image=rajaLeftImage;
        }

        else if(raja.direction=='R'){
            raja.image=rajaRightImage;
        }

    }
}
