package entity;

import main.GamePanel;
import main.KeyHandler;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends Entity{
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;

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
        direction = "down";
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

        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed){

            if(keyHandler.upPressed){
                direction = "up";
            }
            else if(keyHandler.downPressed){
                direction = "down";
            }
            else if(keyHandler.leftPressed){
                direction = "left";
            }
            else if(keyHandler.rightPressed){
                direction = "right";
            }

            collision = false;
            gamePanel.collisionChecker.checkTile(this);

            int objectIndex = gamePanel.collisionChecker.checkObject(this, true);
            pickUpObject(objectIndex);


            if(collision == false){
                switch (direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 10){
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int index){
        String objectName = "empty";
        if(index != 999){

        }
    }

    public void draw(Graphics2D graphics2D){

        BufferedImage image = null;

        switch (direction){
            case "up":
                if(spriteNum == 1) image = up1;
                if(spriteNum == 2) image = up2;
                break;
            case "down":
                if(spriteNum == 1) image = down1;
                if(spriteNum == 2) image = down2;
                break;
            case "left":
                if(spriteNum == 1) image = left1;
                if(spriteNum == 2) image = left2;
                break;
            case "right":
                if(spriteNum == 1) image = right1;
                if(spriteNum == 2) image = right2;
                break;
        }
        graphics2D.drawImage(image, screenX, screenY,null);
    }
}
