package game.level.gameWorld.collisions;

import game.level.gameWorld.CoordinateSystem;

public interface ICollidable {
    boolean isColliding(ICollidable other);
    CoordinateSystem getCoordsSystemCopy();
    CollidableInfo getCollidableInfo();
    void setResponseTo(ICollidable c);
    void respondToCollisions();

//    CollidableType getCollidableType();
}
