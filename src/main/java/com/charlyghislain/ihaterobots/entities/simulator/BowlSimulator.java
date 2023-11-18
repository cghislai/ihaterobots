/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.simulator;

import com.charlyghislain.ihaterobots.entities.EntityListener;
import com.charlyghislain.ihaterobots.entities.Bowl;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public class BowlSimulator extends BaseEntitySimulator implements EntityListener {
   public static final float ROLLING_SPEED_MAX = .13f;
   public static final float ROLLING_SPEED_MIN = .05f;
   public static final float ROLLING_ACCEL = .00003f;
   public static final float FALLING_SPEED = .1f;
   public static final float BOWL_FALLING_ACCEL = .005f;
   private Bowl bowl;

   public BowlSimulator(Bowl bowl) {
      super(bowl);
      this.bowl = bowl;
      bowl.addEntityListener(this);
   }

   @Override
   protected void updateStates(int delta) {
      super.updateStates(delta);
      if (bowl.isOnGround() && bowl.isOnLadderUp()) {
         bowl.setClimbing(true);
      }
//      if (!bowl.isFalling() && (bowl.isOnLadderDown() || bowl.isLadderBelowDown())) {
//            bowl.setClimbing(true);
//      }
   }

   @Override
   protected void updateAccel(int delta) {
      parent.setAccelX(0);
      parent.setAccelY(0);
      if (parent.isClimbing()) {
         return;
      }
      if (parent.isFalling()) {
         parent.setAccelY(BOWL_FALLING_ACCEL);
         return;
      }
      if (bowl.isActionLeft() != bowl.isActionRight()) {
         boolean fl = bowl.isActionLeft();
         bowl.setAccelX(ROLLING_ACCEL * (fl ? -1 : 1));
      }
   }

   @Override
   protected void updateSpeed(int delta) {
      parent.setSpeedX(parent.getSpeedX() + parent.getAccelX() * delta);
      parent.setSpeedY(parent.getSpeedY() + parent.getAccelY() * delta);
      if (bowl.isClimbing()) {
         bowl.setSpeedX(0);
         bowl.setSpeedY(0);
         updateSpeedOnSpecialTiles();
         return;
      }
      if (bowl.isFalling()) {
         parent.setSpeedY(Math.max(ROLLING_SPEED_MIN, parent.getSpeedY()));
         parent.setSpeedY(Math.min(FALLING_SPEED, parent.getSpeedY()));
         updateSpeedOnSpecialTiles();
         return;
      }
      if (parent.getSpeedX() > 0 || parent.isActionRight()) {
         parent.setSpeedX(Math.min(ROLLING_SPEED_MAX, parent.getSpeedX()));
         parent.setSpeedX(Math.max(ROLLING_SPEED_MIN, parent.getSpeedX()));
      } else if (parent.getSpeedX() < 0 || parent.isActionLeft()) {
         parent.setSpeedX(Math.max(-ROLLING_SPEED_MAX, parent.getSpeedX()));
         parent.setSpeedX(Math.min(-ROLLING_SPEED_MIN, parent.getSpeedX()));
      }
      updateSpeedOnSpecialTiles();
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
      if (!parent.isFalling()) {
         parent.setSpeedY(0);
      }
   }

   @Override
   public void facingLeftChanged() {
   }

   @Override
   public void stuckChanged() {
   }

   @Override
   public void collidedLeft() {
      bowl.setSpeedX(-bowl.getSpeedX() / 4);

   }

   @Override
   public void collidedRight() {
      bowl.setSpeedX(-bowl.getSpeedX() / 4);

   }

   @Override
   public void collidedUp() {
      bowl.setSpeedY(0);
      bowl.setClimbing(false);
   }

   @Override
   public void collidedDown() {
      bowl.setSpeedY(0);
      bowl.setFalling(false);
   }
}
