package game.level.gameWorld;
// Shiran Golbar, 313196974
// Lev Levin, 342480456
import game.GLSingletons;
import game.level.gameObjects.*;
import game.level.gameWorld.collisions.CollisionManager;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a physical world for coronavirus game.
 */
public class GameWorld implements IGameWorld{
    private Player player;

    private TexturesManager texturesManager;
    private GLU glu;
    private List<GameObject> gameObjects; // objects to draw on this level.
    private CollisionManager collisionManager;
    private List<Light> lights = new ArrayList<>();

    public GameWorld(List<GameObject> objects, Player player, TexturesManager texturesManager
            , CollisionManager collisionManager) {
        this.gameObjects = objects;
        this.player = player;
        if (!objects.contains(player)) {
            throw new RuntimeException("player must be passed in objects list to GameWorld constructor!!");
        }
        for (GameObject o: objects) {
            o.setTexturesManager(texturesManager);
        }
        this.texturesManager = texturesManager;
        this.glu = GLSingletons.getGlu();
        this.collisionManager = collisionManager;
        initCollisionManaging();
    }

    /**
     * add the light to the world.
     * @param l
     * @return
     */
    public GameWorld addLight(Light l) {
        lights.add(l);
        return this;
    }

    /**
     * add lights to the world.
     * @param ls
     * @return
     */
    public GameWorld addLights(Collection<Light> ls) {
        lights.addAll(ls);
        return this;
    }

    /**
     * Initialize collision manager.
     */
    private void initCollisionManaging() {
        for(GameObject o: gameObjects) {
            collisionManager.addCollidable(o);
            o.setCollisionManager(collisionManager);
        }
    }
    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Initializes physical world state.
     * @param glDrawable
     */
    public void init(GLAutoDrawable glDrawable) {
        for (Light l: lights) {
            l.init(glDrawable.getGL().getGL2());
        }
        for (GameObject o: gameObjects) {
            o.init(glDrawable);
        }
    }

    @Override
    public void deleteFromWorld(GameObject g) {
        collisionManager.removeCollidable(g);
        gameObjects.remove(g);
    }

    @Override
    public void addToWorld(GameObject g) {
        this.gameObjects.add(g);
        this.collisionManager.addCollidable(g);
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
//        gl.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //setting up the camera...
        CoordinateSystem coordsSystem = player.getCoordsSystem();
        try {
            Vertex lookat = coordsSystem.getLookAt();
            glu.gluLookAt(coordsSystem.getOrigin().getX(), coordsSystem.getOrigin().getY(),
                    coordsSystem.getOrigin().getZ(), lookat.getX(),
                    lookat.getY(), lookat.getZ(),
                    coordsSystem.getYAxis().getX(), coordsSystem.getYAxis().getY(), coordsSystem.getYAxis().getZ());


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("an exception has occurred in display method of Player class!!!");
        }

        for (GameObject o : gameObjects) {
            o.update(glDrawable);
        }
        lights.forEach(l->l.display(gl));

    }
}