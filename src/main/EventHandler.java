package main;

import util.Direction;

import java.awt.*;

public class EventHandler {
    GamePanel gamePanel;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;


    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        eventRect = new Rectangle();
        eventRect.x = 19;
        eventRect.y = 19;
        eventRect.width = 10;
        eventRect.height = 10;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent(){
//        if(hit(27, 17, Direction.RIGHT)) damagePit(gamePanel.dialogState);
        if (hit(23, 12, Direction.UP) && gamePanel.keyHandler.enterPressed) healingPool(gamePanel.dialogState);
        if (hit(27, 17, Direction.RIGHT)) teleport(gamePanel.dialogState);
    }



    public void drawEventRectangles(Graphics2D graphics2D) {
        // Draw the damage pit event rectangle
        drawEventRectangle(graphics2D, 27, 17, Color.BLUE);

        // Draw the healing pool event rectangle
        drawEventRectangle(graphics2D, 23, 12, Color.GREEN);
    }


    private void drawEventRectangle(Graphics2D graphics2D, int eventCol, int eventRow, Color color) {
        int eventRectX = eventCol * gamePanel.tileSize + eventRectDefaultX - gamePanel.player.worldX + gamePanel.player.screenX;
        int eventRectY = eventRow * gamePanel.tileSize + eventRectDefaultY - gamePanel.player.worldY + gamePanel.player.screenY;

        graphics2D.setColor(color);
        graphics2D.drawRect(eventRectX, eventRectY, eventRect.width, eventRect.height);
    }



    public boolean hit(int eventCol, int eventRow, Direction requiredDirection) {
        boolean hit = false;

        gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
        gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;

        eventRect.x = eventCol * gamePanel.tileSize + eventRect.x;
        eventRect.y = eventRow * gamePanel.tileSize + eventRect.y;

        if (gamePanel.keyHandler.isTestMode) {
            System.out.println("Player Position: (" + gamePanel.player.worldX + ", " + gamePanel.player.worldY + ")");
            System.out.println("Event Position: (" + eventCol * gamePanel.tileSize + ", " + eventRow * gamePanel.tileSize + ")");
            System.out.println("Player Direction: " + gamePanel.player.direction);
        }

        // Allow interaction regardless of direction
        if (gamePanel.player.solidArea.intersects(eventRect)) {
            if (requiredDirection == Direction.ANY || gamePanel.player.direction.equals(requiredDirection)) {
                hit = true;
            }
        }

        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    private void damagePit(int gameState) {

        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "You fall into a pit";
        gamePanel.player.life--;

    }

    public void healingPool(int gameState){
            gamePanel.gameState = gameState;
            gamePanel.ui.currentDialogue = "You are healed";
            gamePanel.player.life = gamePanel.player.maxLife;
    }

    private void teleport(int gameState) {

        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "Teleport!";
        gamePanel.player.worldX = gamePanel.tileSize * 37;
        gamePanel.player.worldY = gamePanel.tileSize * 10;

    }
}
