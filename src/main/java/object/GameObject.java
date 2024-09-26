package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameObject {

    public Map<String, BufferedImage> stateImages;
    public String currentState;
    public String name;
    public boolean collisionOn = false;
    public int worldX, worldY;
    public Rectangle solidArea;
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public UtilityTool utilityTool = new UtilityTool();

    public GameObject(GameObjectConfig config, GamePanel gamePanel){
        this.name = config.getName();
        this.collisionOn = config.isCollisionOn();
        this.solidArea = new Rectangle(0, 0, gamePanel.tileSize, gamePanel.tileSize);
        this.stateImages = new HashMap<>();

        if(config.getStates() != null && !config.getStates().isEmpty()){
            loadStateImages(config.getStates(), gamePanel.tileSize);
            currentState = config.getStates().getFirst().getStateName();
        } else {
            loadSingleImage(config.getImageName(), gamePanel.tileSize);
            currentState = "default";
        }
    }

    private void loadStateImages(List<StateConfig> states, int tileSize) {
        for(StateConfig state : states){
            try{
                BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/" + state.getImageName())));
                image = utilityTool.scaleImage(image, tileSize, tileSize);
                stateImages.put(state.getStateName(), image);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void loadSingleImage(String imageName, int tileSize) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/" + imageName)));
            image = utilityTool.scaleImage(image, tileSize, tileSize);
            stateImages.put("default", image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setState(String stateName){
        if(stateImages.containsKey(stateName)){
            currentState = stateName;
        } else {
            System.err.println("state " + stateName + " not found for object" + name);
        }
    }


    public void draw(Graphics2D graphics2D, GamePanel gamePanel){

        BufferedImage image = stateImages.get(currentState);
        if(image == null){
            System.err.println("No image found for current state " + currentState + " of object " + name);
            return;
        }

        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if(worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
            worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
            worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
            worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY){

            graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }

}
