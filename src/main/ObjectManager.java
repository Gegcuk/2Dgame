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
        heart.setWorldX(10);
        heart.setWorldY(10);
        heart.setState("Full");
        gameObjects.add(heart);

//        GameObject door = objectFactory.createGameObject("Door", gamePanel);
//        door.setWorldX(10);
//        door.setWorldY(12);
//        gameObjects.add(door);

        GameObject door2 = objectFactory.createGameObject("Door", gamePanel);
        door2.setWorldX(23);
        door2.setWorldY(15);
        gameObjects.add(door2);
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
