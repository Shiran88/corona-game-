package game.level.gameWorld;

import math.*;

/**
 * This class represents a 3d coordinate system
 */
public class CoordinateSystem {
    private Vertex origin;
    // The axes
    private Vertex xAxis;
    private Vertex yAxis;
    private Vertex zAxis;
    public void putTo(Vertex position) {
        this.origin = position.deepCopy();
    }
    public CoordinateSystem(double x, double y, double z) {
        origin = new Vertex(x,y,z);
        xAxis = new Vertex(1, 0, 0);
        yAxis = new Vertex(0, 1, 0);
        zAxis = new Vertex(0, 0, 1);

    }
    public CoordinateSystem(Vertex origin) {
        this.origin = origin;
        xAxis = new Vertex(1, 0, 0);
        yAxis = new Vertex(0, 1, 0);
        zAxis = new Vertex(0, 0, 1);
    }

    public Vertex getOrigin() {
        return origin;
    }

    public Vertex getXAxis() {
        return xAxis;
    }

    public Vertex getYAxis() {
        return yAxis;
    }

    public Vertex getZAxis() {
        return zAxis;
    }

    /**
     * Rotates the coordinate system.
     * @param angle angle to rotate.
     * @param axis axis.
     */
    public void rotate(double angle, Axis axis) {
        int el1 = 0, el2 = 0;
        // Selecting the rotated axes
        Vertex a1 = null, a2 = null;
        switch (axis) {
            case X:
                a1 = yAxis;
                a2 = zAxis;
                break;
            case Y:
                a1 = xAxis;
                a2 = zAxis;
                break;
            case Z:
                a1 = yAxis;
                a2 = xAxis;
                break;
        }

        // Calculating the rotations
        Vertex newA1, newA2;
        newA1 = a1.multiply(Math.cos(Math.toRadians(angle)));
        newA1 = newA1.subtract(a2.multiply(Math.sin(Math.toRadians(angle))));
        newA2 = a1.multiply(Math.sin(Math.toRadians(angle)));
        newA2 = newA2.add(a2.multiply(Math.cos(Math.toRadians(angle))));

        // Normalizing the new axes
        newA1 = newA1.normalize();
        newA2 = newA2.normalize();

        // Storing the rotated axes
        switch (axis) {
            case X:
                yAxis = newA1;
                zAxis = newA2;
                break;
            case Y:
                xAxis = newA1;
                zAxis = newA2;
                break;
            case Z:
                yAxis = newA1;
                xAxis = newA2;
                break;
        }

    }

    /**
     * Get "look at" vector according to current coordinate system state.
     * @return
     */
    public Vertex getLookAt() {
        return this.getOrigin().subtract(zAxis);
    }
    public void moveAlongWorldAxises(Axis axis, double coef) {
        Vertex floorAxisX = new Vertex(1,0,0);
        Vertex floorAxisZ = new Vertex(0,0,1);
        Vertex movingAxis = null;
        switch (axis) {
            case X:
                movingAxis = xAxis;
                break;
            case Y:
                this.origin.setY(this.origin.getY() + coef);
                return;

            case Z:
                movingAxis = zAxis;
                break;
        }


        double angleX = Vector.calculateAngleBetweenVectors(floorAxisX,movingAxis);
        double cosX = Math.cos(Math.toRadians(angleX));
        double vpX = movingAxis.getMagnitude() * cosX * coef;

        double angleZ = Vector.calculateAngleBetweenVectors(floorAxisZ,movingAxis);
         double cosZ = Math.cos(Math.toRadians(angleZ));
        double vpZ = movingAxis.getMagnitude() * cosZ * coef;

        this.origin.setX(this.origin.getX() + vpX);
        this.origin.setZ(this.origin.getZ() + vpZ);
//        this.origin = this.origin.add(addAxis.multiply(coef));
    }


    /**
     * Move the coordinate system along axis axis.
     * @param axis
     * @param coef hom many units to move.
     */
    public void move(Axis axis, double coef) {
        Vertex addAxis = null;
        switch (axis) {
            case X:
                addAxis = this.xAxis;
                break;
            case Y:
                addAxis = this.yAxis;
                break;
            case Z:
                addAxis = this.zAxis;
                break;
        }


        this.origin = this.origin.add(addAxis.multiply(coef));


    }

    /**
     * deep copy of this coordinate system.
     * @return
     */
    public CoordinateSystem deepCopy() {
        CoordinateSystem s = new CoordinateSystem(this.origin.getX(), this.origin.getY(), this.origin.getZ());
        s.xAxis = xAxis;
        s.yAxis = yAxis;
        s.zAxis = zAxis;
        return s;
    }
}
