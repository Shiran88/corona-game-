package game.level.gameObjects;

import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.text.html.HTML;

/**
 * The class represents horizontal 2d object.
 */
public abstract class Horizontal2D extends GameObject {
    private double lengthWestEast; // length from west to east in meters.
    private double lengthSouthNorth; // length from south to north in meters.
    private GameObjectType objectType;
    protected float[] normal = null;
    protected void setNormal(float x,float y,float z){
        normal = new float[]{x,y,z};
    }
    public Horizontal2D(double lengthWestEast, double lengthSouthNorth, Vertex origin){
        this.lengthWestEast = lengthWestEast;
        this.lengthSouthNorth = lengthSouthNorth;
        this.coordsSystem = new CoordinateSystem(origin);
        setObjectMaterial( 0.6f, 0.6f, 0.6f);
    }
    public CoordinateSystem getCoordsSystem() {
        return coordsSystem;
    }


    @Override
    public void init(GLAutoDrawable glDrawable) {
        return;
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        float xWest = (float)( coordsSystem.getOrigin().getX() - (lengthWestEast / 2) );
        float xEast = (float)( coordsSystem.getOrigin().getX() + (lengthWestEast / 2) );
        float zNorth = (float)( coordsSystem.getOrigin().getZ() - (lengthSouthNorth / 2) );
        float zSouth = (float)( coordsSystem.getOrigin().getZ() + (lengthSouthNorth / 2) );

        float orX = (float) coordsSystem.getOrigin().getX();
        float orY = (float) coordsSystem.getOrigin().getY();
        float orZ = (float) coordsSystem.getOrigin().getZ();
        gl.glPushMatrix();
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        texturesManager.getTexture(this).bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, objectMaterial, 0);
        gl.glNormal3f(normal[0], normal[1], normal[2]);
   //     gl.glTranslatef(orX,orY,orZ);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(1,0);
        gl.glVertex3f(xEast, orY, zNorth);
        gl.glTexCoord2f(1,1);
        gl.glVertex3f(xEast, orY, zSouth);
        gl.glTexCoord2f(0,1);
        gl.glVertex3f(xWest, orY,zSouth );
        gl.glTexCoord2f(0,0);
        gl.glVertex3f( xWest, orY, zNorth);
        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public boolean isColliding(ICollidable other) {
        return false;
    }
    @Override
    public CollidableInfo getCollidableInfo() {
        return new CollidableInfo(CollidableInfo.CollidableType.HORIZONTAL, GameObjectType.HORIZONTAL);
    }
}