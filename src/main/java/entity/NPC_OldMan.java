package entity;

import main.GamePanel;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gamePanel){
        super(gamePanel);
        direction = "down";
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
}
