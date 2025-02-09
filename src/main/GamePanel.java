package main;

import entity.Entity;
import entity.Player;
import object.GameObject;
import tile.TileManager;
import util.GameState;
import util.Renderable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
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
    public final int FPS = 60;

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

    public EventHandler eventHandler = new EventHandler(this);

    /** The thread that runs the main game loop. */
    Thread gameThread;

    /** The player object representing the main character. */
    public Player player;

    /** Array of objects present in the game world. */
    public Entity[] monsters = new Entity[20];
    /** Array of NPCs (non-player characters) in the game. */
    public Entity[] npc = new Entity[10];

    /** The current state of the game. */
    public GameState gameState;

    public ObjectManager objectManager;

    ArrayList<Renderable> renderables = new ArrayList<>();

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
        this.player = new Player(this, keyHandler);

    }

    /**
     * Initializes the game by setting up objects, NPCs, and playing background music.
     */
    public void setupGame() {
        gameState = GameState.TITLE;
        assetSetter.setObject();
        assetSetter.setNPC();
        assetSetter.setMonster();
        playMusic(0);
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
        if (gameState == GameState.PLAY) {
            player.update();
            for(Entity npc : npc){
                if(npc != null){
                    npc.update();
                }
            }
            for(int i = 0; i < monsters.length; i++){
                if(monsters[i] != null) {
                    if(monsters[i].alive && !monsters[i].dying){
                        monsters[i].update();
                    }
                    if (!monsters[i].alive){
                        monsters[i] = null;
                    }
                }
            }
        }
        if (gameState == GameState.PAUSE) {
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

        if(gameState == GameState.TITLE){
            ui.draw(graphics2D);
        } else {
            tileManager.draw(graphics2D);
            renderables.clear();

            List<GameObject> gameObjects = objectManager.getGameObjects();

            for (GameObject obj : gameObjects) {
                if (obj != null) {
                    renderables.add(obj);
                }
            }

            for (Entity entity : npc) {
                if (entity != null) {
                    renderables.add(entity);
                }
            }

            for (Entity entity : monsters){
                if(entity != null){
                    renderables.add(entity);
                }
            }

            renderables.add(player);

            renderables.sort(Comparator.comparingInt(Renderable::getWorldY));

            for(Renderable renderable : renderables){
                renderable.draw(graphics2D);
            }

            //UI
            ui.draw(graphics2D);
        }

        // Drawing tiles, objects, player, and UI

        if(keyHandler.isTestMode){
            eventHandler.drawEventRectangles(graphics2D);
        }

        // Display debug information

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
