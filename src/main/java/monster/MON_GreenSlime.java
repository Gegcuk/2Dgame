package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

import static util.Direction.*;
import static util.Direction.RIGHT;

public class MON_GreenSlime extends Entity {
    public MON_GreenSlime(GamePanel gamePanel) {
        super(gamePanel);

        name = "Green Slime";
        speed = 1;
        maxLife = 30;
        life = maxLife;
        type = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {

        up1 = setup("/monsters/greenslime_down_1");
        up2 = setup("/monsters/greenslime_down_2");
        down1 = setup("/monsters/greenslime_down_1");
        down2 = setup("/monsters/greenslime_down_2");
        left1 = setup("/monsters/greenslime_down_1");
        left2 = setup("/monsters/greenslime_down_2");
        right1 = setup("/monsters/greenslime_down_1");
        right2 = setup("/monsters/greenslime_down_2");


    }

    @Override
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) direction = UP;
            if (i > 25 && i <= 50) direction = DOWN;
            if (i > 50 && i <= 75) direction = LEFT;
            if (i > 75 && i <= 100) direction = RIGHT;
            actionLockCounter = 0;
        }
    }

    @Override
    public void damageReaction(){
        actionLockCounter = 0;
         direction = gamePanel.player.direction;
    }
}
