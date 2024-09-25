package entity;

import main.GamePanel;
import util.Direction;

import java.util.Random;

import static util.Direction.*;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gamePanel){
        super(gamePanel);
        direction = Direction.DOWN;
        speed = 1;

        getImage();
    }

    public void getImage(){
        up1 = setup("/NPC/oldman_up_1");
        up2 = setup("/NPC/oldman_up_2");
        down1 = setup("/NPC/oldman_down_1");
        down2 = setup("/NPC/oldman_down_2");
        left1 = setup("/NPC/oldman_left_1");
        left2 = setup("/NPC/oldman_left_2");
        right1 = setup("/NPC/oldman_right_1");
        right2 = setup("/NPC/oldman_right_2");
    }

    @Override
    public void setAction() {

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
}
