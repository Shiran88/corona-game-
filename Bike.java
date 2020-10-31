// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level.gameObjects;

import events.IObserver;
import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.KeyboardManager;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vector;
import math.Vertex;

import javax.lang.model.util.Elements;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import net.java.joglutils.model.iModel3DRenderer;

/**
 * This class represnts a bike.
 */
public class Bike extends GameObjectFromFile {
    private boolean isMovable;




    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public Bike(Vertex origin, String fileName) {
        super(origin, fileName);
    }


    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.BIKE);
    }

    @Override
    public boolean isMovable() {
        return isMovable;
    }
    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.SPHERE,6f,GameObjectType.BIKE);
        return info;
    }

}