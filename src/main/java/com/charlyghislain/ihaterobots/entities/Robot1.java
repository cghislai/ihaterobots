/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

import com.charlyghislain.ihaterobots.entities.drawer.Robot1Drawer;
import com.charlyghislain.ihaterobots.entities.controller.Robot1Controller;
import com.charlyghislain.ihaterobots.entities.simulator.Robot1Simulator;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Controllable;
import com.charlyghislain.ihaterobots.interfaces.Drawable;
import com.charlyghislain.ihaterobots.interfaces.Entity;
import com.charlyghislain.ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Graphics;

/**
 *
 * @author charly
 */
public final class Robot1 extends BaseEntity implements Entity, Drawable, Controllable, Updatable {

    private Robot1Controller controller;
    private Robot1Simulator simulator;
    private Robot1Drawer drawer;

    public Robot1() {
        super();

        controller = new Robot1Controller(this);
        simulator = new Robot1Simulator(this);
        drawer = new Robot1Drawer(this);
        width = 16;
        heigth = 30;
//        moving = true;
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
    public void update(int delta) {
        drawer.update(delta);
        controller.update(delta);
        simulator.update(delta);
    }

    public Robot1Controller getController() {
        return controller;
    }

    public Robot1Drawer getDrawer() {
        return drawer;
    }

    public Robot1Simulator getSimulator() {
        return simulator;
    }

    @Override
    public int getEntityId() {
        return Utils.ENTITY_TYPE_ENNEMY;
    }

    @Override
    public short getDrawablePriority() {
        return Utils.DRAWABLE_PRIORITY_NORMAL;
    }

    @Override
    public void doorLockChanged() {
        simulator.checkCollisionNeeded();
    }
}
