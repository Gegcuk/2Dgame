package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

//    @Override
//    public void run() {
//
//        double drawInterval = (double) 1_000_000_000/FPS;
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while(gameThread != null){
//
//            update();
//
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime /= 1_000_000;
//
//                if(remainingTime < 0) {
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }


    @Override
    public void run() {

        double drawInterval = 1_000_000_000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;

            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }
    }

    public void update(){

        if(keyHandler.upPressed){
            playerY -= playerSpeed;
        }
        else if(keyHandler.downPressed){
            playerY += playerSpeed;
        }
        else if(keyHandler.leftPressed){
            playerX -= playerSpeed;
        }
        else if(keyHandler.rightPressed){
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(playerX, playerY, tileSize, tileSize);
        graphics.dispose();
    }
}
