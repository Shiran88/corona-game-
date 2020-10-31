// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level;

import events.ILevelObservable;
import events.IObservable;

import javax.media.opengl.GLAutoDrawable;
/**
 * Represents a level of some game.
 */
public interface ILevel extends ILevelObservable {
    /**
     * The method displays and updates a current level state on the screen.
     * Sequential calls of this method simulate
     * an interactive game (within a level context).
     * @param gLDrawable
     */
    void display(GLAutoDrawable gLDrawable);

    /**
     * The method initalizes a level state.
     * @param gLDrawable
     */
    void init(GLAutoDrawable gLDrawable);
}
