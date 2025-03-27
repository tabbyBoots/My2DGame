package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    //screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread; // In order to repeat the process, also require to implement Runnable

    // set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // The size of this class, which is JPanel
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // all the drawing from this component will be drawn in an off-screen buffer
        this.addKeyListener(keyH);
        this.setFocusable(true); // focus on GamePanel to get key input
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
        long timer =0;
        int drawCount =0;

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
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }


        }
    }

    public void update(){
        // In JAVA, (0,0) is the top left corner.
        if( keyH.upPressed == true ){
            playerY -= playerSpeed;
        }else if( keyH.downPressed == true ){
            playerY += playerSpeed;
        }else if( keyH.leftPressed == true ){
            playerX -= playerSpeed;
        }else if( keyH.rightPressed == true ){
            playerX += playerSpeed;
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g); // draw object on the screen
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize,tileSize);
        g2.dispose();// to save some memory

    }



}
