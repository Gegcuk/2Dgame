package entity;

import main.GamePanel;
import main.UtilityTool;
import util.Direction;
import util.Renderable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static util.Direction.*;
import static util.Direction.LEFT;

public class Entity implements Renderable {

    // Constants and Static Imports
    public static final int DEFAULT_WIDTH = 48;
    public static final int DEFAULT_HEIGHT = 48;

    // Core Components
    public GamePanel gamePanel;
    public Direction direction = ANY;

    // Position and Movement
    public int worldX, worldY;
    public int speed;

    // Collision Areas
    public Rectangle solidArea = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;

    // Graphics and Animation
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Entity Attributes
    public String name;
    public int type;
    public int maxLife;
    public int life;

    // State Flags
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;

    // Counters and Timing
    public int actionLockCounter;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;

    // Dialogue
    public String[] dialogue = new String[20];
    public int dialogueIndex = 0;

    public Entity(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void setAction(){}

    public void damageReaction(){

    }

    public void speak(){
        if(dialogue[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gamePanel.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gamePanel.player.direction){
            case UP -> direction=DOWN;
            case DOWN -> direction=UP;
            case LEFT -> direction=RIGHT;
            case RIGHT -> direction=LEFT;
        }
    }

    public void update(){
        setAction();

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        boolean contactPlayer = gamePanel.collisionChecker.checkPlayer(this);

        if(type == 2 && contactPlayer){
            if(!gamePanel.player.invincible){
                gamePanel.playSE(6);
                gamePanel.player.life--;
                gamePanel.player.invincible = true;
            }
        }

        if(!collisionOn){
            switch (direction){
                case UP: worldY -= speed; break;
                case DOWN: worldY += speed; break;
                case LEFT: worldX -= speed; break;
                case RIGHT: worldX += speed; break;
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

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible=false;
                invincibleCounter = 0;
            }
        }
    }

    @Override
    public int getWorldX() {
        return this.worldX;
    }

    @Override
    public int getWorldY() {
        return this.worldY;
    }

    @Override
    public int getScreenX() {
        return worldX - gamePanel.player.worldX + gamePanel.player.screenX;
    }

    @Override
    public int getScreenY() {
        return worldY - gamePanel.player.worldY + gamePanel.player.screenY;
    }

    @Override
    public int getHeight() {
        return gamePanel.tileSize;
    }

    @Override
    public void draw(Graphics2D graphics2D) {

        BufferedImage image = null;

        int screenX = getScreenX();
        int screenY = getScreenY();

        if(onScreen()){

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
                default -> {
                    image = down1;
                }
            }

            if (type == 2) {

                int barLength = (int) (gamePanel.tileSize * ((double) life / maxLife));

                if (barLength != gamePanel.tileSize) {
                    graphics2D.setColor(new Color(35, 35, 35));
                    graphics2D.fillRect(screenX - 1, screenY - 11, gamePanel.tileSize + 2, 12);
                    graphics2D.setColor(new Color(255, 0, 30));
                    graphics2D.fillRect(screenX, screenY - 10, barLength, 10);

                }
            }

            if(invincible) graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if(dying) dyingAnimation(graphics2D);

            graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }
    }

    private void dyingAnimation(Graphics2D graphics2D) {
        dyingCounter++;
        int dyingLength = 5;
        if(dyingCounter <= dyingLength){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        }
        if(dyingCounter <= dyingLength * 2 && dyingCounter > dyingLength){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }
        if(dyingCounter <= dyingLength * 3 && dyingCounter > dyingLength * 2){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        }
        if(dyingCounter <= dyingLength * 4 && dyingCounter > dyingLength * 3){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }
        if(dyingCounter <= dyingLength * 5 && dyingCounter > dyingLength * 4){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        }
        if(dyingCounter <= dyingLength * 6 && dyingCounter > dyingLength * 5){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }
        if(dyingCounter <= dyingLength * 7 && dyingCounter > dyingLength * 6){
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        }

        if(dyingCounter > dyingLength * 7){
            dying = false;
            alive = false;
        }

    }

    private boolean onScreen() {
        return worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY;
    }

    public BufferedImage setup(String imagePath){
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage scaledImage = null;

        try{
            scaledImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath +".png")));
            scaledImage = utilityTool.scaleImage(scaledImage, gamePanel.tileSize, gamePanel.tileSize);
        } catch (IOException e){
            e.printStackTrace();
        }
        return scaledImage;
    }

    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage scaledImage = null;

        try{
            scaledImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath +".png")));
            scaledImage = utilityTool.scaleImage(scaledImage, width, height);
        } catch (IOException e){
            e.printStackTrace();
        }
        return scaledImage;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX * gamePanel.tileSize;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY * gamePanel.tileSize;
    }
}
