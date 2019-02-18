/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.controller;

import ihaterobots.entities.Robot1;

/**
 *
 * @author charly
 */
public class Robot1Controller extends AIController {
   private Robot1 robot;

   public Robot1Controller(Robot1 robot1) {
      super(robot1, robot1);
      robot = robot1;
      robot1.addEntityListener(this);
      switchActionX(Action.RIGHT);
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
   public void collidedUp() {
      if (robot.isClimbing()) {
         robot.setClimbing(false);
      }
   }
}
