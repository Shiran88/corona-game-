// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level.gameObjects;

import com.jogamp.opengl.util.texture.Texture;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.TexturesManager;
import game.level.gameWorld.collisions.CollisionManager;
import game.level.gameWorld.collisions.ICollidable;
import game.level.logic.IGameLogic;
import math.Axis;
import math.Vertex;

import javax.media.opengl.GLAutoDrawable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a physical part of game object in coronavirus game. That is,
 * it is responsible for displaying, collisions etc.(Things related to physical appearence in the world).
 */
public abstract class GameObject implements ICollidable {
    protected IGameLogic logic = null;
    protected CoordinateSystem coordsSystem;
    protected TexturesManager texturesManager;
    protected String textureName;
    public void setTexturesManager(TexturesManager manager) {
        texturesManager = manager;
    }
    public CoordinateSystem getCoordsSystem() {
        return coordsSystem;
    }
    protected float rotationAngleX = 0f;
    protected float rotationAngleY = 0f;
    protected float rotationAngleZ = 0f;

    protected float scaleFactorX = 1f;
    protected float scaleFactorY = 1f;
    protected float scaleFactorZ = 1f;
    protected CollisionManager collisionManager;
    protected float[] objectMaterial = new float[]{0.5f,0.5f,0.5f};
    public GameObject setObjectMaterial(float r, float g, float b){
        objectMaterial = new float[]{r,g,b};
        return  this;
    }

    public float[] getObjectMaterial() {
        return objectMaterial;
    }

    public void setPosition(Vertex position) {
        this.coordsSystem.putTo(position);
    }
    public void setCollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    protected Set<ICollidable> responseTo = new HashSet<>();
    public Texture getTexture() {
        return texturesManager.getTexture(this);
    }
    public void setRotationAngleX(float rotationAngleX) {
        this.rotationAngleX = rotationAngleX;
        this.coordsSystem.rotate(rotationAngleX, Axis.X);
    }

    public void setRotationAngleY(float rotationAngleY) {
        this.rotationAngleY = rotationAngleY;
        this.coordsSystem.rotate(rotationAngleY, Axis.Y);
    }

    public void setRotationAngleZ(float rotationAngleZ) {
        this.rotationAngleZ = rotationAngleZ;
        this.coordsSystem.rotate(rotationAngleZ, Axis.Z);
    }

    public void setScaleFactorX(float scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public void setScaleFactorY(float scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }

    public void setScaleFactorZ(float scaleFactorZ) {
        this.scaleFactorZ = scaleFactorZ;
    }
    public void setRotationAngle(float x, float y, float z) {
        this.rotationAngleX = x;
        this.rotationAngleY = y;
        this.rotationAngleZ = z;
        this.coordsSystem.rotate(x, Axis.X);
        this.coordsSystem.rotate(y, Axis.Y);
        this.coordsSystem.rotate(z, Axis.Z);
    }

    public void setScaleFactor(float x, float y, float z) {
        this.scaleFactorX = x;
        this.scaleFactorY = y;
        this.scaleFactorZ = z;
    }

    abstract GameObjectUpdateInfo getUpdateInfo();
    public void update(GLAutoDrawable gLDrawable) {

    }
    public void init(GLAutoDrawable gLDrawable) {
      //  renderingComponent.init();
    }
    public abstract boolean isMovable();

    @Override
    public CoordinateSystem getCoordsSystemCopy() {
        return this.coordsSystem.deepCopy();
    }

    @Override
    public void setResponseTo(ICollidable c) {
        responseTo.add(c);
    }
    final public void respondToCollisions() {
        for (ICollidable c: responseTo) {
            //change logic state of 'this' because of its collision with c.
            respondToCollision(c);
        }
        responseTo.clear();
    }
    abstract public void respondToCollision(ICollidable c);

    public void setLogic(IGameLogic logic) {
        this.logic = logic;
    }
}