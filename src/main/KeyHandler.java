package main;

import util.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    public boolean upPressed, downPressed, leftPressed ,rightPressed;
    public boolean enterPressed;
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
        if(gamePanel.gameState == GameState.TITLE){
            titleState(code);
        }
        else if(gamePanel.gameState == GameState.PLAY){
            playState(code);
        }
        else if(gamePanel.gameState == GameState.PAUSE){
            pauseState(code);
        }
        else if(gamePanel.gameState == GameState.DIALOG){
            dialogueState(code);
        }
        else if(gamePanel.gameState == GameState.CHARACTER){
            characterState(code);
        }

        if(code == KeyEvent.VK_M){
            gamePanel.stopMusic();
        }

    }

    private void characterState(int code) {
        if(code == KeyEvent.VK_I) {
            gamePanel.gameState = GameState.PLAY;
        }
    }

    private void dialogueState(int code) {
        if(code == KeyEvent.VK_ENTER){
            gamePanel.gameState = GameState.PLAY;
        }
    }

    private void pauseState(int code) {
        if(code == KeyEvent.VK_P){
            gamePanel.gameState = GameState.PLAY;
        }
    }

    private void playState(int code) {
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
            gamePanel.gameState = GameState.PAUSE;
        }
        if(code == KeyEvent.VK_I) {
            gamePanel.gameState = GameState.CHARACTER;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_T){
            isTestMode = !isTestMode;
        }
    }

    private void titleState(int code) {
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
                    gamePanel.gameState = GameState.PLAY;
                }
                if(gamePanel.ui.commandNumber == 1) {
                    System.out.println("You chose Mage!");
                    gamePanel.gameState = GameState.PLAY;
                }
                if(gamePanel.ui.commandNumber == 2) {
                    System.out.println("You chose Archer!");
                    gamePanel.gameState = GameState.PLAY;
                }
                if(gamePanel.ui.commandNumber == 3) {
                    gamePanel.ui.commandNumber = 0;
                    gamePanel.ui.titleScreenState = 0;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
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
