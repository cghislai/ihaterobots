/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.simulator;

import ihaterobots.entities.EntityListener;
import ihaterobots.entities.Robot1;
import ihaterobots.game.Utils;

/**
 *
 * @author charly
 */
public class Robot1Simulator extends BaseEntitySimulator implements EntityListener {
   private static final float WALKING_SPEED = .08f;
   private static final float FALLING_SPEED = .1f;
   private static final float CLIMBING_SPEED = .06f;
   private Robot1 robot1;

   public Robot1Simulator(Robot1 robot1) {
      super(robot1);
      this.robot1 = robot1;
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
         }
         // start teleporting
         if (parent.isActionDown() && parent.isOnTeleport()) {
            teleport();
         }
      }

      if (!parent.isOnGround()
              && !parent.isClimbing()
              && !parent.isOnTopOfLadder()
              && !parent.isLadderBelow()) { // dont fall when crossing a ladder
         parent.setFalling(true);
      }

      if (robot1.isActionLeft() != robot1.isActionRight()) {
         robot1.setMoving(true);
      } else {
         robot1.setMoving(false);
      }
   }

   @Override
   protected void updateAccel(int delta) {
   }

   @Override
   protected void updateSpeed(int delta) {
      final boolean up = robot1.isActionUp();
      final boolean down = robot1.isActionDown();
      final boolean right = robot1.isActionRight();
      final boolean left = robot1.isActionLeft();

      if (robot1.isClimbing()) {
         if (left != right) {
            robot1.setSpeedX(CLIMBING_SPEED * (left ? -1 : 1));
         } else {
            robot1.setSpeedX(0);
         }
         if (up != down) {
            robot1.setSpeedY(CLIMBING_SPEED * (up ? -1 : 1));
         } else {
            robot1.setSpeedY(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (robot1.isFalling()) {
         robot1.setSpeedY(FALLING_SPEED);
         if (left != right) {
            robot1.setSpeedX(WALKING_SPEED * .2f * (left ? -1 : 1));
         } else {
            robot1.setSpeedX(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (left != right) {
         robot1.setSpeedX(WALKING_SPEED * (left ? -1 : 1));
      } else {
         robot1.setSpeedX(0);
      }
      robot1.setSpeedY(0);
      updateSpeedOnSpecialTiles();
   }

   @Override
   protected void updateSpeedOnSpecialTiles() {
      super.updateSpeedOnSpecialTiles();
      if (robot1.isOnGrass() && Math.abs(robot1.getSpeedX()) > WALKING_SPEED * .5f) {
         robot1.setSpeedX(WALKING_SPEED * .5f * (robot1.isActionLeft() ? -1 : 1));
         return;
      }
   }

   @Override
   public void collidedDown() {
      robot1.setSpeedY(0);
   }

   @Override
   public void collidedLeft() {
      robot1.setSpeedX(0);
   }

   @Override
   public void collidedRight() {
      robot1.setSpeedX(0);
   }

   @Override
   public void collidedUp() {
      robot1.setSpeedY(0);
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
