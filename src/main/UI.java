package main;

import java.awt.*;
import java.text.DecimalFormat;

public class UI {

    Graphics2D graphics2D;
    GamePanel gamePanel;
    Font colibri_40, colibri_50B, colibri_32P;
    public boolean messageOn = false;
    public String message = "";
    public String currentDialogue = "";

    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        colibri_40 = new Font("Colibri", Font.ITALIC, 30);
        colibri_50B = new Font("Colibri", Font.BOLD, 50);
        colibri_32P = new Font("Colibri", Font.PLAIN, 32);
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
        if(gamePanel.gameState == gamePanel.dialogState){
            drawDialogueScreen();
        }
    }



    public void drawPauseScreen(){
        String text = "PAUSED";

        int x = getCenterOfTextX(text);
        int y = gamePanel.screenHeight/2;

        graphics2D.drawString(text, x, y);
    }

    private void drawDialogueScreen() {

        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize/2;
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
        int height = gamePanel.tileSize * 4;

        drawSubWindow(x, y, width, height);

        graphics2D.setFont(colibri_32P);
        x += gamePanel.tileSize;
        y += gamePanel.tileSize;
        for (String line: currentDialogue.split("\n")){
            graphics2D.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color color = new Color(0, 0, 0, 220);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x, y, width, height, 35,35);
    }

    public int getCenterOfTextX(String text){
        int length = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth/2 - length/2;
        return x;
    }
}
