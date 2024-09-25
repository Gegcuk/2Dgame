package main;

import java.awt.*;

/**
 * The UI class is responsible for rendering the user interface in the game,
 * including the title screen, pause screen, dialogues, and in-game messages.
 */
public class UI {

    Graphics2D graphics2D;
    GamePanel gamePanel;
    Font pixelify_40, pixelify_50B, pixelify_32P;
    public boolean messageOn = false; // Flag to display messages on the screen
    public String message = ""; // The message to display
    public String currentDialogue = ""; // Current dialogue text to display
    public int commandNumber = 0;

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
    }

    /**
     * Displays a message on the screen.
     *
     * @param text the message text to display
     */
    public void showMessage(String text){
        message = text;
        messageOn = true;
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
        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }

        // Draw pause screen if the game is in pause state
        if(gamePanel.gameState == gamePanel.pauseState){
            drawPauseScreen();
        }

        // Draw dialogue screen if the game is in dialogue state
        if(gamePanel.gameState == gamePanel.dialogState){
            drawDialogueScreen();
        }
    }

    /**
     * Draws the title screen with the game title and acknowledgment.
     */
    private void drawTitleScreen() {
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
}
