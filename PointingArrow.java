package game.level.gameObjects;

import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import java.nio.FloatBuffer;

/**
 * Sign in a form of arrow that rotates and points to something.
 */
public class PointingArrow extends GameObject {
    private float[] color = new float[]{1f,1f,1f};
    public PointingArrow(Vertex pos){
        coordsSystem = new CoordinateSystem(pos);
        this.setObjectMaterial(1f, 1f, 1f);
        this.setRotationAngleY(2);
    }
    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return null;
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
        return false;
    }

    @Override
    public CollidableInfo getCollidableInfo() {
        return new CollidableInfo(CollidableInfo.CollidableType.SPHERE, 1f, GameObjectType.HELPER);
    }
    @Override
    public void update(GLAutoDrawable glDrawable) {
        // Get the GL corresponding to the drawable we are animating
        GL2 gl = glDrawable.getGL().getGL2();
        gl.glPushMatrix();

  //      gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glTranslatef((float)coordsSystem.getOrigin().getX(), (float)coordsSystem.getOrigin().getY(),
                (float)coordsSystem.getOrigin().getZ());

        gl.glRotatef(rotationAngleY,0f,1f,0f);
        rotationAngleY+=1;
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        texturesManager.getTexture(this).bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, objectMaterial, 0);





        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glTexCoord2f(1,1);
        gl.glVertex3f(6,0,0);
        gl.glTexCoord2f(0,1);
        gl.glVertex3f(-6,0,0);
        gl.glTexCoord2f(0.5f,0);
        gl.glVertex3f(0,-15,0);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(1,1);
        gl.glVertex3f(3,10,0);

        gl.glTexCoord2f(0,1);
        gl.glVertex3f(-3,10,0);

        gl.glTexCoord2f(0,0);
        gl.glVertex3f(-3,0,0);

        gl.glTexCoord2f(1,0);
        gl.glVertex3f(3,0,0);
        gl.glEnd();



        gl.glPopMatrix();
    }

}
