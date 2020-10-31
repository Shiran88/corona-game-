package game.level.gameObjects;

import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

/**
 * Game object representing a road.
 */
public class Road extends Horizontal2D {
    public Road(double lengthWestEast, double lengthSouthNorth, Vertex origin) {
        super(lengthWestEast, lengthSouthNorth, origin);
        setNormal(0,-1,0);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.ROAD);
    }

    @Override
    public boolean isMovable() {
        //road is static object and doesn't move.
        return false;
    }

    @Override
    public void respondToCollision(ICollidable c) {

    }

    @Override
    public boolean isColliding(ICollidable other) {
        // it's static, therefore checking if it's colliding with something doesn't make sense.
        return false;
    }


}
