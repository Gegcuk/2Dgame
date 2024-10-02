package object;

import java.util.List;

public class GameObjectConfig {
    private String name;
    private String imageName;
    private List<StateConfig> states;
    private boolean collisionOn;
    private int attack;
    private int defense;

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<StateConfig> getStates() {
        return states;
    }

    public void setStates(List<StateConfig> states) {
        this.states = states;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }
}
