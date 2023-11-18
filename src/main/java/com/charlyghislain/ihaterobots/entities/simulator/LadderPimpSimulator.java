/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.simulator;

import com.charlyghislain.ihaterobots.entities.EntityListener;
import com.charlyghislain.ihaterobots.entities.LadderPimp;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author cghislai
 */
public class LadderPimpSimulator extends BaseEntitySimulator implements EntityListener {
   public final static float CHARGING_ACCEL = .0002f;
   public final static float CHARGING_SPEED = .15f;
   public final static float WALKING_SPEED = .03f;
   public final static float ROTATING_SPEED = .002f;
   private LadderPimp pimp;
   private boolean rotatingLeft;

   public LadderPimpSimulator(LadderPimp pimp) {
      super(pimp);
      this.pimp = pimp;
      pimp.addEntityListener(this);
   }

   @Override
   protected void updateStates(int delta) {
   }

   @Override
   protected void updateAccel(int delta) {
      pimp.setAccelX(0);
      pimp.setAccelY(0);
      if (pimp.isCharging()) {
         float accelX = (float) Math.cos(pimp.getAngle()) * CHARGING_ACCEL;
         float accelY = (float) -Math.sin(pimp.getAngle()) * CHARGING_ACCEL;
         pimp.setAccelX(accelX);
         pimp.setAccelY(accelY);
      }
   }

   @Override
   protected void updateSpeed(int delta) {
      if (!pimp.isMoving() && !pimp.isCharging()) {
         pimp.setSpeedX(0);
         pimp.setSpeedY(0);
      }
      if (pimp.isMoving() && !pimp.isCharging()) {
         pimp.setSpeedX((float) Math.cos(pimp.getAngle()) * WALKING_SPEED);
         pimp.setSpeedY((float) -Math.sin(pimp.getAngle()) * WALKING_SPEED);
      }
      if (pimp.isCharging()) {
         parent.setSpeedX(parent.getSpeedX() + parent.getAccelX() * delta);
         parent.setSpeedY(parent.getSpeedY() + parent.getAccelY() * delta);
         float a = pimp.getAngle();
         if (Math.abs(pimp.getSpeedX()) > Math.abs((float) Math.cos(a) * CHARGING_SPEED)
                 || Math.abs(pimp.getSpeedY()) > Math.abs((float) Math.sin(a) * CHARGING_SPEED)) {
            pimp.setSpeedX((float) Math.cos(a) * CHARGING_SPEED);
            pimp.setSpeedY(-(float) Math.sin(a) * CHARGING_SPEED);
         }
      }
      if (!parent.isFalling() && parent.isClimbing() && (parent.isLadderBelowDown() || parent.isOnLadderDown())) {
         parent.setSpeedY(parent.getSpeedY() + Utils.ROLL_SPEED / 5);
      }
      if (parent.isClimbing() && (parent.isLadderBelowUp() || parent.isOnLadderUp())) {
         parent.setSpeedY(parent.getSpeedY() - Utils.ROLL_SPEED / 5);
      }
      if (parent.isOnRollLeft()) {
         parent.setSpeedX(parent.getSpeedX() - Utils.ROLL_SPEED);
      }
      if (parent.isOnRollRight()) {
         parent.setSpeedX(parent.getSpeedX() + Utils.ROLL_SPEED);
      }

   }

   @Override
   protected void updatePos(int delta) {
      super.updatePos(delta);
      if (pimp.getEnv().getGameManager().isEnnemiesPaused()) {
         return;
      }
      if (pimp.isRotating()) {
         float dangle = ROTATING_SPEED * delta * (rotatingLeft ? 1 : -1);
         pimp.setAngle(pimp.getAngle() + dangle);
      }
   }

   @Override
   protected void checkMapCollisionNoPlaceOver(GameMap map, float centerX, float centerY, float leftX, float rightX, float topY, float botY, int tilePosX, int tilePosY) {
      final boolean left = map.isTileSolid(leftX, centerY) || !map.isTileLadder(leftX, centerY);
      final boolean right = map.isTileSolid(rightX, centerY) || !map.isTileLadder(rightX, centerY);
      final boolean top = map.isTileSolid(centerX, topY) || !map.isTileLadder(centerX, topY);
      final boolean bottom = map.isTileSolid(centerX, botY) || !map.isTileLadder(centerX, botY);
      int tileSize = Utils.TILE_SIZE;


      if (left) {
         centerX = tilePosX * tileSize + pimp.getWidth() / 2;
         collideLeft(centerX);
      }
      if (right) {
         centerX = (tilePosX + 1) * tileSize - pimp.getWidth() / 2;
         collideRight(centerX);
      }
      if (top) {
         centerY = (tilePosY) * tileSize + pimp.getHeigth() / 2;
         collideUp(centerY);
      }
      if (bottom) {
         centerY = (tilePosY + 1) * tileSize - pimp.getHeigth() / 2;
         collideDown(centerY);
      }
      checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
   }

   @Override
   public void collidedDown() {
      pimp.setAngle(-pimp.getAngle());
      pimp.setCharging(false);
   }

   @Override
   public void collidedLeft() {
      pimp.setAngle((float) (Math.PI - pimp.getAngle()));
      pimp.setCharging(false);
   }

   @Override
   public void collidedRight() {
      pimp.setAngle((float) (Math.PI - pimp.getAngle()));
      pimp.setCharging(false);
   }

   @Override
   public void collidedUp() {
      pimp.setAngle(-pimp.getAngle());
      pimp.setCharging(false);
   }

   public boolean isRotatingLeft() {
      return rotatingLeft;
   }

   public void setRotatingLeft(boolean rotatingLeft) {
      this.rotatingLeft = rotatingLeft;
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
}
