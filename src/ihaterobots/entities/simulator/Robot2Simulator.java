/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.simulator;

import ihaterobots.entities.EntityListener;
import ihaterobots.entities.Robot2;
import ihaterobots.game.Utils;
import ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public class Robot2Simulator extends BaseEntitySimulator implements EntityListener {
   private static final float SPEED = .06f;
   private static final float FALLING_SPEED = .1f;
   private static final float FLYING_ACCEL = .001f;
   private Robot2 robot;

   public Robot2Simulator(Robot2 robot1) {
      super(robot1);
      this.robot = robot1;
      robot1.addEntityListener(this);
   }

   @Override
   protected void updateStates(int delta) {
      if (parent.isClimbing()) {
         // drop from ladder
         if ((!parent.isOnLadder() && !parent.isLadderBelow())
                 || (parent.isOnGround() && parent.isActionDown())
                 || (parent.isOnGround() && parent.isOnLadderDown())) {
            parent.setClimbing(false);
         }
      }
      if (robot.isFlying()) {
         if (!robot.isActionUp() || robot.getFuel() == 0) {
            robot.setFlying(false);
         }
         if (robot.isOnLadder() && robot.isActionUp()) {
            robot.setFlying(false);
            robot.setClimbing(true);
         }
      }
      if (parent.isFalling()) {
         // land on top of ladder & ground, but not on ladder
         if (parent.isOnGround()
                 || parent.isOnTopOfLadder()
                 || parent.isClimbing()) {
            parent.setFalling(false);
            // make sure we are at right pos
            if (parent.isOnTopOfLadder()) {
               int tileY = parent.getEnv().getMap().getTilePosY(parent.getPosY());
               float posY = (tileY + 1) * Utils.TILE_SIZE - parent.getHeigth() / 2;
               parent.setPosY(posY);
            }
         }
      }

      if (parent.isActionDown() != parent.isActionUp()) {
         // start climbing
         if (parent.isLadderBelow() || parent.isOnLadder()) {
            parent.setClimbing(true);
         } else if (parent.isActionDown() && parent.isOnTeleport()) {
            // start teleporting
            teleport();
         } else if (robot.isActionUp() && robot.getFuel() > 0) {
            // start flying
            robot.setFlying(true);
         }
      }

      if (!parent.isOnGround()
              && !parent.isClimbing()
              && !parent.isOnTopOfLadder()
              && !parent.isLadderBelow()) { // dont fall when crossing a ladder
         parent.setFalling(true);
      }

      if (robot.isActionLeft() != robot.isActionRight()) {
         robot.setMoving(true);
      } else {
         robot.setMoving(false);
      }
   }

   @Override
   protected void updateAccel(int delta) {
      super.updateAccel(delta);
      if (robot.isFlying()) {
         final boolean up = robot.isActionUp();
         final boolean down = robot.isActionDown();
         final boolean right = robot.isActionRight();
         final boolean left = robot.isActionLeft();

         if (up != down) {
            if (up) {
               robot.setAccelY(-FLYING_ACCEL);
            }
            if (left != right) {
               robot.setAccelX(left ? -FLYING_ACCEL : FLYING_ACCEL);
            }
         }
      }
   }

   @Override
   protected void updateSpeed(int delta) {
      super.updateSpeed(delta);
      final boolean up = robot.isActionUp();
      final boolean down = robot.isActionDown();
      final boolean right = robot.isActionRight();
      final boolean left = robot.isActionLeft();

      if (robot.isFlying()) {
         if (robot.getSpeedX() < -SPEED) {
            robot.setSpeedX(-SPEED);
         }
         if (robot.getSpeedX() > SPEED) {
            robot.setSpeedX(SPEED);
         }
         if (robot.getSpeedY() < -SPEED) {
            robot.setSpeedY(-SPEED);
         }
         return;
      }
      if (robot.isClimbing()) {
         if (left != right) {
            robot.setSpeedX(SPEED * (left ? -1 : 1));
         } else {
            robot.setSpeedX(0);
         }
         if (up != down) {
            robot.setSpeedY(SPEED * (up ? -1 : 1));
         } else {
            robot.setSpeedY(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (robot.isFalling()) {
         robot.setSpeedY(FALLING_SPEED);
         if (left != right) {
            robot.setSpeedX(SPEED * .2f * (left ? -1 : 1));
         } else {
            robot.setSpeedX(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (left != right) {
         robot.setSpeedX(SPEED * (left ? -1 : 1));
      } else {
         robot.setSpeedX(0);
      }
      robot.setSpeedY(0);
      updateSpeedOnSpecialTiles();
   }

   @Override
   protected void updateSpeedOnSpecialTiles() {
      super.updateSpeedOnSpecialTiles();
      if (robot.isOnGrass() && Math.abs(robot.getSpeedX()) > SPEED * .5f) {
         robot.setSpeedX(SPEED * .5f * (robot.isActionLeft() ? -1 : 1));
         return;
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
      if (!staticCollisionCheckNeeded) {
         return;
      }

      checkMapCollisionOnPlaceovers(map, centerX, centerY, leftX, rightX, topY, botY, tilePosX, tilePosY);
      checkAutoTrigger(map, tilePosX, tilePosY);
      staticCollisionCheckNeeded = false;
   }

   @Override
   public void collidedDown() {
      robot.setSpeedY(0);
   }

   @Override
   public void collidedLeft() {
      robot.setSpeedX(0);
   }

   @Override
   public void collidedRight() {
      robot.setSpeedX(0);
   }

   @Override
   public void collidedUp() {
      robot.setSpeedY(0);
   }

   @Override
   public void climbingChanged() {
   }

   @Override
   public void facingLeftChanged() {
   }

   @Override
   public void fallingChanged() {
   }

   @Override
   public void movingChanged() {
   }

   @Override
   public void stuckChanged() {
   }

   @Override
   public void teleportingChanged() {
   }
}
