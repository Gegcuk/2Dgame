package main;

import monster.MON_GreenSlime;
import npc.NPC_OldMan;


public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void setObject(){

    }

    public void setNPC(){
        gamePanel.npc[0] = new NPC_OldMan(gamePanel);
        gamePanel.npc[0].setWorldX(9);
        gamePanel.npc[0].setWorldY(10);
    }

    public void setMonster(){
        gamePanel.monsters[0] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[0].setWorldX(22);
        gamePanel.monsters[0].setWorldY(13);

        gamePanel.monsters[1] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[1].setWorldX(24);
        gamePanel.monsters[1].setWorldY(13);

    }


}
