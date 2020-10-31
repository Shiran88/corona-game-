package game.level.gameObjects;

import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;
import net.java.joglutils.model.DisplayListRenderer;
import net.java.joglutils.model.ModelLoadException;
import net.java.joglutils.model.geometry.Model;
import net.java.joglutils.model.iModel3DRenderer;
import net.java.joglutils.model.loader.LoaderFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents game objects that are loaded  from the file.
 */
public class GameObjectFromFile extends GameObject {
    protected String filePath;
    protected int id;
    protected float collisionRadius;
    protected List<Float> surroundingPoints; // xMin, xMax, zMin, zMax
    public GameObjectFromFile( Vertex origin, String filePath) {
        this.coordsSystem = new CoordinateSystem(origin);
        this.filePath = filePath;
        this.setObjectMaterial(0.35f, 0.35f, 0.35f);
    }

    public CoordinateSystem getCoordsSystem() {
        return coordsSystem;
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.ROAD);
    }


    @Override
    public void init(GLAutoDrawable glDrawable) {

    GL2 gl = glDrawable.getGL().getGL2();
    WavefrontObjectLoader_DisplayList.loadWavefrontObjectAsDisplayList(gl,filePath);
    id = WavefrontObjectLoader_DisplayList.getAndCleanLastLoaded(gl);
    }

    @Override
    public boolean isMovable() {
        //by default, objects loaded from a file cannot move.
        return false;
    }


    @Override
    public void update(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glTranslatef((float)coordsSystem.getOrigin().getX(), (float)coordsSystem.getOrigin().getY(), (float)coordsSystem.getOrigin().getZ());

        gl.glRotatef(rotationAngleX,1f,0f,0f);
        gl.glRotatef(rotationAngleY,0f,1f,0f);
        gl.glRotatef(rotationAngleZ,0f,0f,1f);
        gl.glScalef(scaleFactorX,scaleFactorY,scaleFactorZ);
        texturesManager.getTexture(this).bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, getObjectMaterial(), 0);
        gl.glCallList(id);
        gl.glPopMatrix();


    }
    public int getDisplayListID() {
        return this.id;
    }

    /**
     * Is this object collides with sphere?
     * @param other other collidable.
     * @return
     */
    private boolean isCollidingWithSphere(ICollidable other) {
        float sumRadiuses;
        try {
            sumRadiuses = this.getCollidableInfo().getRadius() + other.getCollidableInfo().getRadius();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        Vertex dif = this.getCoordsSystem().getOrigin().subtract(other.getCoordsSystemCopy().getOrigin());
        float distance = (float) Math.sqrt(dif.dotProduct(dif));
        return distance < sumRadiuses;
    }

    /**
     * Is colliding with box?
     * @param box collidable which is box.
     * @return
     */
    private boolean isCollidingWithBox(ICollidable box) {
        CollidableInfo cInfo = box.getCollidableInfo();
        if (cInfo.getGameObjectType() == GameObjectType.BIKE) {
            System.out.println("cInfo = " + cInfo);
        }
        List<Float> points = box.getCollidableInfo().getBoxPoints();
        Vertex orBox = box.getCoordsSystemCopy().getOrigin();
        float xMin = (float)orBox.getX() - Math.abs(points.get(0));
        float xMax = (float)orBox.getX() + Math.abs(points.get(1));
        float zMin = (float)orBox.getZ() - Math.abs(points.get(2));
        float zMax = (float)orBox.getZ() + Math.abs(points.get(3));
        Vertex thisOrigin = this.getCoordsSystem().getOrigin();
        return (thisOrigin.getX() >= xMin && thisOrigin.getX() <= xMax &&
                thisOrigin.getZ() >= zMin && thisOrigin.getZ() <= zMax);

    }
    @Override
    public boolean isColliding(ICollidable other) {
        switch (other.getCollidableInfo().getCollidableType()) {
            case SPHERE:
                return isCollidingWithSphere(other);
            case BOX:
                return isCollidingWithBox(other);
            default:
                // for unknown collidable, always state that there is not collision.
                return false;
        }
    }

    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.BOX,getUpdateInfo().getObjectType());
        info.setBoxPoints(this.surroundingPoints);
        return info;
    }
    @Override
    public CoordinateSystem getCoordsSystemCopy() {
        return this.coordsSystem.deepCopy();
    }

    @Override
    public void respondToCollision(ICollidable c) {
    }

}