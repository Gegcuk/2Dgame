package main;

import java.awt.*;
import java.text.DecimalFormat;

public class UI {

    Graphics2D graphics2D;
    GamePanel gamePanel;
    Font colibri_40, colibri_50B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter;
    public boolean gameFinished = false;
    double playTime;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        colibri_40 = new Font("Colibri", Font.ITALIC, 30);
        colibri_50B = new Font("Colibri", Font.BOLD, 50);
    }

    public void showMessage(String text){

        message = text;
        messageOn = true;

    }

    public void draw(Graphics2D graphics2D){
        this.graphics2D = graphics2D;
        graphics2D.setFont(colibri_50B);
        graphics2D.setColor(Color.white);

        if(gamePanel.gameState == gamePanel.playState){

        }
        if(gamePanel.gameState == gamePanel.pauseState){
            drawPauseScreen();
        }
    }

    public void drawPauseScreen(){
        String text = "PAUSED";

        int x = getCenterOfTextX(text);
        int y = gamePanel.screenHeight/2;

        graphics2D.drawString(text, x, y);
    }

    public int getCenterOfTextX(String text){
        int length = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth/2 - length/2;
        return x;
    }
}
