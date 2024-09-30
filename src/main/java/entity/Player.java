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
        getPlayerAttackImage();
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
        up1 = setup("/player/Walking sprites/boy_up_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = setup("/player/Walking sprites/boy_up_2", gamePanel.tileSize, gamePanel.tileSize);
        down1 = setup("/player/Walking sprites/boy_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = setup("/player/Walking sprites/boy_down_2", gamePanel.tileSize, gamePanel.tileSize);
        left1 = setup("/player/Walking sprites/boy_left_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = setup("/player/Walking sprites/boy_left_2", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/player/Walking sprites/boy_right_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/player/Walking sprites/boy_right_2", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/Attacking sprites/boy_attack_up_1", gamePanel.tileSize, gamePanel.tileSize*2);
        attackUp2 = setup("/player/Attacking sprites/boy_attack_up_2", gamePanel.tileSize, gamePanel.tileSize*2);
        attackDown1 = setup("/player/Attacking sprites/boy_attack_down_1", gamePanel.tileSize, gamePanel.tileSize*2);
        attackDown2 = setup("/player/Attacking sprites/boy_attack_down_2", gamePanel.tileSize, gamePanel.tileSize*2);
        attackLeft1 = setup("/player/Attacking sprites/boy_attack_left_1", gamePanel.tileSize*2, gamePanel.tileSize);
        attackLeft2 = setup("/player/Attacking sprites/boy_attack_left_2", gamePanel.tileSize*2, gamePanel.tileSize);
        attackRight1 = setup("/player/Attacking sprites/boy_attack_right_1", gamePanel.tileSize*2, gamePanel.tileSize);
        attackRight2 = setup("/player/Attacking sprites/boy_attack_right_2", gamePanel.tileSize*2, gamePanel.tileSize);
    }

    public void update(){

        if(attacking){
            attacking();
        }

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

    private void attacking() {
        spriteCounter++;
        if(spriteCounter <= 5) spriteNum = 1;
        if(spriteCounter > 5 && spriteNum <= 25) spriteNum = 2;
        if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }


    public void pickUpObject(int index){
        String objectName = "empty";
        if(index != 999){

        }
    }

    private void interactNPC(int npcIndex) {
        if(keyHandler.enterPressed){
            if(npcIndex != 999){
                gamePanel.gameState = gamePanel.dialogState;
                gamePanel.npc[npcIndex].speak();
            } else attacking = true;
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
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case UP -> {
                if(!attacking) {
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                }
                if(attacking){
                    tempScreenY = screenY - gamePanel.tileSize;
                    if (spriteNum == 1) image = attackUp1;
                    if (spriteNum == 2) image = attackUp2;
                }
            }
            case DOWN -> {
                if(!attacking) {
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                }
                if(attacking){
                    if (spriteNum == 1) image = attackDown1;
                    if (spriteNum == 2) image = attackDown2;
                }
            }
            case LEFT -> {
                if(!attacking) {
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                }
                if(attacking){
                    tempScreenX = screenX - gamePanel.tileSize;
                    if (spriteNum == 1) image = attackLeft1;
                    if (spriteNum == 2) image = attackLeft2;
                }
            }
            case RIGHT -> {
                if(!attacking) {
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                }
                if(attacking){
                    if (spriteNum == 1) image = attackRight1;
                    if (spriteNum == 2) image = attackRight2;
                }
            }
        }

        if(invincible){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        graphics2D.drawImage(image, tempScreenX, tempScreenY,null);

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //DEBUGGING
        if(keyHandler.isTestMode){
            graphics2D.setColor(Color.RED);
            graphics2D.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
