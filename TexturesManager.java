package game.level.gameWorld;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import game.level.gameObjects.GameObject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexturesManager {
    // The available textures
    private Map<String, Texture> pathToTexture = new HashMap<>();
    private Map<GameObject, Texture> gameObjectToTexture= new HashMap<>();

    // The indices of the textures
    public static final int SKY_TEXTURE = 0;
    public static final int GRASS_TEXTURE = 1;
    public static final int CONCRETE_TEXTURE = 2;
    public static final int BALLOON_TEXTURE = 4;
    public void loadTextures(List<String> pathsToTextures) {
        for (String path:pathsToTextures) {
            try {
                pathToTexture.put(path,TextureIO.newTexture(new File(path), true));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR IN LOADING TEXTURE IN loadTextures method\n" +
                        "fail to load:" + path);
            }
        }
    }

    public void bind(String texturePath, GameObject object) throws IllegalArgumentException {
        Texture texture = pathToTexture.get(texturePath);
        if (texture == null){
            loadTextures(Collections.singletonList(texturePath));
        }
        gameObjectToTexture.put(object, texture);
    }
    public Texture getTexture(String path) {
        return pathToTexture.get(path);
    }
    public Texture getTexture(GameObject object) {
        return gameObjectToTexture.get(object);
    }
}
