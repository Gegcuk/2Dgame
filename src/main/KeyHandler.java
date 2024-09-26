package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    public boolean upPressed, downPressed, leftPressed ,rightPressed, enterPressed;
    public boolean isTestMode = false;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();


        //Title state
        if(gamePanel.gameState == gamePanel.titleState){

            if(gamePanel.ui.titleScreenState == 0){
                if(code == KeyEvent.VK_UP)
                    if (gamePanel.ui.commandNumber > 0) gamePanel.ui.commandNumber--;
                if(code == KeyEvent.VK_DOWN) {
                    if (gamePanel.ui.commandNumber < 2) gamePanel.ui.commandNumber++;
                    System.out.println(gamePanel.ui.commandNumber);
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gamePanel.ui.commandNumber == 0){
                        gamePanel.ui.titleScreenState = 1;
                    }
                    if(gamePanel.ui.commandNumber == 1) {

                    }
                    if(gamePanel.ui.commandNumber == 2) {
                        System.exit(0);
                    }
                }
            } else if(gamePanel.ui.titleScreenState == 1){
                if(code == KeyEvent.VK_UP)
                    if (gamePanel.ui.commandNumber > 0) gamePanel.ui.commandNumber--;
                if(code == KeyEvent.VK_DOWN) {
                    if (gamePanel.ui.commandNumber < 3) gamePanel.ui.commandNumber++;
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gamePanel.ui.commandNumber == 0){
                        System.out.println("You chose Viking!");
                        gamePanel.gameState = gamePanel.playState;
                    }
                    if(gamePanel.ui.commandNumber == 1) {
                        System.out.println("You chose Mage!");
                        gamePanel.gameState = gamePanel.playState;
                    }
                    if(gamePanel.ui.commandNumber == 2) {
                        System.out.println("You chose Archer!");
                        gamePanel.gameState = gamePanel.playState;
                    }
                    if(gamePanel.ui.commandNumber == 3) {
                        gamePanel.ui.commandNumber = 0;
                        gamePanel.ui.titleScreenState = 0;
                    }
                }
            }
        }

        if(gamePanel.gameState == gamePanel.playState){
            if(code == KeyEvent.VK_DOWN){
                downPressed = true;
            }
            if(code == KeyEvent.VK_UP){
                upPressed = true;
            }
            if(code == KeyEvent.VK_LEFT){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_RIGHT){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_P){
                gamePanel.gameState = gamePanel.pauseState;
            }
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            if(code == KeyEvent.VK_T){
                if(!isTestMode) isTestMode = true;
                else if (isTestMode) isTestMode = false;
            }
        }
        if(gamePanel.gameState == gamePanel.pauseState){
            if(code == KeyEvent.VK_P){
                gamePanel.gameState = gamePanel.playState;
            }
        }
        if(gamePanel.gameState == gamePanel.dialogState){
            if(code == KeyEvent.VK_ENTER){
                gamePanel.gameState = gamePanel.playState;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if(code == KeyEvent.VK_UP){
            upPressed = false;
        }
        if(code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
    }
}
