/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities;

import ihaterobots.entities.drawer.DiggerDrawer;
import ihaterobots.entities.controller.DiggerController;
import ihaterobots.entities.simulator.DiggerSimulator;
import ihaterobots.entities.controller.Controller.Action;
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
public class Digger extends BaseEntity implements Entity, Drawable, Controllable, Updatable {

    private DiggerController controller;
    private DiggerDrawer drawer;
    private DiggerSimulator simulator;
    private boolean firing;
    private boolean actionFire;

    public Digger() {
        super();
        controller = new DiggerController(this);
        drawer = new DiggerDrawer(this);
        simulator = new DiggerSimulator(this);
        width = 24;
        heigth = 24;
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
    public void processAction(Action action, boolean started) {
        super.processAction(action, started);
        if (action == Action.FIRE) {
            actionFire = started;
        }
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

    public DiggerController getController() {
        return controller;
    }

    public DiggerDrawer getDrawer() {
        return drawer;
    }

    public DiggerSimulator getSimulator() {
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

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public boolean isActionFire() {
        return actionFire;
    }

    @Override
    public void doorLockChanged() {
        simulator.checkCollisionNeeded();
    }

    
}
