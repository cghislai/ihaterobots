/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

import com.charlyghislain.ihaterobots.entities.drawer.LadderPimpDrawer;
import com.charlyghislain.ihaterobots.entities.controller.LadderPimpController;
import com.charlyghislain.ihaterobots.entities.simulator.LadderPimpSimulator;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Controllable;
import com.charlyghislain.ihaterobots.interfaces.Drawable;
import com.charlyghislain.ihaterobots.interfaces.Entity;
import com.charlyghislain.ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Graphics;

/**
 *
 * @author cghislai
 */
public class LadderPimp extends BaseEntity implements Entity, Drawable, Controllable, Updatable {

    private LadderPimpController controller;
    private LadderPimpDrawer drawer;
    private LadderPimpSimulator simulator;
    private float angle;
    private boolean charging;
    private boolean rotating;

    public LadderPimp() {
        super();
        controller = new LadderPimpController(this);
        drawer = new LadderPimpDrawer(this);
        simulator = new LadderPimpSimulator(this);
        heigth = 24;
        width = 24;
        climbing = true;
    }

    @Override
    public int getEntityId() {
        return Utils.ENTITY_TYPE_ENNEMY;
    }

    @Override
    public void register(GameEnv env) {
        this.env = env;
        drawer.init();
        env.getDrawableManager().addDrawable(this);
        env.getUpdatableManager().addUpdatable(this);
        env.getMovablesManager().addMovable(this);
        env.getEntitiesManager().addEntity(this);
    }

    @Override
    public void unregister() {
        env.getDrawableManager().removeDrawable(this);
        env.getUpdatableManager().removeUpdatable(this);
        env.getMovablesManager().removeMovable(this);
        env.getEntitiesManager().removeEntity(this);
    }

    @Override
    public void draw(Graphics g) {
        drawer.draw(g);
    }

    @Override
    public short getDrawablePriority() {
        return Utils.DRAWABLE_PRIORITY_NORMAL;
    }

    @Override
    public void update(int delta) {
        drawer.update(delta);
        controller.update(delta);
        simulator.update(delta);
    }

    public LadderPimpController getController() {
        return controller;
    }

    public LadderPimpDrawer getDrawer() {
        return drawer;
    }

    public LadderPimpSimulator getsimulator() {
        return simulator;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = (float) (angle + Math.PI * 2 % (Math.PI * 2));
    }

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public LadderPimpSimulator getsImulator() {
        return simulator;
    }

    @Override
    public void doorLockChanged() {
        simulator.checkCollisionNeeded();
    }
}
