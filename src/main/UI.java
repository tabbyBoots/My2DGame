package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font ariel_40, ariel_80B;
    BufferedImage keyImage;
    public boolean messageON = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    public UI(GamePanel gp){
        this.gp = gp;
        ariel_40 = new Font("Arial", Font.PLAIN, 40);
        ariel_80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    public void showMeggage(String text){
        message = text;
        messageON = true;
    }

    public void draw(Graphics2D g2){

        if(gameFinished == true){
            g2.setFont(ariel_40);
            g2.setColor(Color.white);

            String text;
            int textLength = 0;
            int x;
            int y;
            text = "You found the treasure!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);

            g2.setFont(ariel_80B);
            g2.setColor(Color.yellow);
            text = "Congratulation! You won!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);

            gp.gameThread = null; // GAME STOP
        }else{
            g2.setFont(ariel_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);// position is from baseline of text !!!

            // Message
            if( messageON == true ){
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message , gp.tileSize/2, gp.tileSize*5);//+ gp.player.hasKey
                messageCounter++;
                if(messageCounter > 120){// FPS=60 so 120 means 2 seconds
                    messageCounter = 0;
                    messageON = false;
                }
            }
        }

    }
}
