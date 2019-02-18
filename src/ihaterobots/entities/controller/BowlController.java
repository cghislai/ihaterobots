/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.controller;

import ihaterobots.entities.simulator.BowlSimulator;
import ihaterobots.entities.controller.AIController;
import ihaterobots.entities.Bowl;
import ihaterobots.entities.EntityListener;

/**
 *
 * @author charly
 */
public class BowlController extends AIController implements EntityListener {
   private Bowl bowl;
   private boolean avoidTeeport;

   public BowlController(Bowl bowl) {
      super(bowl, bowl);
      this.bowl = bowl;
      bowl.addEntityListener(this);
      switchActionX(Math.random() > .5 ? Action.LEFT : Action.RIGHT);
   }

   @Override
   public void collidedLeft() {
      switchActionX(Action.RIGHT);
   }

   @Override
   public void collidedRight() {
      switchActionX(Action.LEFT);
   }

   @Override
   public void climbingChanged() {
      if (!bowl.isClimbing()) {
         switchActionX(Math.random() > .5 ? Action.LEFT : Action.RIGHT);
      }
   }

   @Override
   public void teleportingChanged() {
      switchActionY(null);
      if (bowl.isTeleporting()) {
         avoidTeeport = true;
      } else {
         switchActionX(Math.random() > .5 ? Action.LEFT : Action.RIGHT);
      }
   }

   @Override
   public void fallingChanged() {
      super.fallingChanged();
      if (!bowl.isFalling()) {
         switchActionX(bowl.getSpeedX() < 0 ? Action.LEFT : Action.RIGHT);
      }
   }

   public void update(int delta) {
//      if (bowl.getSpeedX() < -BowlSimulator.ROLLING_SPEED_MAX + .01) {
////         switchActionX(null);
//         avoidTeeport = false;
//      }
//      if (bowl.getSpeedX() > BowlSimulator.ROLLING_SPEED_MAX - .01) {
////         switchActionX(null);
//         avoidTeeport = false;
//      }
      if (bowl.isOnTeleport()) {
         if (!avoidTeeport) {
            switchActionX(null);
            switchActionY(Action.DOWN);
         }
      } else {
         avoidTeeport = false;
      }
   }
}
