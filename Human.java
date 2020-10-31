package game.level.gameObjects;

import externalLoaders.WavefrontObjectLoader_DisplayList;
import game.level.gameWorld.collisions.CollidableInfo;
import game.level.gameWorld.collisions.ICollidable;
import math.Axis;
import math.Vertex;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.Arrays;

/**
 * The class that represents a human.
 */
public class Human extends GameObjectFromFile {
    private double moveSpeedAxisX;
    private double moveSpeedAxisZ;
    private double addingFactor = Math.PI/5;

    public void setMoveSpeedAxisX(double moveSpeedAxisX) {
        this.moveSpeedAxisX = moveSpeedAxisX;
    }

    public void setMoveSpeedAxisZ(double moveSpeedAxisZ) {
        this.moveSpeedAxisZ = moveSpeedAxisZ;
    }

    public double getMoveSpeedAxisX() {
        return moveSpeedAxisX;
    }

    public double getMoveSpeedAxisZ() {
        return moveSpeedAxisZ;
    }

    public Human(Vertex origin, String filePath) {
        super(origin, filePath);
    }

    @Override
    GameObjectUpdateInfo getUpdateInfo() {
        return new GameObjectUpdateInfo(GameObjectType.HUMAN);
    }




    public ICollidable findCollision() {
        for (ICollidable c: collisionManager.getCollidables()) {
            if (this.isColliding(c) && c != this) {
                // cancel the move if it results in collision.
                return c;
            }
        }
        return null;
    }


    public boolean move() {
        this.coordsSystem.moveAlongWorldAxises(Axis.X, moveSpeedAxisX);
        this.coordsSystem.moveAlongWorldAxises(Axis.Z, moveSpeedAxisZ);
        ICollidable c = this.findCollision();
        if (c != null) {
            this.coordsSystem.moveAlongWorldAxises(Axis.X, -moveSpeedAxisX);
            this.coordsSystem.moveAlongWorldAxises(Axis.Z, -moveSpeedAxisZ);
            logic.getNotifiedAboutCollision(this,c);
            this.respondToCollision();
            return false;
        }
        return true;
    }


    public void respondToCollision() {
        if (moveSpeedAxisX == 0) {
            moveSpeedAxisZ = - moveSpeedAxisZ;
            return;
        }
        if (moveSpeedAxisZ == 0) {
            moveSpeedAxisX = - moveSpeedAxisX;
            return;
        }
        double spSquared = moveSpeedAxisX*moveSpeedAxisX + moveSpeedAxisZ*moveSpeedAxisZ;
        double prevAngle = Math.atan(Math.abs(moveSpeedAxisZ)/Math.abs(moveSpeedAxisX));
        double add;
        if (Math.random() >=0.5)
            add = addingFactor;
        else
            add = -addingFactor;
        double newAngle = prevAngle + add;
        double t = Math.tan(newAngle);
        double xTagAbsSquared = spSquared/(Math.pow(t,2) + 1);
        double xTag = Math.sqrt(xTagAbsSquared);
        double zTag = Math.sqrt(spSquared - xTagAbsSquared);
        if (moveSpeedAxisX > 0) {
            xTag = - xTag;
        }
        if (moveSpeedAxisZ > 0) {
            zTag = - zTag;
        }

        moveSpeedAxisX = xTag;
        moveSpeedAxisZ = zTag;
    }

    @Override
    public CollidableInfo getCollidableInfo() {
        CollidableInfo info = new CollidableInfo(CollidableInfo.CollidableType.SPHERE,6f,GameObjectType.HUMAN);
        return info;
    }

    @Override
    public void update(GLAutoDrawable glDrawable) {
        this.move();
        super.update(glDrawable);
    }
}
