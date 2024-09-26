package main;

import object.GameObject;
import object.GameObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class ObjectManager {

    private GamePanel gamePanel;
    private GameObjectFactory objectFactory;

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    private List<GameObject> gameObjects;

    public ObjectManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.objectFactory = new GameObjectFactory();
        this.gameObjects = new ArrayList<>();
    }

    public void loadObjects(){
        GameObject heart = objectFactory.createGameObject("Heart", gamePanel);
        heart.worldX = 10 * gamePanel.tileSize;
        heart.worldY = 10 * gamePanel.tileSize;
        heart.setState("Full");
        gameObjects.add(heart);
    }

    public void updateHeartState(GameObject heart, int playerHealth){
        if(playerHealth >= 2){
            heart.setState("Full");
        } else if(playerHealth == 1){
            heart.setState("Half");
        } else if(playerHealth == 0){
            heart.setState("Empty");
        }
    }

}
