package main;

import util.Direction;
import util.GameState;

import java.awt.*;

public class EventHandler {
    GamePanel gamePanel;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;


    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRect = new EventRect[gamePanel.maxWorldCols][gamePanel.maxWorldRows];
        int col = 0;
        int row = 0;
        while(col < gamePanel.maxWorldCols && row < gamePanel.maxWorldRows){
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 19;
            eventRect[col][row].y = 19;
            eventRect[col][row].width = 10;
            eventRect[col][row].height = 10;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            col++;
            if(col == gamePanel.maxWorldCols){
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent(){

        int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gamePanel.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent){

        if(hit(23, 17, Direction.ANY)) damagePit(23, 17, GameState.DIALOG);
        if(hit(25, 17, Direction.ANY)) damagePit(25, 17, GameState.DIALOG);
        if (hit(23, 12, Direction.UP) && gamePanel.keyHandler.enterPressed) healingPool(GameState.DIALOG);
        if (hit(27, 17, Direction.RIGHT)) teleport(GameState.DIALOG);
        }
    }

    public void drawEventRectangles(Graphics2D graphics2D) {
        // Draw the damage pit event rectangle
        drawEventRectangle(graphics2D, 27, 17, Color.BLUE);

        // Draw the healing pool event rectangle
        drawEventRectangle(graphics2D, 23, 12, Color.GREEN);
    }

    private void drawEventRectangle(Graphics2D graphics2D, int eventCol, int eventRow, Color color) {
        int eventRectX = eventCol * gamePanel.tileSize + eventRect[eventCol][eventRow].eventRectDefaultX - gamePanel.player.worldX + gamePanel.player.screenX;
        int eventRectY = eventRow * gamePanel.tileSize + eventRect[eventCol][eventRow].eventRectDefaultY - gamePanel.player.worldY + gamePanel.player.screenY;

        graphics2D.setColor(color);
        graphics2D.drawRect(eventRectX, eventRectY, eventRect[eventCol][eventRow].width, eventRect[eventCol][eventRow].height);
    }

    public boolean hit(int col, int row, Direction requiredDirection) {
        boolean hit = false;

        gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
        gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;

        eventRect[col][row].x = col * gamePanel.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gamePanel.tileSize + eventRect[col][row].y;

        if (gamePanel.keyHandler.isTestMode) {
            System.out.println("Player Position: (" + gamePanel.player.worldX + ", " + gamePanel.player.worldY + ")");
            System.out.println("Event Position: (" + col * gamePanel.tileSize + ", " + row * gamePanel.tileSize + ")");
            System.out.println("Player Direction: " + gamePanel.player.direction);
            System.out.println("Event intersects: " + gamePanel.player.solidArea.intersects(eventRect[col][row]));
        }

        // Allow interaction regardless of direction
        if (gamePanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (requiredDirection == Direction.ANY || gamePanel.player.direction.equals(requiredDirection)) {
                hit = true;

                previousEventX = gamePanel.player.worldX;
                previousEventY = gamePanel.player.worldY;
            }
        }

        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    private void damagePit(int col, int row, GameState gameState) {

        gamePanel.gameState = gameState;
        gamePanel.playSE(6);
        gamePanel.ui.currentDialogue = "You fall into a pit";
        gamePanel.player.life--;
        canTouchEvent = false;
    }

    public void healingPool(GameState gameState){
            gamePanel.gameState = gameState;
            gamePanel.player.attackCanceled = true;
            gamePanel.playSE(2);
            gamePanel.ui.currentDialogue = "You are healed";
            gamePanel.player.life = gamePanel.player.maxLife;
    }

    private void teleport(GameState gameState) {
        gamePanel.assetSetter.setMonster();
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "Teleport!";
        gamePanel.player.worldX = gamePanel.tileSize * 37;
        gamePanel.player.worldY = gamePanel.tileSize * 10;

    }
}
