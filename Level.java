// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level;

import com.jogamp.opengl.util.awt.TextRenderer;
import events.*;
import events.Event;
import game.GLSingletons;
import game.level.gameObjects.Player;
import game.level.gameWorld.IGameWorld;
import game.level.logic.IGameLogic;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Coronavirus level.
 */
public class Level implements ILevel {

    private IGameLogic logic; //specifies rules for the game(who wins, who don't).
    private IGameWorld world; //physical 3d world- no logic. only tracks for collisions and draws the world.

    private List<ILevelObserver> observers = new ArrayList<>();


    public Level(IGameLogic logic, IGameWorld world) {
        this.logic = logic;
        this.world = world;
    }

    /**
     * Draws messages for the case the player/players win the game.
     * @param glDrawable
     */
    public void displayWin(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        TextRenderer renderer = new TextRenderer(new Font("Forte", Font.BOLD, 65));
        int width = GLSingletons.getGLCanvas().getWidth();
        int height = GLSingletons.getGLCanvas().getHeight();
        renderer.beginRendering(width, height);
        renderer.setColor(Color.GREEN);

        String message = "LEVEL OVER! YOU WON! :)";
        renderer.draw(message, 200, height / 2);
        renderer.draw("The next level will start in a few seconds...",110, height/2 - 120);
        renderer.setColor(Color.WHITE);
        renderer.endRendering();
    }

    /**
     * Displays a current level status on the screen.
     * @param glDrawable
     */
    public void displayCurrentStatus(GLAutoDrawable glDrawable) {
       logic.drawLogicInfo();
    }

    /**
     * Draws messages for the case the player/players loss the game.
     * @param glDrawable
     */
    public void displayLoss(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        TextRenderer renderer = new TextRenderer(new Font("Forte", Font.BOLD, 65));
        int width = GLSingletons.getGLCanvas().getWidth();
        int height = GLSingletons.getGLCanvas().getHeight();
        renderer.beginRendering(width, height);
        renderer.setColor(Color.RED);

        String message = "GAME OVER! YOU LOSE :(!";
        renderer.draw(message, 200, height / 2);
        renderer.draw("This level will be restarted in a few seconds...",110, height/2 - 120);
        renderer.setColor(Color.WHITE);
        renderer.endRendering();
    }
    @Override
    public void display(GLAutoDrawable gLDrawable) {
        if (logic.isGameFinished() && logic.isWinner(world.getPlayer())) {
            notifyObservers(new Event(EventType.LevelWin,new EventArgs("player won")));
            this.displayWin(gLDrawable);
        } else if (logic.isGameFinished() && (!logic.isWinner(world.getPlayer()))) {
            notifyObservers(new Event(EventType.LevelLoss, new EventArgs("level loss")));
            this.displayLoss(gLDrawable);
        } else {
            world.update(gLDrawable);
            this.displayCurrentStatus(gLDrawable);
        }
    }

    @Override
    public void init(GLAutoDrawable gLDrawable) {
        logic.init();
        world.init(gLDrawable);
    }

    @Override
    public void attachObserver(ILevelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(ILevelObserver observer) {
        observers.remove(observer);
    }


    @Override
    public void notifyObservers(Event event) {
        for (ILevelObserver o : observers) {
            o.getNotified(event);
        }
    }

    public Player getPlayer() {
        return world.getPlayer();
    }
}
