package game.level.gameObjects;

import com.jogamp.opengl.util.texture.Texture;
import math.Axis;
import math.Vertex;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

/**
 * Represents an bject that a player can hold while playing.
 */
public class StuckToCameraObject {
    private Vertex positionBeforeCamera;
    private Texture texture = null;
    private int displayListId;
    protected float rotationAngleX = 0f;
    protected float rotationAngleY = 0f;
    protected float rotationAngleZ = 0f;

    protected float scaleFactorX = 1f;
    protected float scaleFactorY = 1f;
    protected float scaleFactorZ = 1f;

    protected float[] objectMaterial = new float[]{0.5f,0.5f,0.5f};

    public StuckToCameraObject setObjectMaterial(float r, float g, float b){
        objectMaterial = new float[]{r,g,b};
        return  this;
    }

    public float[] getObjectMaterial() {
        return objectMaterial;
    }
    public StuckToCameraObject(Vertex positionBeforeCamera, int displayListId) {
        this.positionBeforeCamera = positionBeforeCamera;
        this.displayListId = displayListId;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void display(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslated(positionBeforeCamera.getX(), positionBeforeCamera.getY(), positionBeforeCamera.getZ());
        gl.glRotatef(rotationAngleX,1f,0f,0f);
        gl.glRotatef(rotationAngleY,0f,1f,0f);
        gl.glRotatef(rotationAngleZ,0f,0f,1f);
        gl.glScalef(scaleFactorX,scaleFactorY,scaleFactorZ);
        if (texture != null) {
            texture.bind(gl);
        }
    //    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, getObjectMaterial(), 0);
        gl.glCallList(displayListId);
        gl.glPopMatrix();
    }

    public void setRotationAngleX(float rotationAngleX) {
        this.rotationAngleX = rotationAngleX;
    }

    public void setRotationAngleY(float rotationAngleY) {
        this.rotationAngleY = rotationAngleY;
    }

    public void setRotationAngleZ(float rotationAngleZ) {
        this.rotationAngleZ = rotationAngleZ;
    }

    public void setScaleFactorX(float scaleFactorX) {
        this.scaleFactorX = scaleFactorX;
    }

    public void setScaleFactorY(float scaleFactorY) {
        this.scaleFactorY = scaleFactorY;
    }

    public void setScaleFactorZ(float scaleFactorZ) {
        this.scaleFactorZ = scaleFactorZ;
    }
    public void setRotationAngle(float x, float y, float z) {
        this.rotationAngleX = x;
        this.rotationAngleY = y;
        this.rotationAngleZ = z;
    }

    public void setScaleFactor(float x, float y, float z) {
        this.scaleFactorX = x;
        this.scaleFactorY = y;
        this.scaleFactorZ = z;
    }
}
