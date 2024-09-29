package entity;

import main.GamePanel;
import main.KeyHandler;
import util.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends Entity{
    public KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        super(gamePanel);
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize/2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = gamePanel.originalTileSize;
        solidArea.y = gamePanel.originalTileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gamePanel.originalTileSize;
        solidArea.height = gamePanel.originalTileSize;
        System.out.println(solidArea.x + " " + solidArea.y + " " + solidArea.width + " " + solidArea.height);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        speed = 4;
        direction = Direction.DOWN;

        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up1 = setup("/player/Walking sprites/boy_up_1");
        up2 = setup("/player/Walking sprites/boy_up_2");
        down1 = setup("/player/Walking sprites/boy_down_1");
        down2 = setup("/player/Walking sprites/boy_down_2");
        left1 = setup("/player/Walking sprites/boy_left_1");
        left2 = setup("/player/Walking sprites/boy_left_2");
        right1 = setup("/player/Walking sprites/boy_right_1");
        right2 = setup("/player/Walking sprites/boy_right_2");
    }

    public void update(){

        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed){

            if(keyHandler.upPressed){
                direction = Direction.UP;
            }
            else if(keyHandler.downPressed){
                direction = Direction.DOWN;
            }
            else if(keyHandler.leftPressed){
                direction = Direction.LEFT;
            }
            else if(keyHandler.rightPressed){
                direction = Direction.RIGHT;
            }

            collisionOn = false;

            gamePanel.collisionChecker.checkTile(this);

            int objectIndex = gamePanel.collisionChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
            interactNPC(npcIndex);

            int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
            contactMonster(monsterIndex);
            gamePanel.eventHandler.checkEvent();

            if(!collisionOn && !keyHandler.enterPressed){
                switch (direction) {
                    case Direction.UP -> worldY -= speed;
                    case Direction.DOWN -> worldY += speed;
                    case Direction.LEFT -> worldX -= speed;
                    case Direction.RIGHT -> worldX += speed;
                }
            }

            if(!keyHandler.enterPressed)spriteCounter++;

            if(spriteCounter > 10){
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            gamePanel.keyHandler.enterPressed = false;
        }
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 30) {
                invincible=false;
                invincibleCounter = 0;
            }
        }
        if(keyHandler.isTestMode){
            System.out.println("Invincible counter = " + invincibleCounter);
        }
    }


    public void pickUpObject(int index){
        String objectName = "empty";
        if(index != 999){

        }
    }

    private void interactNPC(int npcIndex) {
        if(npcIndex != 999){
            if(gamePanel.keyHandler.enterPressed){
                gamePanel.gameState = gamePanel.dialogState;
                gamePanel.npc[npcIndex].speak();
            }
        }
    }

    private void contactMonster(int monsterIndex) {
        if (monsterIndex != 999){
            if(!invincible) {
                life--;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D graphics2D){

        BufferedImage image = null;

        switch (direction) {
            case UP -> {
                if (spriteNum == 1) image = up1;
                if (spriteNum == 2) image = up2;
            }
            case DOWN -> {
                if (spriteNum == 1) image = down1;
                if (spriteNum == 2) image = down2;
            }
            case LEFT -> {
                if (spriteNum == 1) image = left1;
                if (spriteNum == 2) image = left2;
            }
            case RIGHT -> {
                if (spriteNum == 1) image = right1;
                if (spriteNum == 2) image = right2;
            }
        }

        if(invincible){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        graphics2D.drawImage(image, screenX, screenY,null);

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //DEBUGGING
        if(keyHandler.isTestMode){
            graphics2D.setColor(Color.RED);
            graphics2D.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
