/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.controller;

import com.charlyghislain.ihaterobots.entities.Robot2;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public class Robot2Controller extends AIController {
   private static final int FLYING_CONSIDERATION_TIME = 1000;
   private Robot2 robot;
   private int flyingCOnsiderationTimer;//tile coords

   public Robot2Controller(Robot2 robot1) {
      super(robot1, robot1);
      robot = robot1;
      robot1.addEntityListener(this);
      switchActionX(Action.RIGHT);
   }
   
   public void onEmptyFuel() {
       switchActionY(null);
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
   public void update(int delta) {
      if (flyingCOnsiderationTimer > 0) {
         flyingCOnsiderationTimer -= delta;
      }
      super.update(delta);
      if (robot.getFuel() == Robot2.MAX_FUEL) {
         considerFlying();
         return;
      }
   }

   private void considerFlying() {
      if (robot.isFlying() || flyingCOnsiderationTimer > 0
              || robot.getFuel() == 0) {
         return;
      }
      flyingCOnsiderationTimer = FLYING_CONSIDERATION_TIME;
      GameMap map = robot.getEnv().getMap();
      int tileX = map.getTilePosX(robot.getPosX());
      int tileY = map.getTilePosY(robot.getPosY());
      if (map.isTileSolidMapCoords(tileX, tileY - 1)
              || map.isTileSolidMapCoords(tileX + (robot.isFacingLeft() ? -1 : 1),
                                          tileY - 1)) {
         return;
      }
      if (Math.random() < .1f) {
         switchActionY(Action.UP);
         return;
      }
      updatePlayerPosInfo(robot.getEnv().getPlayer(), robot);
      if (playerSqrDist < 0 && Math.random() < .1f) { //no player
         switchActionY(Action.UP);
         return;
      }

      if (Math.abs(playerPosYrel) < Y_CUTOFF && Math.random() < .8f) {
         // SAME HEIGTH
         return;
      }
      // IF player above go up if possible. Higher random probability since robot keeps falling
      if (playerPosYrel < 0 && Math.random() < .6f) {
         // PLAYER ABOVE
         switchActionY(Action.UP);
         return;
      }
   }

   @Override
   public void collidedUp() {
      if (robot.isClimbing()) {
         robot.setClimbing(false);
      }
   }
}
