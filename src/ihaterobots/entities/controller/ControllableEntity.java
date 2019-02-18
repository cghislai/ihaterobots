/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.controller;

import ihaterobots.entities.controller.Controller;
import ihaterobots.entities.controller.Controller.Action;
import ihaterobots.interfaces.Controllable;
import ihaterobots.interfaces.Entity;

/**
 *
 * @author charly
 */
public abstract class ControllableEntity implements Controllable, Entity {

    protected boolean actionLeft;
    protected boolean actionRight;
    protected boolean actionUp;
    protected boolean actionDown;
    protected boolean facingLeft;
    protected float posX;
    protected float posY;
    protected int width;
    protected int heigth;

    public ControllableEntity(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
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

    @Override
    public float getPosX() {
        return posX;
    }

    @Override
    public float getPosY() {
        return posY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeigth() {
        return heigth;
    }

    public boolean isActionDown() {
        return actionDown;
    }

    public boolean isActionLeft() {
        return actionLeft;
    }

    public boolean isActionRight() {
        return actionRight;
    }

    public boolean isActionUp() {
        return actionUp;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
