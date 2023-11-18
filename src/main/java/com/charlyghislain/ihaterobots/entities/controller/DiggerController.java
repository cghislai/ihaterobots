/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.controller;

import com.charlyghislain.ihaterobots.entities.Digger;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author cghislai
 */
public class DiggerController extends AIController {
   private Digger digger;
   private final static int STUCK_TME = 5000;
   private final static int DIG_TIME = 3000;
   private int lastBrokenX;
   private int lastBrokenY;
   private int stuckTimer;
   private int digTimer;

   public DiggerController(Digger digger) {
      super(digger, digger);
      this.digger = digger;
      digger.addEntityListener(this);
      switchActionX(Action.RIGHT);
   }

   @Override
   public void update(int delta) {
      super.update(delta);
      if (stuckTimer > 0) {
         stuckTimer -= delta;
         if (stuckTimer <= 0) {
            switchActionX(null);
            switchActionY(null);
            digger.processAction(Action.FIRE, true);
         }
      }
      if (!digger.getSimulator().isBreakingBlock() && digger.isFiring()) {
         digger.processAction(Action.FIRE, false);
         digger.processAction(Action.DOWN, false);
         switchActionX();
      }
      if (digTimer > 0) {
         digTimer -= delta;
      } else {
         considerBreaking();
      }
   }

   @Override
   public void collidedLeft() {
      if (digger.isFiring()) {
         return;
      }
      if (!considerBreaking()) {
         switchActionX(Action.RIGHT);
      }
      super.collidedLeft();
   }

   @Override
   public void collidedRight() {
      if (digger.isFiring()) {
         return;
      }
      if (!considerBreaking()) {
         switchActionX(Action.LEFT);
      }
   }

   @Override
   public void collidedUp() {
      if (digger.isClimbing()) {
         digger.setClimbing(false);
      }
   }

   @Override
   protected void considerClimbing() {
      if (digger.isFiring()) {
         return;
      }
      super.considerClimbing();
   }
   
   

   private boolean considerBreaking() {
      GameMap map = digger.getEnv().getMap();
      int tilePosX = map.getTilePosX(digger.getPosX());
      int tilePosY = map.getTilePosY(digger.getPosY());
      if ((tilePosX != lastBrokenX && tilePosY != lastBrokenY)
              && Math.random() < .2f) {
         return false;
      }
      if (digger.isFiring() || digger.isFalling() ) {
         return false;
      }
      if (digger.isOnRollLeft() || digger.isOnRollRight()) {
         return false;
      }
      digTimer = DIG_TIME;
      updatePlayerPosInfo(digger.getEnv().getPlayer(), digger);

      if (playerPosYrel > 0 || Math.random() < .1f) {
         if (map.isTileBreakableFromTopMapCoords(tilePosX, tilePosY + 1)) {
            switchActionY(Action.DOWN);
            switchActionX(null);
            digger.processAction(Action.FIRE, true);
            return true;
         }
      }
      if (digger.isFacingLeft()) {
         if (map.isTileBreakableFromRightMapCoords(tilePosX - 1, tilePosY)) {
            switchActionY(null);
            switchActionX(Action.LEFT);
            digger.processAction(Action.FIRE, true);
            return true;
         }
      }
      if (!digger.isFacingLeft()) {
         if (map.isTileBreakableFromLeftMapCoords(tilePosX + 1, tilePosY)) {
            switchActionY(null);
            switchActionX(Action.RIGHT);
            digger.processAction(Action.FIRE, true);
            return true;
         }
      }
      return false;
   }

   public void blockBroken(int x, int y) {
      digger.processAction(Action.FIRE, false);
      lastBrokenX = x;
      lastBrokenY = y;
      switchActionX();
      digTimer = -1;
   }

   @Override
   public void stuckChanged() {
      if (digger.isStuck()) {
         stuckTimer = STUCK_TME;
         switchActionX(null);
         switchActionY(null);
         digger.processAction(Action.FIRE, false);
      } else {
         switchActionX();
      }
   }
}
