package main;

import util.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The UI class is responsible for rendering the user interface in the game,
 * including the title screen, pause screen, dialogues, and in-game messages.
 */
public class UI {

    Graphics2D graphics2D;
    GamePanel gamePanel;
    Font pixelify_40, pixelify_50B, pixelify_32P;
    BufferedImage heart_full, heart_half, heart_empty;
    public boolean messageOn = false; // Flag to display messages on the screen
    public String currentDialogue = ""; // Current dialogue text to display
    public int commandNumber = 0;
    public int titleScreenState = 0;
    List<String> messageList = new ArrayList<>();
    List<Integer> messageCounter = new ArrayList<>();

    /**
     * Constructor that initializes the UI with the given GamePanel and fonts.
     *
     * @param gamePanel the main game panel where the UI will be rendered
     */
    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        // Initialize the fonts used in the UI
        pixelify_40 = new Font("Pixelify Sans", Font.ITALIC, 20);
        pixelify_50B = new Font("Pixelify Sans", Font.BOLD, 50);
        pixelify_32P = new Font("Pixelify Sans", Font.PLAIN, 32);

        try {
            heart_full = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_full.png")));
            heart_half = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_half.png")));
            heart_empty = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_blank.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Displays a message on the screen.
     *
     * @param text the message text to display
     */
    public void addMessage(String text){
        messageList.add(text);
        messageCounter.add(0);
    }

    /**
     * Draws the UI components depending on the current game state.
     *
     * @param graphics2D the Graphics2D object used for rendering the UI
     */
    public void draw(Graphics2D graphics2D){
        this.graphics2D = graphics2D;
        graphics2D.setFont(pixelify_50B);
        graphics2D.setColor(Color.white);

        // Draw title screen if the game is in title state
        if(gamePanel.gameState == GameState.TITLE){
            drawTitleScreen();
        }

        if(gamePanel.gameState == GameState.PLAY){
            drawPlayerLife();
            drawMessage();
        }

        // Draw pause screen if the game is in pause state
        if(gamePanel.gameState == GameState.PAUSE){
            drawPlayerLife();
            drawPauseScreen();
        }

        // Draw dialogue screen if the game is in dialogue state
        if(gamePanel.gameState == GameState.DIALOG){
            drawPlayerLife();
            drawDialogueScreen();
        }
        
        if(gamePanel.gameState == GameState.CHARACTER){
            drawCharacterScreen();
        }
    }

    private void drawPlayerLife() {
        int x = gamePanel.tileSize/2;
        int y = gamePanel.tileSize/2;
        int heartSize = gamePanel.tileSize;

        int maxHearts = gamePanel.player.maxLife/2;
        int currentLife = gamePanel.player.life;

        for(int i = 0; i < maxHearts; i++){
            BufferedImage heartImage;

            if(currentLife >= (i + 1) * 2){
                heartImage = heart_full;
            } else if (currentLife == (i * 2) + 1){
                heartImage = heart_half;
            } else {
                heartImage = heart_empty;
            }

            graphics2D.drawImage(heartImage, x, y, heartSize, heartSize, null);
            x += heartSize + 10;
        }
    }


    private void drawMessage() {
        int messageX = gamePanel.tileSize;
        int messageY = gamePanel.tileSize*8;
       graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 20F));
        for (int i = 0; i < messageList.size(); i++) {
            String text = messageList.get(i);
            if (text != null) {
                graphics2D.setColor(Color.black);
                graphics2D.drawString(text, messageX+2, messageY+2);
                graphics2D.setColor(Color.white);
                graphics2D.drawString(text, messageX, messageY);
                messageCounter.set(i, messageCounter.get(i)+1);
                messageY += 30;
                if(messageCounter.get(i) > 180) {
                    messageList.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    /**
     * Draws the title screen with the game title and acknowledgment.
     */
    private void drawTitleScreen() {
        if(titleScreenState == 0){
            // Set background color
            graphics2D.setColor(new Color(37, 107, 125));
            graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

            // Draw game title
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 50F));
            String text = "Blue Knight Adventure";
            String acknowledgment = "Inspired by RyiSnow";

            int x = getCenterOfTextX(text);
            int y = gamePanel.tileSize * 2;

            // Draw shadow effect
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(text, x + 3, y + 3);
            graphics2D.setColor(Color.white);
            graphics2D.drawString(text, x, y);

            //Draw blue knight image
            x = gamePanel.screenWidth/2 - gamePanel.tileSize;
            y += gamePanel.tileSize * 2;
            graphics2D.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize*2, gamePanel.tileSize*2, null);

            //Draw menu
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD));
            text = "NEW GAME";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "LOAD GAME";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "QUIT GAME";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }


            // Draw acknowledgment
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 30F));
            x = getCenterOfTextX(acknowledgment);
            y += gamePanel.tileSize * 2;
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(acknowledgment, x + 3, y + 3);
            graphics2D.setColor(Color.white);
            graphics2D.drawString(acknowledgment, x, y);
        }
        else if (titleScreenState == 1){
            graphics2D.setColor(Color.white);
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 30F));

            String text = "Select your class";
            int x = getCenterOfTextX(text);
            int y = gamePanel.tileSize * 2;
            graphics2D.drawString(text, x, y);

            text = "Viking";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize * 2;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Mage";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Archer";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Back";
            x = getCenterOfTextX(text);
            y += gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);
            if(commandNumber == 3) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

        }
    }

    /**
     * Draws the pause screen with a "PAUSED" text in the center.
     */
    public void drawPauseScreen(){
        String text = "PAUSED";

        int x = getCenterOfTextX(text);
        int y = gamePanel.screenHeight / 2;

        graphics2D.drawString(text, x, y);
    }

    /**
     * Draws the dialogue screen, displaying the current dialogue text.
     */
    private void drawDialogueScreen() {
        int x = gamePanel.tileSize * 2;
        int y = gamePanel.tileSize / 2;
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
        int height = gamePanel.tileSize * 4;

        // Draw the dialogue window
        drawSubWindow(x, y, width, height);

        graphics2D.setFont(pixelify_32P);
        x += gamePanel.tileSize;
        y += gamePanel.tileSize;

        // Draw each line of the dialogue text
        for (String line : currentDialogue.split("\n")){
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }

    private void drawCharacterScreen() {
        final int frameX = gamePanel.tileSize;
        final int frameY = gamePanel.tileSize;
        final int frameWidth = gamePanel.tileSize * 5;
        final int frameHeight = gamePanel.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(20F));

        int textX = frameX + 20;
        int textY = frameY + gamePanel.tileSize;
        final int lineHeight = 30;
        graphics2D.drawString("Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Life ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Strength ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Dexterity ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Attack ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Defense ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Exp ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Next Level ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Coins ", textX, textY);

        textY += lineHeight;
        graphics2D.drawString("Weapon ", textX, textY);

        textY += lineHeight * 2 + 10;
        graphics2D.drawString("Shield ", textX, textY);

        int tailX = (frameX + frameWidth) - 25;
        textY = frameY + gamePanel.tileSize;
        String value;

        value = String.valueOf(gamePanel.player.level);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.life + "/" + gamePanel.player.maxLife);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.strength);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.dexterity);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.attack);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.defense);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.exp);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.nextLevelExp);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.coin);
        textX = getLeftAlignTextX(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        graphics2D.drawImage(gamePanel.player.getEquippedWeapon().stateImages.get("default"), tailX - gamePanel.tileSize * 2, textY, null );
        textY += gamePanel.tileSize + 10;
        graphics2D.drawImage(gamePanel.player.getEquippedShield().stateImages.get("default"), tailX - gamePanel.tileSize * 2, textY, null );
    }

    /**
     * Draws a sub-window with rounded corners and an optional background and border.
     *
     * @param x the x-coordinate of the window
     * @param y the y-coordinate of the window
     * @param width the width of the window
     * @param height the height of the window
     */
    public void drawSubWindow(int x, int y, int width, int height){
        Color color = new Color(0, 0, 0, 220); // Set background color with transparency
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255); // Set border color
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5)); // Set border thickness
        graphics2D.drawRoundRect(x, y, width, height, 35, 35);
    }

    /**
     * Calculates the x-coordinate to center the text horizontally on the screen.
     *
     * @param text the text to be centered
     * @return the x-coordinate to draw the centered text
     */
    public int getCenterOfTextX(String text){
        int length = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;
    }

    public int getLeftAlignTextX(String text, int tailX){
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = tailX - length;
        return x;
    }
}
