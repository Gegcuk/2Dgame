package main;

import entity.Player;
import object.SuperObject;
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

    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Sound soundEffect = new Sound();
    Sound backgroundMusic = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);

    Thread gameThread;

    public Player player = new Player(this, keyHandler);
    public SuperObject[] objects = new SuperObject[10];


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame(){
        assetSetter.setObject();
        playMusic(0);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1_000_000_000 / FPS;
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


        //DEBUG!!!
        long drawStartTime = 0;
        long drawEndTime = 0;

        if(keyHandler.isTestMode) {
            drawStartTime = System.nanoTime();
        }

        tileManager.draw(graphics2D);
        for (SuperObject object : objects) {
            if (object != null) {
                object.draw(graphics2D, this);
            }
        }
        ui.draw(graphics2D);
        player.draw(graphics2D);

        if(keyHandler.isTestMode){
            drawEndTime = System.nanoTime();
            graphics2D.setColor(Color.white);
            graphics2D.drawString("Draw time: " + (drawEndTime-drawStartTime), 10, 400);
            System.out.println(drawEndTime - drawStartTime);
        }


        graphics.dispose();
    }

    public void playMusic(int index){
        backgroundMusic.setFile(index);
        backgroundMusic.play();
        backgroundMusic.loop();
    }

    public void stopMusic(){
        backgroundMusic.stop();
    }

    public void playSE(int index){
        soundEffect.setFile(index);
        soundEffect.play();
    }
}
