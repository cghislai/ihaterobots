/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities;

import com.charlyghislain.ihaterobots.entities.controller.Robot2Controller;
import com.charlyghislain.ihaterobots.entities.drawer.Robot2Drawer;
import com.charlyghislain.ihaterobots.entities.simulator.Robot2Simulator;
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
public final class Robot2 extends BaseEntity implements Entity, Drawable, Controllable, Updatable {
   public final static int MAX_FUEL = 5000;
   private Robot2Controller controller;
   private Robot2Simulator simulator;
   private Robot2Drawer drawer;
   private boolean flying;
   private float fuel;

   public Robot2() {
      super();
      controller = new Robot2Controller(this);
      simulator = new Robot2Simulator(this);
      drawer = new Robot2Drawer(this);
      width = 30;
      heigth = 18;
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
      if (!flying && fuel < MAX_FUEL) {
         fuel += delta / 3;
         if (fuel > MAX_FUEL) {
            fuel = MAX_FUEL;
         }
      } else if (flying) {
         fuel -= delta/2;
      }
      if (fuel < 0) {
         fuel = 0;
         controller.onEmptyFuel();
      }
   }

   public Robot2Controller getController() {
      return controller;
   }

   public Robot2Drawer getDrawer() {
      return drawer;
   }

   public Robot2Simulator getSimulator() {
      return simulator;
   }

   public boolean isFlying() {
      return flying;
   }

   public void setFlying(boolean flying) {
      this.flying = flying;
   }

   public float getFuel() {
      return fuel;
   }

   public void setFuel(float fuel) {
      this.fuel = fuel;
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
