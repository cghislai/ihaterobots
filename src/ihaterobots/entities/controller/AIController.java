/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.controller;

import ihaterobots.entities.BaseEntity;
import ihaterobots.entities.EntityListener;
import ihaterobots.entities.Player;
import ihaterobots.game.map.GameMap;
import ihaterobots.interfaces.Controllable;

/**
 *
 * @author charly
 */
public class AIController extends Controller implements EntityListener {
   protected static final int Y_CUTOFF = 10;
   protected Action currentActionX;
   protected Action currentActionY;
   protected float playerPosXrel;
   protected float playerPosYrel;
   protected float playerangle;
   protected float playerSqrDist;
   protected boolean fromLadder; // check if it comes from a ladder;
   protected int lastTriedLadderCount;
   protected int lastTriedLadderDropY; //tile coords
   protected int lastTriedLadderX; //tile coords
   protected int lastTriedLadderY; //tile coords
   protected boolean triedThisLadder; // don't consider more than once per time crossing a ladder
   protected boolean triedThisTeleport; // don't consider more than once per time crossing a ladder
   protected BaseEntity parent;

   public AIController(Controllable controllable, BaseEntity parent) {
      super(controllable);
      this.parent = parent;
   }

   protected void updatePlayerPosInfo(Player player, BaseEntity entity) {
      if (player == null) {
         playerSqrDist = -1;
         return;
      }
      float px = player.getPosX();
      float py = player.getPosY();
      float x = entity.getPosX();
      float y = entity.getPosY();
      playerSqrDist = (float) (Math.pow(px - x, 2) + Math.pow(py - y, 2));
      playerPosXrel = px - x;
      playerPosYrel = py - y;
   }

   protected void updatePlayerRotationInfo() {
      float dist = (float) Math.sqrt(playerSqrDist);
      float angle = 0;
      if (playerPosYrel < 0) {
         angle = (float) Math.acos(playerPosXrel / dist);
      } else {
         angle = (float) -Math.acos(playerPosXrel / dist);
      }
      playerangle = (float) (angle + Math.PI * 2 % Math.PI * 2);
   }

   /**
    * 
    * @param fromAngle
    * @param toAngle
    * @return angle between -PI;PI; positive if needs to go left
    */
   protected float getAngleDiff(float fromAngle, float toAngle) {
      double dif = ((toAngle - fromAngle) % (2 * Math.PI));
      if (fromAngle > toAngle) {
         dif += (2 * Math.PI);
      }
      if (dif > Math.PI) {
         dif = 2 * Math.PI - dif;
      }
      return (float) dif;
   }

   @Override
   public void movingChanged() {
   }

   @Override
   public void teleportingChanged() {
      if (!parent.isTeleporting()) {
         switchActionX();
         switchActionY(null);
      }
   }

   @Override
   public void climbingChanged() {
      if (!parent.isClimbing()) {
         lastTriedLadderDropY = -1;
         fromLadder = true;
         switchActionX();
         switchActionY(null);
         return;
      }
      if (parent.isClimbing()) {
         switchActionX(null);
      }
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
   }

   @Override
   public void collidedRight() {
   }

   @Override
   public void collidedUp() {
   }

   @Override
   public void collidedDown() {
   }

   private void climbOnLadder() {
      if (Math.random() < 0.5F) {
         climbOnLadder(true);
      } else {
         climbOnLadder(false);
      }
   }

   /**
    *
    * @param b  true: up ; false: down
    */
   private void climbOnLadder(boolean b) {
      if (parent.isClimbing()) {
         return;
      }
      switchActionX(null);
      GameMap map = parent.getEnv().getMap();
      lastTriedLadderDropY = map.getTilePosY(parent.getPosY());
      if (!parent.isOnLadder() && parent.isLadderBelow()) {
         switchActionY(Action.DOWN);
         return;
      }
      if (parent.isOnLadder() && !parent.isLadderBelow()) {
         switchActionY(Action.UP);
         return;
      }
      // Always climb if UP/DOWN on ladder
      if (b) {
         switchActionY(Action.UP);
      } else {
         switchActionY(Action.DOWN);
      }
   }

   private void condiderDropOffLadder() {
      GameMap map = parent.getEnv().getMap();
      // consider dropping only when are at right height
      int botY = map.getTilePosY(parent.getPosY() + parent.getHeigth() / 2);
      int topY = map.getTilePosY(parent.getPosY() - parent.getHeigth() / 2);
      if (botY != topY) {
         return;
      }
      int tilePosY = botY;
      if (lastTriedLadderDropY == tilePosY) {
         return;
      }
      lastTriedLadderDropY = tilePosY;
      updatePlayerPosInfo(parent.getEnv().getPlayer(), parent);
      // drop off randomly or if we want to follow player
      if (Math.random() < 0.7F) {
         return;
      }
      if ((parent.isActionUp() && playerPosYrel < 0) || (parent.isActionDown() && playerPosYrel > 0)) {
         if (Math.random() < 0.5F) {
            return;
         }
      }
      int tilePosX = map.getTilePosX(parent.getPosX());
      boolean leftSolid = map.isTileSolidMapCoords(tilePosX - 1, tilePosY);
      boolean leftwalkable = map.isTileSolidMapCoords(tilePosX - 1, tilePosY + 1) || map.isTileLadderMapCoords(tilePosX - 1, tilePosY);
      boolean rightSolid = map.isTileSolidMapCoords(tilePosX + 1, tilePosY);
      boolean rightwalkable = map.isTileSolidMapCoords(tilePosX + 1, tilePosY + 1) || map.isTileLadderMapCoords(tilePosX + 1, tilePosY);
      if (!leftSolid && leftwalkable && (rightSolid || !rightwalkable)) {
         switchActionX(Action.LEFT);
         switchActionY(null);
         return;
      }
      if (!rightSolid && rightwalkable && (leftSolid || !leftwalkable)) {
         switchActionX(Action.RIGHT);
         switchActionY(null);
         return;
      }
      if (!leftSolid && !rightSolid && leftwalkable && rightwalkable) {
         switchActionX();
         switchActionY(null);
         return;
      }
   }

