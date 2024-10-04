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
        gamePanel.npc[0].setWorldX(23);
        gamePanel.npc[0].setWorldY(19);
    }

    public void setMonster(){

        int i = 0;
        gamePanel.monsters[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[i].setWorldX(22);
        gamePanel.monsters[i].setWorldY(13);
        i++;
        gamePanel.monsters[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[i].setWorldX(24);
        gamePanel.monsters[i].setWorldY(13);
        i++;
        gamePanel.monsters[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[i].setWorldX(21);
        gamePanel.monsters[i].setWorldY(38);
        i++;
        gamePanel.monsters[i] = new MON_GreenSlime(gamePanel);
        gamePanel.monsters[i].setWorldX(23);
        gamePanel.monsters[i].setWorldY(42);

    }


}
