package game.level.gameObjects;

import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

/**
 * The class represents the grass arrea.
 */
public class Grass extends Horizontal2D {
    public Grass(double lengthWestEast, double lengthSouthNorth, Vertex origin) {
        super(lengthWestEast, lengthSouthNorth, origin);
        setNormal(0,-1,0);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.GROUND);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void respondToCollision(ICollidable c) {

    }

}
