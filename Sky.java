package game.level.gameObjects;

import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

/**
 * Game object representing the sky.
 */
public class Sky extends Horizontal2D {
    CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.HORIZONTAL, GameObjectType.SKY);
    public Sky(double lengthWestEast, double lengthSouthNorth, Vertex origin) {
        super(lengthWestEast, lengthSouthNorth, origin);
        setNormal(0,1,0);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.SKY);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void respondToCollision(ICollidable c) {

    }

    @Override
    public boolean isColliding(ICollidable other) {
        //sky is static and cannot be moved.
        return false;
    }

    @Override
    public CollidableInfo getCollidableInfo() {
        return info;
    }


    // private ProductsTracker productsTracker; -> move to Logic
}