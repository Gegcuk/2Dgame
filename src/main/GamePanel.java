package main;

import entity.Entity;
import entity.Player;
import object.GameObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The GamePanel class manages the core game functionalities, including rendering, updates, and game states.
 * It also handles the main game loop and player input.
 */
public class GamePanel extends JPanel implements Runnable {

    /** The original size of a tile (in pixels). */
    public final int originalTileSize = 16;

    /** The scaling factor for the tile size. */
    public final int scale = 3;

    /** The size of a tile after scaling. */
    public final int tileSize = originalTileSize * scale;

    /** The maximum number of columns on the screen. */
    public final int maxScreenCol = 16;

    /** The maximum number of rows on the screen. */
    public final int maxScreenRow = 12;

    /** The width of the game screen (in pixels). */
    public final int screenWidth = tileSize * maxScreenCol;

    /** The height of the game screen (in pixels). */
    public final int screenHeight = tileSize * maxScreenRow;

    /** The maximum number of columns in the game world. */
    public final int maxWorldCols = 50;

    /** The maximum number of rows in the game world. */
    public final int maxWorldRows = 50;

    /** Frames per second (FPS) for the game loop. */
    int FPS = 60;

    /** Manages the game's tile rendering. */
    public TileManager tileManager = new TileManager(this);

    /** Handles key input from the player. */
    public KeyHandler keyHandler = new KeyHandler(this);

    /** Handles sound effects. */
    public Sound soundEffect = new Sound();

    /** Handles background music. */
    public Sound backgroundMusic = new Sound();

    /** Checks for collisions in the game. */
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    /** Sets assets such as objects and NPCs in the game. */
    public AssetSetter assetSetter = new AssetSetter(this);

    /** Manages the user interface (UI) elements. */
    public UI ui = new UI(this);

    /** The thread that runs the main game loop. */
    Thread gameThread;

    /** The player object representing the main character. */
    public Player player = new Player(this, keyHandler);

    /** Array of objects present in the game world. */
    public GameObject[] objects = new GameObject[10];

    /** Array of NPCs (non-player characters) in the game. */
    public Entity[] npc = new Entity[10];

    /** The current state of the game. */
    public int gameState;

    public final int titleState = 0;

    /** Game state indicating that the game is in play. */
    public final int playState = 1;

    /** Game state indicating that the game is paused. */
    public final int pauseState = 2;

    public final int dialogState = 3;

    public ObjectManager objectManager;

    /**
     * Constructor for the GamePanel class. Sets up the initial game panel size, background color,
     * and key listener.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.objectManager = new ObjectManager(this);
        objectManager.loadObjects();
    }

    /**
     * Initializes the game by setting up objects, NPCs, and playing background music.
     */
    public void setupGame() {
        assetSetter.setObject();
        assetSetter.setNPC();
        playMusic(0);
        gameState = titleState;
    }

    /**
     * Starts the game thread, which runs the main game loop.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * The main game loop. This method is called when the game thread starts.
     * It updates the game state and renders the game at a consistent frame rate.
     */
    @Override
    public void run() {

        double drawInterval = (double) 1_000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    /**
     * Updates the game logic, such as player movement and game state.
     */
    public void update() {
        if (gameState == playState) {
            player.update();
            for(Entity npc : npc){
                if(npc != null){
                    npc.update();
                }
            }
        }
        if (gameState == pauseState) {
            // Logic for pause state can be added here
        }
    }

    /**
     * Renders the game components such as tiles, objects, player, and UI elements.
     *
     * @param graphics The Graphics object used for rendering.
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        // DEBUG MODE
        long drawStartTime = 0;
        long drawEndTime = 0;

        if (keyHandler.isTestMode) drawStartTime = System.nanoTime();

        if(gameState == titleState){
            ui.draw(graphics2D);
        } else {
            tileManager.draw(graphics2D);

            List<GameObject> gameObjects = objectManager.getGameObjects();
            for (GameObject object : gameObjects) {
                if (object != null) {
                    object.draw(graphics2D, this);
                }
            }

            //NPC
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.draw(graphics2D);
                }
            }

            //Player
            player.draw(graphics2D);

            //UI
            ui.draw(graphics2D);
        }

        // Drawing tiles, objects, player, and UI


        // Display debug information
        if (keyHandler.isTestMode) {
            drawEndTime = System.nanoTime();
            graphics2D.setColor(Color.white);
            graphics2D.drawString("Draw time: " + (drawEndTime - drawStartTime), 10, 400);
            System.out.println(drawEndTime - drawStartTime);
        }

        graphics.dispose();
    }

    /**
     * Plays background music specified by the given index.
     *
     * @param index The index of the music file to play.
     */
    public void playMusic(int index) {
        backgroundMusic.setFile(index);
        backgroundMusic.play();
        backgroundMusic.loop();
    }

    /**
     * Stops the background music.
     */
    public void stopMusic() {
        backgroundMusic.stop();
    }

    /**
     * Plays a sound effect specified by the given index.
     *
     * @param index The index of the sound effect file to play.
     */
    public void playSE(int index) {
        soundEffect.setFile(index);
        soundEffect.play();
    }
}
