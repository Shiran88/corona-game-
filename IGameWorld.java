package game.level.gameWorld;

import game.level.gameObjects.GameObject;
import game.level.gameObjects.Player;

import javax.media.opengl.GLAutoDrawable;

public interface IGameWorld {
    Player getPlayer();
    void update(GLAutoDrawable glAutoDrawable);
    void init(GLAutoDrawable glAutoDrawable);
    void deleteFromWorld(GameObject g );
    void addToWorld(GameObject g);
}
