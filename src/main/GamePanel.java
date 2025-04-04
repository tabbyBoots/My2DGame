package main;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // World Setting
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // System setting
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);


    Thread gameThread; // In order to repeat the process, also require to implement Runnable

    // Entity and Object
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];



    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // The size of this class, which is JPanel
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // All the drawing from this component will be drawn in an off-screen buffer
        this.addKeyListener(keyH);
        this.setFocusable(true); // focus on GamePanel to get key input
    }
    public void setupGame(){
        aSetter.setObject();
        playMusic(0);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // automatically call run() method
    }

//    @Override
//    public void run() {
//        //Game Loop ******* Core of this game.
//
//        // Keep FPS : method 1 : sleep
//        double drawInterval = 1000000000/FPS; // 1B nano sec = 1 sec
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while( gameThread != null ){
//            // System.out.println("The game loop is still running.");
//            long currentTime = System.nanoTime();
//
//            // This Loop will do the following 2 things.
//            // 1 UPDATE: update information such as charactor positions
//            update();
//            // 2 DRAW: draw the screen with the updated information
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime / 1000000;
//                if( remainingTime < 0 ){
//                    remainingTime = 0;
//                }
//                Thread.sleep( (long) remainingTime ); // sleep only accept long millisecond
//                nextDrawTime += drawInterval;
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void run() {
        // Keep FPS : method 2 : Delta/Accumulator
        double drawInterval = 1000000000/FPS; // 1B nano sec = 1 sec
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while( gameThread != null ){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if( delta >= 1 ){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if( timer >= 1000000000 ){
                //System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g); // draw object on the screen
        Graphics2D g2 = (Graphics2D)g;
        // TILE
        tileM.draw(g2);
        // OBJECT

        for(int i = 0; i < obj.length; i++){
            if( obj[i] != null ){
                obj[i].draw(g2, this);
            }
        }
        // PLAYER
        player.draw(g2);

        // UI
        ui.draw(g2);

        g2.dispose();// to save some memory
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
