package game.level.gameObjects;

import game.level.gameWorld.CoordinateSystem;
import math.Vertex;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Game objects representing rectangular.
 */
public abstract class Rectangle2D extends GameObject {
    private Vertex pRightTop;
    private Vertex pRightBottom;
    private Vertex pLeftBottom;
    private Vertex pLeftTop;
    private GameObjectType objectType;
    public Rectangle2D(Vertex pRightTop, Vertex pRightBottom, Vertex pLeftBottom, Vertex pLeftTop){
        this.pRightTop = pRightTop;
        this.pRightBottom = pRightBottom;
        this.pLeftBottom = pLeftBottom;
        this.pLeftTop = pLeftTop;
    }
    public CoordinateSystem getCoordsSystem() {
        return coordsSystem;
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return null;
    }


    @Override
    public void init(GLAutoDrawable glDrawable) {
        return;
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        texturesManager.getTexture(this).bind(gl);

        //     gl.glTranslatef(orX,orY,orZ);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(1,0);
        gl.glVertex3f((float) pRightTop.getX(), (float)pRightTop.getY(), (float)pRightTop.getZ());
        gl.glTexCoord2f(1,1);
        gl.glVertex3f((float) pRightBottom.getX(), (float)pRightBottom.getY(), (float)pRightBottom.getZ());
        gl.glTexCoord2f(0,1);
        gl.glVertex3f((float) pLeftBottom.getX(), (float)pLeftBottom.getY(), (float)pLeftBottom.getZ());
        gl.glTexCoord2f(0,0);
        gl.glVertex3f((float) pLeftTop.getX(), (float)pLeftTop.getY(), (float)pLeftTop.getZ());
        gl.glEnd();
        gl.glPopMatrix();
    }
}
