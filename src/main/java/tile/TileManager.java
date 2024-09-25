package tile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class TileManager {

    GamePanel gamePanel;
    public Tile[] tiles;
    public int[][] mapTileNum;
    private  UtilityTool utilityTool = new UtilityTool();

    private static final String TILE_CONFIG_FILE = "/tiles/tiles_config.json";
    private static final String TILE_IMAGE_FOLDER_PATH = "/tiles/New version/";
    private static final String MAP_FILE = "/maps/map01.txt";

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        tiles = new Tile[50];
        mapTileNum = new int[gamePanel.maxWorldCols][gamePanel.maxWorldRows];
//        getTileImage();
        loadTileImages();
        loadMap("/maps/map01.txt");
    }

    private void loadTileImages() {
        ObjectMapper objectMapper = new ObjectMapper();
        try(InputStream inputStream = getClass().getResourceAsStream(TILE_CONFIG_FILE)){
            if(inputStream == null){
                throw new IOException("Tile configuration file not found: " + TILE_CONFIG_FILE);
            }
            List<TileConfig> tileConfigs = objectMapper.readValue(inputStream, new TypeReference<List<TileConfig>>(){});
            for(TileConfig config : tileConfigs){
                setupTile(config);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setupTile(TileConfig config) {
        try{
            Tile tile = new Tile();
            InputStream inputStream = getClass().getResourceAsStream(TILE_IMAGE_FOLDER_PATH + config.getImageName() + ".png");
            if(inputStream == null){
                throw new IOException("Tile image not found: " + config.getImageName() + ".png");
            }
            tile.image = ImageIO.read(inputStream);
            tile.image = utilityTool.scaleImage(tile.image, gamePanel.tileSize, gamePanel.tileSize);
            tile.collision = config.isCollision();
            tiles[config.getIndex()] = tile;
        } catch (IOException e){
            System.err.println("Error loading tile image for index " + config.getIndex() + ": " + e.getMessage());
        }
    }


    public void loadMap(String filePath){
        try(InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ){
            if(inputStream == null){
                throw new IOException("Map file not found: " + filePath);
            }

            int col = 0;
            int row = 0;

            while (row < gamePanel.maxWorldRows){
                String line = reader.readLine();
                if(line == null){
                    break;
                }
                String[] numbers = line.split(" ");

                for(col = 0; col < gamePanel.maxWorldCols; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D){
        int worldCol = 0;
        int worldRow = 0;


        while(worldRow < gamePanel.maxWorldRows){
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;

            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if(worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
               worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
               worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
               worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY){

                graphics2D.drawImage(tiles[tileNum].image, screenX, screenY, null);

                //DEBUGGING
                graphics2D.setColor(Color.RED);
                graphics2D.drawRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);
            }
            worldCol++;
            if(worldCol == gamePanel.maxWorldCols){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
