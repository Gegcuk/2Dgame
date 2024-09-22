package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {

    GamePanel gamePanel;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        tiles = new Tile[10];
        mapTileNum = new int[gamePanel.maxWorldCols][gamePanel.maxWorldRows];
        getTileImage();
        loadMap("/maps/map01.txt");
    }

    public void getTileImage(){
        try{
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/grass.png")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/wall.png")));
            tiles[1].collision = true;

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/water.png")));
            tiles[2].collision = true;

            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/earth.png")));

            tiles[4] = new Tile();
            tiles[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/tree.png")));
            tiles[4].collision = true;

            tiles[5] = new Tile();
            tiles[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/Old version/sand.png")));


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try(InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){

            int col = 0;
            int row = 0;

            while(col < gamePanel.maxWorldCols && row < gamePanel.maxWorldRows){
                String line = reader.readLine();

                while(col < gamePanel.maxWorldCols) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gamePanel.maxWorldCols){
                    col = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D){
        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gamePanel.maxWorldCols && worldRow < gamePanel.maxWorldRows){
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;

            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if(worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                    worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                    worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                    worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY){

                graphics2D.drawImage(tiles[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

            }
            worldCol++;


            if(worldCol == gamePanel.maxWorldCols){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
