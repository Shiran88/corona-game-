package game.level.gameWorld;

import math.Vertex;

import javax.media.opengl.GL2;

public class Light {
    // The light amount
    private float[] ambient;
    private float[] diffuse;
    // The GL light index
    private int lightIndex;

    // The position of the light
    private float[] position;



    /**
     * The constructor.
     * @param index The GL light index
     * @param pos The position
     * @param amb The ambient light amount
     * @param dif The diffuse light amount
     */
    public Light(int index, Vertex pos, float[] amb, float[] dif) {
        lightIndex = index;
        position = new float[]{(float)pos.getX(), (float)pos.getY(), (float)pos.getZ(),1f};
        ambient = amb;
        diffuse = dif;
    }

    /**
     * The init method of the light.
     * @param gl The GL2 object to use
     */
    public void init(GL2 gl) {
        gl.glEnable(lightIndex);
    }

    /**
     * The display method of the light.
     * @param gl The GL2 object to use
     */
    public void display(GL2 gl) {
        gl.glLightfv(lightIndex, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(lightIndex, GL2.GL_DIFFUSE, diffuse, 0);

        gl.glLightfv(lightIndex, GL2.GL_POSITION, position, 0);
    }
}
