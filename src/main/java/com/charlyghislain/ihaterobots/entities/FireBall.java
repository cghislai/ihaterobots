/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

import com.charlyghislain.ihaterobots.entities.drawer.FireBallDrawer;
import com.charlyghislain.ihaterobots.entities.simulator.FireBallSimulator;
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
public class FireBall extends BaseEntity implements Entity, Drawable, Controllable, Updatable {
   private FireBallSimulator simulator;
   private FireBallDrawer drawer;

   public FireBall() {
      super();
      simulator = new FireBallSimulator(this);
      drawer = new FireBallDrawer(this);
      width = 15;
      heigth = 15;
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

   public FireBallDrawer getDrawer() {
      return drawer;
   }

   public FireBallSimulator getSimulator() {
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
