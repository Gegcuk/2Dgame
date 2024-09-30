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

    public GamePanel gamePanel;
    public int worldX, worldY;
    public int speed;
    public String name;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public Direction direction = ANY;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter;
    public String[] dialogue = new String[20];
    public int dialogueIndex = 0;

    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int type;
    public boolean attacking = false;

    public Entity(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }


    public void setAction(){}
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

            graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

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
