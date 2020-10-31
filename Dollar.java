package game.level.gameObjects;

import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;
import net.java.joglutils.model.iModel3DRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.Arrays;
/**
 * This class represents a dollar.
 */
public class Dollar extends GameObjectFromFile {


    public Dollar(Vertex origin, String fileName) {
        super(origin, fileName);
    }
    @Override
    public void init(GLAutoDrawable glDrawable) {

        GL2 gl = glDrawable.getGL().getGL2();
        WavefrontObjectLoader_DisplayList.loadWavefrontObjectAsDisplayList(gl,filePath);
        id = WavefrontObjectLoader_DisplayList.getAndCleanLastLoaded(gl);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.MONEY);
    }
    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.SPHERE,2.5f,GameObjectType.MONEY);
        return info;
    }

}

