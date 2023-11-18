/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.simulator;

import com.charlyghislain.ihaterobots.entities.EntityListener;
import com.charlyghislain.ihaterobots.entities.FireBall;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public class FireBallSimulator extends BaseEntitySimulator implements EntityListener {
   private static final float FLYING_SPEED = .05f;
   private static final float FLYING_SPEED_MOD = .003f;
   private FireBall fireBall;

   public FireBallSimulator(FireBall ball) {
      super(ball);
      this.fireBall = ball;
      fireBall.setSpeedX(FLYING_SPEED);
      fireBall.setSpeedY(FLYING_SPEED);
      fireBall.addEntityListener(this);
   }

   @Override
   protected void updateAccel(int delta) {
   }

   @Override
   protected void updateSpeed(int delta) {
      if (Math.random() < .01f) { // deviate from tragectory a bit
         if (Math.random() < .5f) { // on x or y axis
            if (Math.random() < .5f) { // on left or right
               fireBall.setSpeedX(fireBall.getSpeedX() - FLYING_SPEED_MOD);
            } else {
               fireBall.setSpeedX(fireBall.getSpeedX() + FLYING_SPEED_MOD);
            }
         } else {
            if (Math.random() < .5f) { // up or down
               fireBall.setSpeedY(fireBall.getSpeedY() - FLYING_SPEED_MOD);
            } else {
               fireBall.setSpeedY(fireBall.getSpeedY() + FLYING_SPEED_MOD);
            }
         }
      }
   }

   @Override
   protected void checkStaticCollisions(int delta) {

      GameMap map = parent.getEnv().getMap();

      // Entity coords
      float centerX = parent.getPosX();
      float centerY = parent.getPosY();
      float leftX = centerX - parent.getWidth() / 2;
      float rightX = centerX + parent.getWidth() / 2;
      float topY = centerY - parent.getHeigth() / 2;
      float botY = centerY + parent.getHeigth() / 2;
      int tilePosX = map.getTilePosX(centerX);
      int tilePosY = map.getTilePosY(centerY);

      // HERE
      if (checkStuckStatus(map, centerX, centerY)) {
         return;
      }
      if (!staticCollisionCheckNeeded && !parent.isStuck()) {
         return;
      }
      checkMapCollisionOnPlaceovers(map, centerX, centerY, leftX, rightX, topY, botY, tilePosX, tilePosY);
      checkAutoTrigger(map, tilePosX, tilePosY);
      staticCollisionCheckNeeded = false;
   }

   @Override
   protected void updatePos(int delta) {
      super.updatePos(delta);
   }

   @Override
   public void movingChanged() {
   }

   @Override
   public void teleportingChanged() {
   }

   @Override
   public void climbingChanged() {
   }

   @Override
   public void fallingChanged() {
   }

   @Override
   public void facingLeftChanged() {
   }

   @Override
   public void stuckChanged() {
   }

   @Override
   public void collidedLeft() {
      fireBall.setSpeedX(-fireBall.getSpeedX());
   }

   @Override
   public void collidedRight() {
      fireBall.setSpeedX(-fireBall.getSpeedX());
   }

   @Override
   public void collidedUp() {
      fireBall.setSpeedY(-fireBall.getSpeedY());
   }

   @Override
   public void collidedDown() {
      fireBall.setSpeedY(-fireBall.getSpeedY());
   }
}
