/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.controller;

import com.charlyghislain.ihaterobots.entities.Rocket;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public final class RocketController extends AIController {
   private Action currentAction;
   private Rocket rocket;

   public RocketController(Rocket robot1) {
      super(robot1, robot1);
      rocket = robot1;
      rocket.addEntityListener(this);
      switchAction(Action.UP);
      currentAction = Action.UP;
   }

   @Override
   public void collidedLeft() {
      GameMap map = rocket.getEnv().getMap();
      float centerX = rocket.getPosX();
      float centerY = rocket.getPosY();
      int tileSize = Utils.TILE_SIZE;
      boolean topSolid = map.isTileSolid(centerX, centerY - tileSize);
      boolean botSolid = map.isTileSolid(centerX, centerY + tileSize);
      boolean onTeleport = map.isTileTeleportMapCoords(map.getTilePosX(rocket.getPosX()),
                                                       map.getTilePosY(rocket.getPosY()));

      if (!topSolid) {
         switchAction(Action.UP);
      } else if (!botSolid || (onTeleport && !triedThisTeleport)) {
         switchAction(Action.DOWN);
      } else {
         switchAction(Action.RIGHT);
      }
   }

   @Override
   public void collidedRight() {
      GameMap map = rocket.getEnv().getMap();
      float centerX = rocket.getPosX();
      float centerY = rocket.getPosY();
      int tileSize = Utils.TILE_SIZE;
      boolean topSolid = map.isTileSolid(centerX, centerY - tileSize);
      boolean botSolid = map.isTileSolid(centerX, centerY + tileSize);
      boolean onTeleport = map.isTileTeleportMapCoords(map.getTilePosX(rocket.getPosX()),
                                                       map.getTilePosY(rocket.getPosY()));
      if (!botSolid || (onTeleport && !triedThisTeleport)) {
         switchAction(Action.DOWN);
      } else if (!topSolid) {
         switchAction(Action.UP);
      } else {
         switchAction(Action.LEFT);
      }
   }

   @Override
   public void collidedUp() {
      GameMap map = rocket.getEnv().getMap();
      float centerX = rocket.getPosX();
      float centerY = rocket.getPosY();
      int tileSize = Utils.TILE_SIZE;
      boolean leftSolid = map.isTileSolid(centerX - tileSize, centerY);
      boolean rightSolid = map.isTileSolid(centerX + tileSize, centerY);
      if (!rightSolid) {
         switchAction(Action.RIGHT);
      } else if (!leftSolid) {
         switchAction(Action.LEFT);
      } else {
         switchAction(Action.DOWN);
      }
   }

   @Override
   public void collidedDown() {
      GameMap map = rocket.getEnv().getMap();
      float centerX = rocket.getPosX();
      float centerY = rocket.getPosY();
      rocket.setOnTeleport(map.isTileTeleport(centerX, centerY));
      int tileSize = Utils.TILE_SIZE;
      boolean leftSolid = map.isTileSolid(centerX - tileSize, centerY);
      boolean rightSolid = map.isTileSolid(centerX + tileSize, centerY);
      if (!leftSolid) {
         switchAction(Action.LEFT);
      } else if (!rightSolid) {
         switchAction(Action.RIGHT);
      } else {
         switchAction(Action.UP);
      }
   }

   @Override
   public void update(int delta) {
      if (!parent.isOnTeleport()) {
         triedThisTeleport = false;
      }
   }

   public void switchAction(Action action) {
      if (currentAction != null) {
         rocket.processAction(currentAction, false);
      }
      if (action != null) {
         rocket.processAction(action, true);
      }
      currentAction = action;
      if (currentAction == Action.LEFT || currentAction == Action.RIGHT) {
         rocket.setWidth(30);
         rocket.setHeigth(10);
      } else {
         rocket.setWidth(10);
         rocket.setHeigth(30);
      }
   }

   @Override
   public void teleportingChanged() {
//      super.teleportingChanged();
      if (parent.isTeleporting()) {
         triedThisTeleport = true;
      } else {
         collidedDown();
      }
   }
}
