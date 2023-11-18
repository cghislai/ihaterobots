/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.simulator;

import com.charlyghislain.ihaterobots.entities.EntityListener;
import com.charlyghislain.ihaterobots.entities.controller.Controller.Action;
import com.charlyghislain.ihaterobots.entities.Digger;
import com.charlyghislain.ihaterobots.game.map.BreakableEntity;
import com.charlyghislain.ihaterobots.game.map.QuickBreakEntity;
import com.charlyghislain.ihaterobots.game.map.TileDrawer;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author cghislai
 */
public class DiggerSimulator extends BaseEntitySimulator implements EntityListener {
   private static final float WALKING_SPEED = .06f;
   private static final float FALLING_SPEED = .1f;
   private static final float CLIMBING_SPEED = .04f;
   private Digger digger;
   private int breakingTilePosX = -1;
   private int breakingTilePosY = -1;
   private int breakingTimer = -1;
   private int brokenTileId;

   public DiggerSimulator(Digger digger) {
      super(digger);
      this.digger = digger;
      digger.addEntityListener(this);
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
         if (parent.isActionDown() && parent.isOnTeleport() && !digger.isFiring()) {
            teleport();
         }
      }

      if (!parent.isOnGround()
              && !parent.isClimbing()
              && !parent.isOnTopOfLadder()
              && !parent.isLadderBelow()) { // dont fall when crossing a ladder
         parent.setFalling(true);
      }

//      if (!digger.isFiring()) {
      digger.setMoving((parent.isActionDown() != parent.isActionUp()) || (parent.isActionLeft() != parent.isActionRight()));
//      }
      digger.setFiring(digger.isActionFire());
   }

   @Override
   protected void updateAccel(int delta) {
   }

   @Override
   protected void updateSpeed(int delta) {
      final boolean up = digger.isActionUp();
      final boolean down = digger.isActionDown();
      final boolean right = digger.isActionRight();
      final boolean left = digger.isActionLeft();

      if (digger.isClimbing()) {
         staticCollisionCheckNeeded = true;
         if (left != right) {
            digger.setSpeedX(CLIMBING_SPEED * (left ? -1 : 1));
         } else {
            digger.setSpeedX(0);
         }
         if (up != down) {
            digger.setSpeedY(CLIMBING_SPEED * (up ? -1 : 1));
         } else {
            digger.setSpeedY(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (digger.isFalling()) {
         digger.setSpeedY(FALLING_SPEED);
         staticCollisionCheckNeeded = true;
         if (left != right) {
            digger.setSpeedX(WALKING_SPEED * .2f * (left ? -1 : 1));
         } else {
            digger.setSpeedX(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }
      if (left != right) {
         digger.setSpeedX(WALKING_SPEED * (left ? -1 : 1));
         staticCollisionCheckNeeded = true;
      } else {
         digger.setSpeedX(0);
      }
      digger.setSpeedY(0);
      updateSpeedOnSpecialTiles();
   }

   @Override
   protected void updateSpeedOnSpecialTiles() {
      super.updateSpeedOnSpecialTiles();
      if (digger.isOnGrass() && Math.abs(digger.getSpeedX()) > WALKING_SPEED * .5f) {
         digger.setSpeedX(WALKING_SPEED * .5f * (digger.isActionLeft() ? -1 : 1));
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
      int tileSize = Utils.TILE_SIZE;

      checkBreakBlocksCollisions(map, centerX, topY, tilePosY, tilePosX, botY, leftX, centerY, rightX);

      // HERE
      if (checkStuckStatus(map, centerX, centerY)) {
         return;
      }
      if (!staticCollisionCheckNeeded && !digger.isStuck()) {
         return;
      }

      checkMapCollisionNoPlaceOver(map, centerX, centerY, leftX, rightX, topY, botY, tilePosX, tilePosY);
      checkAutoTrigger(map, tilePosX, tilePosY);
      staticCollisionCheckNeeded = false;
   }

   protected void checkBreakBlocksCollisions(GameMap map, float centerX, float topY, int tilePosY, int tilePosX, float botY, float leftX, float centerY, float rightX) {
      int breakTilePoxX = -1;
      int breakTilePosY = -1;
      if (digger.isActionFire()) {
         if (digger.isStuck()) {
            breakTilePoxX = tilePosX;
            breakTilePosY = tilePosY;
         } else if (digger.isActionLeft()) {
            if (map.isTileBreakableFromRight(leftX - 3, centerY)) {
               breakTilePoxX = tilePosX - 1;
               breakTilePosY = tilePosY;
            }
         } else if (digger.isActionRight()) {
            if (map.isTileBreakableFromLeft(rightX + 3, centerY)) {
               breakTilePoxX = tilePosX + 1;
               breakTilePosY = tilePosY;
            }
         } else if (digger.isActionUp()) {
            if (map.isTileBreakableFromBottom(centerX, topY - 3)) {
               breakTilePosY = tilePosY - 1;
               breakTilePoxX = tilePosX;
            }
         } else if (digger.isActionDown()) {
            if (map.isTileBreakableFromTop(centerX, botY + 3)) {
               breakTilePosY = tilePosY + 1;
               breakTilePoxX = tilePosX;
            }
         } else {
            breakTilePosY = -1;
            breakTilePoxX = -1;
         }
         if (breakingTilePosX != breakTilePoxX || breakingTilePosY != breakTilePosY) {
            switchBreakBlock(breakTilePoxX, breakTilePosY);
         }
      }
   }

   @Override
   protected void updateTimers(int delta) {
      super.updateTimers(delta);
      if (digger.isActionFire()) {
         if (breakingTimer >= 0) {
            breakingTimer -= delta;
            if (breakingTimer < 0) {
               breakTile();
            }
         }
      } else {
         breakingTilePosX = -1;
         breakingTilePosY = -1;
         breakingTimer = -1;
      }
   }

   private void switchBreakBlock(int tilePosX, int tilePosY) {
      GameMap map = digger.getEnv().getMap();
      int tileId = map.getTileId(tilePosX, tilePosY);
      int timer = Utils.getBreakableTileTimeout(tileId) * 3;
      breakingTimer = timer;
      breakingTilePosX = tilePosX;
      breakingTilePosY = tilePosY;
   }

   private void breakTile() {
      GameMap map = digger.getEnv().getMap();
      brokenTileId = map.getTileId(breakingTilePosX, breakingTilePosY);
      if (brokenTileId == Utils.TILE_ID_QUICK_BREAK) {
         QuickBreakEntity entity = new QuickBreakEntity(50, Utils.QUICK_BREAK_TIMER * 2);
         entity.setPosX(breakingTilePosX);
         entity.setPosY(breakingTilePosY);
         entity.register(digger.getEnv());
      } else if (brokenTileId == Utils.TILE_ID_WOODCRATE) {
         map.setTile(breakingTilePosX, breakingTilePosY, Utils.MAP_Z_NORMAL, -1);
         digger.getEnv().getSoundManager().playBoxBreak();
      } else {
         int placeOverId = map.getTileId(breakingTilePosX, breakingTilePosY, Utils.MAP_Z_PLACEOVER_NORMAL);
         TileDrawer placeOverDrawer = map.getMapDrawer().getTileDrawer(placeOverId);
         final int animTime = Utils.getBreakableTileTimeout(brokenTileId) * 4;
         BreakableEntity breakableEntity = null;

         switch (brokenTileId) {
            case Utils.TILE_ID_DIRT:
               breakableEntity = new BreakableEntity(Utils.SHEET_BREAKABLE_DIRT, animTime, 100, brokenTileId);
               break;
            case Utils.TILE_ID_BRICKS:
               breakableEntity = new BreakableEntity(Utils.SHEET_BREAKABLE_BRICKS, animTime, 150, brokenTileId);
               break;
            case Utils.TILE_ID_SAND:
               breakableEntity = new BreakableEntity(Utils.SHEET_BREAKABLE_SAND, animTime, 100, brokenTileId);
               break;
         }
         if (breakableEntity == null) {
            return;
         }
         breakableEntity.setPlaceOverDrawer(placeOverDrawer);
         breakableEntity.setPosX(breakingTilePosX);
         breakableEntity.setPosY(breakingTilePosY);
         breakableEntity.register(digger.getEnv());
      }
      digger.getController().blockBroken(breakingTilePosX, breakingTilePosY);
      breakingTilePosX = -1;
      breakingTilePosY = -1;
      breakingTimer = -1;
      staticCollisionCheckNeeded = true; // update map collision
   }

   @Override
   public void collidedDown() {
      digger.setSpeedY(0);
   }

   @Override
   public void collidedLeft() {
      digger.setSpeedX(0);
   }

   @Override
   public void collidedRight() {
      digger.setSpeedX(0);
   }

   @Override
   public void collidedUp() {
      digger.setSpeedY(0);
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
      if (digger.isStuck()) {
         digger.processAction(Action.FIRE, false);
         breakingTilePosX = -1;
         breakingTilePosY = -1;
         breakingTimer = -1;
      }
   }

   @Override
   public void teleportingChanged() {
   }

   public boolean isBreakingBlock() {
      return breakingTilePosX >= 0 && breakingTilePosY >= 0;
   }
   
}
