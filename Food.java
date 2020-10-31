package game.level.gameObjects;

import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.level.gameWorld.collisions.CollidableInfo;
import math.Vertex;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class Food extends GameObjectFromFile {
    public Food(Vertex origin, String filePath) {
        super(origin, filePath);
    }
    @Override
    public void init(GLAutoDrawable glDrawable) {

        GL2 gl = glDrawable.getGL().getGL2();
        WavefrontObjectLoader_DisplayList.loadWavefrontObjectAsDisplayList(gl,filePath);
        id = WavefrontObjectLoader_DisplayList.getAndCleanLastLoaded(gl);
    }
    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.FOOD);
    }
    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.SPHERE,2.5f,GameObjectType.FOOD);
        return info;
    }


}
