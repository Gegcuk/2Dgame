package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

    GamePanel gamePanel;
    Font arial_40, arial_50B;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter;
    public boolean gameFinished = false;
    double playTime;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        arial_40 = new Font("Colibri", Font.ITALIC, 30);
        arial_50B = new Font("Colibri", Font.BOLD, 50);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    public void showMessage(String text){

        message = text;
        messageOn = true;

    }

    public void draw(Graphics2D graphics2D){
        if(gameFinished){

            graphics2D.setFont(arial_40);
            graphics2D.setColor(Color.white);

            String text;
            int textLength;
            int x;
            int y;

            text = "You found the treasure!";
            textLength = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
            x = gamePanel.screenWidth/2 - textLength/2;
            y = gamePanel.screenHeight/2 - gamePanel.tileSize*3;
            graphics2D.drawString(text, x, y);

            graphics2D.setFont(arial_50B);
            graphics2D.setColor(Color.YELLOW);
            text = "YOU FINISHED LEVEL 1!";
            textLength = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
            x = gamePanel.screenWidth/2 - textLength/2;
            y = gamePanel.screenHeight/2 + gamePanel.tileSize*3;
            graphics2D.drawString(text, x, y);

            gamePanel.gameThread = null;

        } else {

            graphics2D.setFont(arial_40);
            graphics2D.setColor(Color.white);
            graphics2D.drawImage(keyImage, gamePanel.tileSize/2, gamePanel.tileSize/2, gamePanel.tileSize, gamePanel.tileSize, null);
            graphics2D.drawString("x " + gamePanel.player.hasKey, 74, 60);

            playTime += (double) 1/60;
            graphics2D.drawString("Time: " + decimalFormat.format(playTime), gamePanel.tileSize * 11, 60);

            if(messageOn) {
                graphics2D.setFont(graphics2D.getFont().deriveFont(30F));
                graphics2D.drawString(message, gamePanel.tileSize/2, gamePanel.tileSize*2);

                messageCounter++;

                if(messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }

    }

}
