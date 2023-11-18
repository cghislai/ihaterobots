/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

import com.charlyghislain.ihaterobots.entities.controller.Controller.Action;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Controllable;
import com.charlyghislain.ihaterobots.interfaces.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author charly
 */
public abstract class BaseEntity implements Controllable, Entity {

    protected final Set<EntityListener> listeners;
    // actions
    protected boolean actionLeft;
    protected boolean actionRight;
    protected boolean actionUp;
    protected boolean actionDown;
    // states
    protected boolean moving;
    protected boolean teleporting;
    protected boolean climbing;
    protected boolean falling;
    protected boolean facingLeft;
    protected boolean stuck;
    //
    protected boolean onLadder;
    protected boolean ladderBelow; // to start climbing : whole entity on ladder
    protected boolean onTopOfLadder; // to cehck if falling : one corner is enough
    protected boolean onGround;
    protected boolean onRollLeft;
    protected boolean onRollRight;
    protected boolean onLadderUp;
    protected boolean onLadderDown;
    protected boolean ladderBelowDown;
    protected boolean ladderBelowUp;
    protected boolean onIce;
    protected boolean onGrass;
    protected boolean onTrigger;
    protected boolean onTeleport;
    protected boolean onSpikes;
    protected boolean onFillFuel;
    protected boolean onWasteFuel;
    // phys
    protected float posX;
    protected float posY;
    protected float speedX;
    protected float speedY;
    protected float accelX;
    protected float accelY;
    protected int width;
    protected int heigth;
    // misc
    protected GameEnv env;

    public BaseEntity() {
        listeners = new HashSet<EntityListener>();
    }

    @Override
    public void processAction(Action action, boolean started) {
        switch (action) {
            case LEFT:
                actionLeft = started;
                if (started) {
                    facingLeft = true;
                }
                break;
            case RIGHT:
                actionRight = started;
                if (started) {
                    facingLeft = false;
                }
                break;
            case UP:
                actionUp = started;
                break;
            case DOWN:
                actionDown = started;
                break;
        }
    }

    public void addEntityListener(EntityListener listener) {
        listeners.add(listener);
    }

    public void removeEntitylistener(EntityListener listener) {
        listeners.remove(listener);
    }

    public boolean isActionDown() {
        return actionDown;
    }

    public void setActionDown(boolean actionDown) {
        this.actionDown = actionDown;
    }

    public boolean isActionLeft() {
        return actionLeft;
    }

    public void setActionLeft(boolean actionLeft) {
        this.actionLeft = actionLeft;
    }

    public boolean isActionRight() {
        return actionRight;
    }

    public void setActionRight(boolean actionRight) {
        this.actionRight = actionRight;
    }

    public boolean isActionUp() {
        return actionUp;
    }

    public void setActionUp(boolean actionUp) {
        this.actionUp = actionUp;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        if (this.facingLeft == facingLeft) {
            return;
        }
        this.facingLeft = facingLeft;
        fireFacingLeftChanged();
    }

    public boolean isLadderBelow() {
        return ladderBelow;
    }

    public void setLadderBelow(boolean ladderBelow) {
        this.ladderBelow = ladderBelow;
    }

