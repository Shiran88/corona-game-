// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level.gameWorld.collisions;

import game.level.gameObjects.GameObject;
import game.level.gameObjects.GameObjectType;

import java.util.List;

/**
 * The class that represents information about some collidable object.
 */
public class CollidableInfo {

    // types of collidabeles in the coronavirus game.
    public enum CollidableType {
        SPHERE,
        HORIZONTAL,
        BOX
    }
    private GameObjectType gameObjectType;
    private CollidableType collidableType;
    private float radius = 0;

    public CollidableInfo(CollidableType collidableType, float radius, GameObjectType gameType) {
        this.collidableType = collidableType;
        this.radius = radius;
        this.gameObjectType = gameType;
    }
    public CollidableInfo(CollidableType type, GameObjectType gameType) {
        collidableType = type;
        this.gameObjectType =gameType;
    }


    /**
     * Returns collidable type.
     * @return
     */
    public CollidableType getCollidableType(){
        return this.collidableType;
    }

    /**
     * Return collidable radius in the case the type is "sphere"
     * @return
     * @throws IllegalAccessException in the case some one applies the method when the type isn't sphere.
     */
    public float getRadius() throws IllegalAccessException {
        if (this.collidableType != CollidableType.SPHERE) {
            throw new IllegalAccessException("no radius for WALL collidable");
        }
        return this.radius;
    }
    private List<Float> boxPoints = null;

    public void setBoxPoints(List<Float> boxPoints) {
        this.boxPoints = boxPoints;
    }

    /**
     * In the case the object is of type box, these points represent rectangular around the object.
     * It is not allowed to be called on collidable which type isn't "box".
     * @return surrounding points.
     */
    public List<Float> getBoxPoints() {
        if (boxPoints == null) {
            throw new RuntimeException("the box points are not set!!");
        }
        return this.boxPoints;
    }

    /**
     * Returns object type.
     * @return
     */
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }
}
