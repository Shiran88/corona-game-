// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level.gameWorld.collisions;

import game.level.gameWorld.collisions.ICollidable;

import java.util.*;

/**
 * This classes helps to manage collisions.
 */
public class CollisionManager {
    List<ICollidable> collidables;
    public CollisionManager() {
        collidables = new ArrayList<>();
    //    collisionCheckers = new ArrayList<>();
    }
    public void addCollidable(ICollidable col) {
        collidables.add(col);
    }

    public void removeCollidable(ICollidable col) {
        collidables.remove(col);
    }

    public List<ICollidable> getCollidables() {
        return this.collidables;
    }

}
