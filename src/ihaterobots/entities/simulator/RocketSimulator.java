/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.simulator;

import ihaterobots.entities.simulator.BaseEntitySimulator;
import ihaterobots.entities.Rocket;
import ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public class RocketSimulator extends BaseEntitySimulator {
   private static final float FLYING_SPEED = .1f;
   private Rocket rocket;
   private int tilePosX;
   private int tilePosY;

   public RocketSimulator(Rocket rocket) {
      super(rocket);
      this.rocket = rocket;
   }

   @Override
   public void update(int delta) {
      if (rocket.isOnTeleport() && rocket.isActionDown()) {
         teleport();
      }
      updateSpeed(delta);
      updatePos(delta);
      updateTimers(delta);
      checkStaticCollisions(delta);
   }

   @Override
   protected void updateSpeed(int delta) {
      float speedX = rocket.getSpeedX();
      float speedY = rocket.getSpeedY();
      if (rocket.isActionLeft()) {
         speedX = -FLYING_SPEED;
         speedY = 0;
      } else if (rocket.isActionRight()) {
         speedX = FLYING_SPEED;
         speedY = 0;
      } else if (rocket.isActionUp()) {
         speedY = -FLYING_SPEED;
         speedX = 0;
      } else if (rocket.isActionDown()) {
         speedY = FLYING_SPEED;
         speedX = 0;
      }
      rocket.setSpeedX(speedX);
      rocket.setSpeedY(speedY);
   }

   @Override
   protected void checkTileType(GameMap map, float leftX, float botY, float rightX, float topY, float centerX, float centerY) {
      super.checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
      rocket.setOnTeleport(map.isTileTeleportMapCoords(map.getTilePosX(rocket.getPosX()),
                                                       map.getTilePosY(rocket.getPosY())));
   }
}
