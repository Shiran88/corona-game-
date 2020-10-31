package game.level.gameObjects;

import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;
import net.java.joglutils.model.iModel3DRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class representing a building where the player cannot enter.
 */
public class NotEnterableBuilding extends GameObjectFromFile {


    public NotEnterableBuilding(Vertex origin, String fileName, List<Float> surroundingPoints) {
        super(origin, fileName);
        this.surroundingPoints = surroundingPoints;
    }

    @Override
    public void init(GLAutoDrawable glDrawable) {

        GL2 gl = glDrawable.getGL().getGL2();
        WavefrontObjectLoader_DisplayList.loadWavefrontObjectAsDisplayList(gl,filePath);
        id = WavefrontObjectLoader_DisplayList.getAndCleanLastLoaded(gl);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.NOT_ENTERABLE_BUILDING);
    }

}