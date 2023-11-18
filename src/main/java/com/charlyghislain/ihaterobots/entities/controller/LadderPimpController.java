/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.controller;

import com.charlyghislain.ihaterobots.entities.simulator.LadderPimpSimulator;
import com.charlyghislain.ihaterobots.entities.LadderPimp;

/**
 *
 * @author cghislai
 */
public class LadderPimpController extends AIController {
   private final float DIST_THRESHOLD = 10000;
   private final int CHARGING_TIME = 1000;
   private final int ACTION_TIME = 800;
   private LadderPimp pimp;
   private int actionTimer;

   public LadderPimpController(LadderPimp pimp) {
      super(pimp, pimp);
      this.pimp = pimp;
      pimp.addEntityListener(this);
      actionTimer = ACTION_TIME;
   }

   public void considerCharge(boolean noRandom) {
      boolean playerClose = playerSqrDist < DIST_THRESHOLD && playerSqrDist > 0;
      if (!noRandom && Math.random() < (playerClose ? .1 : .001)) {
         charge();
         return;
      }
      updatePlayerPosInfo(pimp.getEnv().getPlayer(), pimp);
      if (playerSqrDist > 4 * DIST_THRESHOLD || playerSqrDist < 0) {
         return;
      }
      // find if player in front of us
      updatePlayerRotationInfo();
      float angleDiff = getAngleDiff(pimp.getAngle(), playerangle);
      // only if p in correct quadrant
      if (Math.abs(angleDiff) < Math.PI / 15) {
         charge();
         return;
      }

   }

   public void considerCharge() {
      considerCharge(false);
   }

   private void doSomething() {
      pimp.setMoving(false);
      pimp.setRotating(false);
      pimp.setCharging(false);
      final LadderPimpSimulator simulator = pimp.getsimulator();
      considerCharge();
      if (actionTimer > 0) {
         return;
      }
      updatePlayerPosInfo(pimp.getEnv().getPlayer(), pimp);
      boolean playerClose = playerSqrDist < DIST_THRESHOLD && playerSqrDist > 0;

      // How long will be the action?
      int time = (int) ((1.5f - Math.random()) * ACTION_TIME);
      actionTimer = time;

      // IDLE
      if (Math.random() < .2f) {
         pimp.setMoving(false);
         if (playerClose) {
            actionTimer /= 5;
         }
         return;
      }
      //ROTATING
      if (Math.random() < (playerClose ? .6f : .3f)) {
         if (!playerClose) {
            simulator.setRotatingLeft((Math.random() < .5f ? true : false));
         } else {
            updatePlayerRotationInfo();
            float angleDiff = getAngleDiff(pimp.getAngle(), playerangle);
            simulator.setRotatingLeft(angleDiff > 0);
            actionTimer = Math.min(actionTimer / 2, (int) (Math.abs(angleDiff) / LadderPimpSimulator.ROTATING_SPEED));
         }
         pimp.setRotating(true);
         pimp.setMoving(false);
         return;
      }
      //MOVE
      if (playerClose) {
         actionTimer /= 2;
      }
      pimp.setMoving(true);
      return;
   }

   private void charge() {
      pimp.setCharging(true);
      pimp.setMoving(true);
      pimp.setRotating(false);
      actionTimer = CHARGING_TIME;
   }

   public void update(int delta) {
      if (pimp.isStuck()) {
         return;
      }
      if (actionTimer > 0) {
         actionTimer -= delta;
         if (actionTimer <= 0) {
            doSomething();
         }
      }
      if (pimp.isRotating()) {
         considerCharge(true);
      }
   }

   public float getPlayerAngle() {
      return playerangle;
   }
}
