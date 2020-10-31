
package game.level.gameObjects;

import com.jogamp.newt.event.KeyEvent;
import events.Event;
import events.EventArgs;
import events.EventType;
import events.IObserver;
import game.KeyboardManager;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Axis;
import math.Vertex;

import javax.media.opengl.GLAutoDrawable;
import java.util.*;

/**
 * Represents a player in game.
 */
public class Player extends GameObject implements IObserver {
    // private LifeTracker lifeTracker; -> move to Logic
    // private MoneyTracker moneyTracker; -> move to Logic
    private int lifes = 100;
    private double speedMove = 0.3;
    private double speedRotate = 1.4;
    private Set<ICollidable> collidedSpheres = new HashSet<>();
    private StuckToCameraObject frontalObject = null;


    public void setFrontalObject(StuckToCameraObject frontalObject) {
        this.frontalObject = frontalObject;
    }

    public Player(Vertex origin) {

        this.coordsSystem = new CoordinateSystem(origin);
    }

    public Player(float x, float y, float z) {
        coordsSystem = new CoordinateSystem(x,y,z);
    }



    public CoordinateSystem getCoordsSystem() {
        return coordsSystem;
    }



    @Override
    public void init(GLAutoDrawable glDrawable) {
  //     super.init(glDrawable);
        KeyboardManager keyboardManager = KeyboardManager.getSingleton();
        keyboardManager.attachObserver(this);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.PLAYER);
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        if (frontalObject != null) {
            frontalObject.display(glDrawable);
        }
    }

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
        return (thisOrigin.getX() > xMin && thisOrigin.getX() < xMax &&
            thisOrigin.getZ() > zMin && thisOrigin.getZ() < zMax);

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
        return new CollidableInfo(CollidableInfo.CollidableType.SPHERE,0.1f, GameObjectType.PLAYER);
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    public void tiltUp() {
        this.coordsSystem.rotate(-speedRotate, Axis.X);
    }
    public void tiltDown() {
        this.coordsSystem.rotate(speedRotate, Axis.X);
    }
    public void tiltLeft() {
        this.coordsSystem.rotate(speedRotate,Axis.Y);
    }
    public void tiltRight() {
        this.coordsSystem.rotate(-speedRotate, Axis.Y);
    }

    public ICollidable findCollision() {
        for (ICollidable c: collisionManager.getCollidables()) {
            if (this.isColliding(c) && c != this) {
                // cancel the move if it results in collision.
                return c;
            }
        }
        return null;
    }

    /**
     * Tries to move the player. If there is no physical obstacles(collisions)
     * - the move is performed. Otherwise, it is not performed.
     * @param ax - axis
     * @param speed - speed.
     * @return whether the move took place or not.
     */
    private boolean move(Axis ax, double speed) {
        this.coordsSystem.moveAlongWorldAxises(ax,speed);
        ICollidable c = this.findCollision();
        if (c != null) {
            logic.getNotifiedAboutCollision(this,c);
            this.coordsSystem.moveAlongWorldAxises(ax ,-speed);
            return false;
        }
        return true;
    }

    /**
     * Tries to move player forward.If there is no physical obstacles(collisions)
     * - the move is performed. Otherwise, it is not performed.
     * @return whether the move took place or not.
     */
    public boolean moveForward() {
        return this.move(Axis.Z, -speedMove);
    }

    /**
     * Tries to move player backward.If there is no physical obstacles(collisions)
     * - the move is performed. Otherwise, it is not performed.
     * @return whether the move took place or not.
     */
    public boolean moveBackward() {
        return this.move(Axis.Z, speedMove);
    }

    /**
     * Tries to move player left. If there is no physical obstacles(collisions)
     * - the move is performed. Otherwise, it is not performed.
     * @return whether the move took place or not.
     */
    public boolean moveLeft() {
        return this.move(Axis.X, -speedMove);
    }

    /**
     * Tries to move player right. If there is no physical obstacles(collisions)
     * - the move is performed. Otherwise, it is not performed.
     * @return whether the move took place or not.
     */
    public boolean moveRight() {
        return this.move(Axis.X, speedMove);
    }



    @Override
    public void getNotified(Event e) {

    }

    /**
     * Responds to collision with sphere collidable.
     * @param c
     */
    private void respondToSphereCollision(ICollidable c) {
        this.collidedSpheres.add(c);
    }
    @Override
    public void respondToCollision(ICollidable c) {
        switch (c.getCollidableInfo().getGameObjectType()) {
            case MONEY:
                logic.getNotifiedAboutCollision(this, c);
                break;
            case BIKE:
                respondToSphereCollision(c);
                logic.getNotifiedAboutCollision(this, c);
                break;
            case FOOD:
                logic.getNotifiedAboutCollision(this, c);
                break;
        }
    }

    /**
     * Set player's speed.
     * @param speedMove
     */
    public void setSpeedMove(double speedMove) {
        this.speedMove = speedMove;
    }

    /**
     * Get player's speed.
     * @return
     */
    public double getSpeedMove() {
        return speedMove;
    }


}
