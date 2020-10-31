// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

/**
 * Holds JOGL2 related singletons.
 */
public class GLSingletons {
    private static GLU glu = new GLU();
    private static GLCanvas canvas = new GLCanvas();
    public static GLCanvas getGLCanvas() {return canvas;}
    public static GLU getGlu() {
        return glu;
    }
}
