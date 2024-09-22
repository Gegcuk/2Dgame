package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    public final int originalTileSize = 16;
    public final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCols = 50;
    public final int maxWorldRows = 50;
    public final int worldWidth = tileSize * maxWorldCols;
    public final int worldHeight = tileSize * maxWorldRows;

    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyHandler);

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
        player.update();
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        tileManager.draw(graphics2D);
        player.draw(graphics2D);
        graphics.dispose();
    }
}