    public boolean isOnLadder() {
        return onLadder;
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    @Override
    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getAccelX() {
        return accelX;
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public void setAccelY(float accelY) {
        this.accelY = accelY;
    }

    @Override
    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        if (this.stuck == stuck) {
            return;
        }
        this.stuck = stuck;
        fireStuckChanged();
        if (stuck) {
            env.getSoundManager().playRobotStuck();
        }
    }

    public GameEnv getEnv() {
        return env;
    }

    public boolean isOnTopOfLadder() {
        return onTopOfLadder;
    }

    public void setOnTopOfLadder(boolean onTopOfLadder) {
        this.onTopOfLadder = onTopOfLadder;
    }

    public boolean isOnRollLeft() {
        return onRollLeft;
    }

    public void setOnRollLeft(boolean onRollLeft) {
        this.onRollLeft = onRollLeft;
    }

    public boolean isOnRollRight() {
        return onRollRight;
    }

    public void setOnRollRight(boolean onRollRight) {
        this.onRollRight = onRollRight;
    }

    public boolean isOnLadderUp() {
        return onLadderUp;
    }

    public void setOnLadderUp(boolean onLadderUp) {
        this.onLadderUp = onLadderUp;
    }

    public boolean isLadderBelowDown() {
        return ladderBelowDown;
    }

    public void setLadderBelowDown(boolean ladderBelowDown) {
        this.ladderBelowDown = ladderBelowDown;
    }

    public boolean isLadderBelowUp() {
        return ladderBelowUp;
    }

    public void setLadderBelowUp(boolean ladderBelowUp) {
        this.ladderBelowUp = ladderBelowUp;
    }

    public boolean isOnGrass() {
        return onGrass;
    }

    public void setOnGrass(boolean onGrass) {
        this.onGrass = onGrass;
    }

    public boolean isOnIce() {
        return onIce;
    }

    public void setOnIce(boolean onIce) {
        this.onIce = onIce;
    }

    public boolean isOnTeleport() {
        return onTeleport;
    }

    public void setOnTeleport(boolean onTeleport) {
        this.onTeleport = onTeleport;
    }

    public boolean isOnTrigger() {
        return onTrigger;
    }

    public void setOnTrigger(boolean onTrigger) {
        this.onTrigger = onTrigger;
    }

    public boolean isOnSpikes() {
        return onSpikes;
    }

    public void setOnSpikes(boolean onSpikes) {
        this.onSpikes = onSpikes;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public void setClimbing(boolean climbing) {
        if (this.climbing == climbing) {
            return;
        }
        this.climbing = climbing;
        fireClimbingChanged();
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        if (this.moving == moving) {
            return;
        }
        this.moving = moving;
        fireMovingChanged();
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    public void setTeleporting(boolean teleporting) {
        if (this.teleporting == teleporting) {
            return;
        }
        this.teleporting = teleporting;
        fireTeleportingChanged();
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        if (this.falling == falling) {
            return;
        }
        this.falling = falling;
        fireFallingChanged();
    }

    public boolean isOnLadderDown() {
        return onLadderDown;
    }

    public void setOnLadderDown(boolean onLadderDown) {
        this.onLadderDown = onLadderDown;
    }

    public boolean isOnFillFuel() {
        return onFillFuel;
    }

    public void setOnFillFuel(boolean onFillFuel) {
        this.onFillFuel = onFillFuel;
    }

    public boolean isOnWasteFuel() {
        return onWasteFuel;
    }

    public void setOnWasteFuel(boolean onWasteFuel) {
        this.onWasteFuel = onWasteFuel;
    }

    protected void fireMovingChanged() {
        for (EntityListener listener : listeners) {
            listener.movingChanged();
        }
    }

    protected void fireTeleportingChanged() {
        for (EntityListener listener : listeners) {
            listener.teleportingChanged();
        }
    }

    protected void fireClimbingChanged() {
        for (EntityListener listener : listeners) {
            listener.climbingChanged();
        }
    }

    protected void fireFallingChanged() {
        for (EntityListener listener : listeners) {
            listener.fallingChanged();
        }
    }

    protected void fireFacingLeftChanged() {
        for (EntityListener listener : listeners) {
            listener.facingLeftChanged();
        }
    }

    protected void fireStuckChanged() {
        for (EntityListener listener : listeners) {
            listener.stuckChanged();
        }
    }

    public void fireCollisionLeft() {
        for (EntityListener listener : listeners) {
            listener.collidedLeft();
        }
    }

    public void fireCollisionRight() {
        for (EntityListener listener : listeners) {
            listener.collidedRight();
        }
    }

    public void fireCollisionUp() {
        for (EntityListener listener : listeners) {
            listener.collidedUp();
        }
    }

    public void fireCollisionDown() {
        for (EntityListener listener : listeners) {
            listener.collidedDown();
        }
    }

    @Override
    public void doorLockChanged() {
    }
}
