/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities;

import ihaterobots.entities.drawer.VertJumperDrawer;
import ihaterobots.entities.simulator.VertJumperSimulator;
import ihaterobots.entities.BaseEntity;
import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.interfaces.Controllable;
import ihaterobots.interfaces.Drawable;
import ihaterobots.interfaces.Entity;
import ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Graphics;

/**
 *
 * @author cghislai
 */
public class VertJumper extends BaseEntity implements Entity, Drawable, Controllable, Updatable {

    public static final int BOUNCE_TIME = 300;
    public static final float JUMPING_SPEED = .2f;
    private boolean bouncingUp;
    private VertJumperDrawer drawer;
    private VertJumperSimulator simulator;

    public VertJumper() {
        drawer = new VertJumperDrawer(this);
        simulator = new VertJumperSimulator(this);
        heigth = 32;
        width = 10;
        bouncingUp = false;
        speedY = JUMPING_SPEED;
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
        simulator.update(delta);
    }

    public boolean isBouncingUp() {
        return bouncingUp;
    }

    public void setBouncingUp(boolean bouncingUp) {
        this.bouncingUp = bouncingUp;
    }

    public VertJumperDrawer getDrawer() {
        return drawer;
    }

    public VertJumperSimulator getSimulator() {
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