   protected void considerClimbing() {
      if (parent.isClimbing()) {
         return;
      }
      if (triedThisLadder) {
         return;
      }
      triedThisLadder = true;
      // Update lastTriedLadder vars
      GameMap map = parent.getEnv().getMap();
      int tilePosX = map.getTilePosX(parent.getPosX());
      int tilePosY = map.getTilePosY(parent.getPosY());
      if (tilePosX == lastTriedLadderX && tilePosY == lastTriedLadderY) {
         lastTriedLadderCount++;
      } else {
         lastTriedLadderX = tilePosX;
         lastTriedLadderY = tilePosY;
         lastTriedLadderCount = 1;
      }
      // Dont go over the same ladder more than 5 times
      if (lastTriedLadderCount >= 5) {
         climbOnLadder();
         return;
      }
      if (fromLadder && Math.random() < 0.9F) {
         // keep a chance to climb back the same ladder
         return;
      }
      if (Math.random() < 0.2F) {
         // keep a chance to not climb
         return;
      }
      updatePlayerPosInfo(parent.getEnv().getPlayer(), parent);
      if (playerSqrDist < 0) {
         //no player
         climbOnLadder();
         return;
      }
      if (Math.abs(playerPosYrel) < Robot2Controller.Y_CUTOFF) {
         // SAME HEIGTH
         return;
      }
      // IF player above go up if possible. Higher random probability since robot keeps falling
      if (parent.isOnLadder() && (playerPosYrel < 0)) {
         // PLAYER ABOVE
         climbOnLadder(true);
         return;
      }
      // GO DOWN if player down
      if (parent.isOnTopOfLadder() && (playerPosYrel > 0)) {
         // PLAYER BELOW
         climbOnLadder(false);
         return;
      }
   }

   private void considerTeleporting() {
      if (triedThisTeleport) {
         return;
      }
      triedThisTeleport = true;
      if (Math.random() < 0.4F) {
         switchActionY(Action.DOWN);
         return;
      }
      if (Math.random() < 0.4F) {
         return;
      }
      updatePlayerPosInfo(parent.getEnv().getPlayer(), parent);
      if (playerSqrDist < 100 * 100) {
         return;
      }
      switchActionY(Action.DOWN);
   }

   public void update(int delta) {
      if (!parent.isOnLadder() && !parent.isLadderBelow()) {
         fromLadder = false;
         triedThisLadder = false;
      }
      if (!parent.isOnTeleport()) {
         triedThisTeleport = false;
      }
      if (!parent.isClimbing() && (parent.isOnLadder() || parent.isLadderBelow())) {
         considerClimbing();
         return;
      }
      if (parent.isClimbing() && parent.isOnLadder()) {
         //dont drop on top of ladder
         condiderDropOffLadder();
         return;
      }
      if (parent.isOnTeleport()) {
         considerTeleporting();
         return;
      }

   }

   protected void switchActionX() {
      if (currentActionX != null) {
         return;
      }
      updatePlayerPosInfo(parent.getEnv().getPlayer(), parent);
      if (playerSqrDist < 0 || Math.random() < 0.2F) {
         if (Math.random() < 0.5F) {
            switchActionX(Action.RIGHT);
         } else {
            switchActionX(Action.LEFT);
         }
         return;
      }
      // avoid falling if we want to go up
      if (playerPosYrel < 0) {
         GameMap map = parent.getEnv().getMap();
         int tilePosX = map.getTilePosX(parent.getPosX());
         int tilePosY = map.getTilePosY(parent.getPosY());
         boolean edgeOnLeft = true;
         boolean edgeOnRight = true;
         for (int x = 1; x < 10; x++) {
            if (!map.isTileSolidMapCoords(tilePosX - x, tilePosY + 1) && !map.isTileLadder(tilePosX - x, tilePosY + 1)) {
               break;
            }
            if (map.isTileSolidMapCoords(tilePosX - x, tilePosY) || map.isTileLadder(tilePosX - x, tilePosY)) {
               edgeOnLeft = false;
               break;
            }
         }
         for (int x = 1; x < 10; x++) {
            if (!map.isTileSolidMapCoords(tilePosX + x, tilePosY + 1) && !map.isTileLadder(tilePosX + x, tilePosY + 1)) {
               break;
            }
            if (map.isTileSolidMapCoords(tilePosX + x, tilePosY) || map.isTileLadder(tilePosX + x, tilePosY)) {
               edgeOnRight = false;
               break;
            }
         }
         if (edgeOnLeft && !edgeOnRight) {
            switchActionX(Action.RIGHT);
            return;
         }
         if (edgeOnRight && !edgeOnLeft) {
            switchActionX(Action.LEFT);
            return;
         }
         // fallback
      }
      if (playerPosXrel < 0) {
         switchActionX(Action.LEFT);
      } else {
         switchActionX(Action.RIGHT);
      }
   }

   protected void switchActionX(Action action) {
      if (currentActionX != null) {
         controllable.processAction(currentActionX, false);
      }
      if (action != null) {
         controllable.processAction(action, true);
      }
      currentActionX = action;
   }

   protected void switchActionY(Action action) {
      if (currentActionY != null) {
         controllable.processAction(currentActionY, false);
      }
      if (action != null) {
         controllable.processAction(action, true);
      }
      currentActionY = action;
   }
}
