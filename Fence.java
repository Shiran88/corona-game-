package game.level.gameObjects;

import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Vertex;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.Arrays;
import java.util.List;

public class Fence extends GameObject {
    public enum Position{
        SouthNorth,
        EastWest,
    }
    private float[] normal;
    private Vertex vTopLeft = null;
    private Vertex vTopRight = null;
    private Vertex vBottomRight = null;
    private Vertex vBottomLeft = null;
    protected List<Float> surroundingPoints; // xMin, xMax, zMin, zMax
    private Position pos;
    public Fence(double lengthLeftRight, double lengthBottomTop, Vertex origin, Position pos) {
        this.coordsSystem = new CoordinateSystem(origin);
        float left,right,bottom,top;
        this.pos = pos;
        switch (pos) {
            case SouthNorth:
                left = (float)( origin.getZ() - lengthLeftRight/2 );
                right =(float)( origin.getZ() + lengthLeftRight/2 );
                bottom = (float)( origin.getY() - lengthBottomTop/2 );
                top = (float)( origin.getY() + lengthBottomTop/2 );
                vTopLeft = new Vertex(origin.getX(),top,left );
                vTopRight = new Vertex(origin.getX(),top,right);
                vBottomRight = new Vertex(origin.getX(),bottom, right);
                vBottomLeft = new Vertex(origin.getX(),bottom,left);
                this.surroundingPoints = Arrays.asList(-1.3f,1.3f,(float)-lengthLeftRight/2, (float)lengthLeftRight/2);
                break;
            case EastWest:
                left = (float)( origin.getX() - lengthLeftRight/2 );
                right =(float)( origin.getX() + lengthLeftRight/2 );
                bottom = (float)( origin.getY() - lengthBottomTop/2 );
                top = (float)( origin.getY() + lengthBottomTop/2 );
                vTopLeft = new Vertex(left,top,origin.getZ() );
                vTopRight = new Vertex(right,top,origin.getZ());
                vBottomRight = new Vertex(right,bottom, origin.getZ());
                vBottomLeft = new Vertex(left,bottom,origin.getZ());
                this.surroundingPoints = Arrays.asList((float)-lengthLeftRight/2, (float)lengthLeftRight/2, -1.3f,1.3f);
                break;
            default:
                throw new IllegalArgumentException("one of the wall types have to be provided!");
        }

        switch (pos) {
            case SouthNorth:
                // x is 1 because we draw verticles in outside-oriented way, but the player is located inside.
                normal = new float[]{1,0,0};
                break;
            case EastWest:
                // z is -1 because we draw vertices in outside-oriented way, but the player is located inside.
                normal = new float[] {0,0,-1};
                break;
        }
        setObjectMaterial( 0.6f, 0.6f, 0.6f);


    }

    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.WALL) ;
    }


    @Override
    public void init(GLAutoDrawable glDrawable) {
        return;
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glPushMatrix();
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        texturesManager.getTexture(this).bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, objectMaterial, 0);


        gl.glNormal3f(normal[0], normal[1], normal[2]);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(1,0);
        gl.glVertex3f((float) vTopLeft.getX(), (float)vTopLeft.getY(), (float)vTopLeft.getZ());
        gl.glTexCoord2f(1,1);
        gl.glVertex3f((float) vTopRight.getX(), (float)vTopRight.getY(), (float)vTopRight.getZ());
        gl.glTexCoord2f(0,1);
        gl.glVertex3f((float) vBottomRight.getX(), (float)vBottomRight.getY(), (float)vBottomRight.getZ());
        gl.glTexCoord2f(0,0);
        gl.glVertex3f((float) vBottomLeft.getX(), (float)vBottomLeft.getY(), (float)vBottomLeft.getZ());
        gl.glEnd();

        gl.glPopMatrix();
    }


    private boolean isCollidingWithSphere(ICollidable other) {
        float sumRadiuses;
        try {
            sumRadiuses = this.getCollidableInfo().getRadius() + other.getCollidableInfo().getRadius();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        Vertex dif = this.getCoordsSystem().getOrigin().subtract(other.getCoordsSystemCopy().getOrigin());
        float distance = (float) Math.sqrt(dif.dotProduct(dif));
        return distance < sumRadiuses;
    }
    private boolean isCollidingWithBox(ICollidable box) {
        CollidableInfo cInfo = box.getCollidableInfo();
        if (cInfo.getGameObjectType() == GameObjectType.BIKE) {
            System.out.println("cInfo = " + cInfo);
        }
        List<Float> points = box.getCollidableInfo().getBoxPoints();
        Vertex orBox = box.getCoordsSystemCopy().getOrigin();
        float xMin = (float)orBox.getX() - Math.abs(points.get(0));
        float xMax = (float)orBox.getX() + Math.abs(points.get(1));
        float zMin = (float)orBox.getZ() - Math.abs(points.get(2));
        float zMax = (float)orBox.getZ() + Math.abs(points.get(3));
        Vertex thisOrigin = this.getCoordsSystem().getOrigin();
        return (thisOrigin.getX() >= xMin && thisOrigin.getX() <= xMax &&
                thisOrigin.getZ() >= zMin && thisOrigin.getZ() <= zMax);
    }

    @Override
    public boolean isColliding(ICollidable other) {
        switch (other.getCollidableInfo().getCollidableType()) {
            case SPHERE:
                return isCollidingWithSphere(other);
            case BOX:
                return isCollidingWithBox(other);
            default:
                // for unknown collidable, always state that there is not collision.
                return false;
        }
    }

    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.BOX,getUpdateInfo().getObjectType());
        info.setBoxPoints(this.surroundingPoints);
        return info;
    }

    @Override
    public CoordinateSystem getCoordsSystemCopy() {
        return this.coordsSystem.deepCopy();
    }

    @Override
    public void respondToCollision(ICollidable c) {
    }

}

