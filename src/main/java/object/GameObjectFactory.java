package object;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.GamePanel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectFactory {

    private static final String CONFIG_FILE = "/objects/objects_config.json";
    private Map<String, GameObjectConfig> objectConfigs = new HashMap<>();

    public GameObjectFactory(){
        loadConfigurations();
    }

    private void loadConfigurations() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE)){
            if(inputStream == null){
                throw new IOException("Configuration file not found: " + CONFIG_FILE);
            }
            List<GameObjectConfig> configs = mapper.readValue(inputStream, new TypeReference<List<GameObjectConfig>>() {});
            for (GameObjectConfig config : configs){
                objectConfigs.put(config.getName(), config);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public GameObject createGameObject(String name, GamePanel gamePanel){
        GameObjectConfig config = objectConfigs.get(name);
        if(config == null){
            System.err.println("No configuration found for object: " + name);
            return null;
        }
        return new GameObject(config, gamePanel);
    }

}
