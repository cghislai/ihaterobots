/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.simulator;

import ihaterobots.entities.EntityListener;
import ihaterobots.entities.Player;
import ihaterobots.game.env.GameManager;
import ihaterobots.game.map.BreakableEntity;
import ihaterobots.game.map.QuickBreakEntity;
import ihaterobots.game.map.TileDrawer;
import ihaterobots.game.Utils;
import ihaterobots.game.map.GameMap;
import ihaterobots.interfaces.Entity;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author charly
 */
public class PlayerSimulator extends BaseEntitySimulator implements EntityListener {
   private static final float WALKING_SPEED = .07f;
   private static final float WALKING_ACCEL = .0005f;
   private static final float FLYING_SPEED = .1f;
   private static final float FLYING_ACCEL = .0002f;
   private static final float JUMP_SPEED = .21f;
   private static final float JUMP_ACCEL = .00033f;
   private static final float ONICE_ACCEL = .0001f;
   private static final float FALLING_SPEED = .1f;
   private int breakingTilePosX = -1;
   private int breakingTilePosY = -1;
   private int breakingTimer;
   private int brokenTileId;
   private boolean jumped; // Only 1 jump each key pres
   private Player player;

   public PlayerSimulator(Player player) {
      super(player);
      this.player = player;
      player.addEntityListener(this);
   }

   @Override
   protected void updateStates(int delta) {
      if (player.isOnDoor() && (player.isOnGround() || player.isLadderBelow())
              && player.getEnv().getGameManager().getRemainingdrops() == 0) {
         endLevel();
         return;
      }

      if (player.isFlying()) {
         if (!player.isActionUp() || player.getFuel() <= 0) {
            player.setFlying(false);
         }
      }
      if (player.isJumping()) {
         if (!player.isActionUp() || player.getSpeedY() > 0) {
            player.setJumping(false);
            player.setFalling(true);
         }
      }
      if (parent.isClimbing()) {
         // drop from ladder
         if (!parent.isOnLadder() && !parent.isLadderBelow()) {
            parent.setClimbing(false);
         }
         if (parent.isOnGround()) {
            parent.setClimbing(false);
         }
      }
      if (parent.isFalling()) {
         // land on top of ladder & ground, but not on ladder
         if (parent.isOnGround()
                 || (parent.isLadderBelow() && !parent.isOnLadder())
                 || parent.isClimbing()
                 || player.isFlying()) {
            parent.setFalling(false);
         }
      }

      if (parent.isActionDown() != parent.isActionUp()) {
         // start climbing if possible
         if (parent.isLadderBelow() || parent.isOnLadder()) {
            if (player.isActionDown() && (parent.isLadderBelow())) {
               parent.setClimbing(true);
            } else if (player.isActionUp() && parent.isOnLadder()) {
               parent.setClimbing(true);
            }
         }
         // fly or jump
         if (!player.isClimbing() && player.isActionUp()) {
            if (player.getFuel() > 0) {
               player.setFlying(true);
            } else if (player.isOnGround() || player.isOnTopOfLadder()) {
               // dont jump if firing up
               if (!jumped && (!player.isActionUp() || !player.isActionFire())) {
                  player.setJumping(true);
                  player.getEnv().getSoundManager().playJump();
               }
            }
         }
         // start teleporting
         if (parent.isActionDown() && parent.isOnTeleport() && !player.isActionFire()) {
            teleport();
         }
      }
      // fall if not on ground or ladder
      if (!parent.isOnGround()
              && !parent.isClimbing()
              && !parent.isOnTopOfLadder()
              && !parent.isLadderBelow() // dont fall when crossing a ladder
              && !player.isFlying()) {
         parent.setFalling(true);
      }


      if (player.isActionLeft() != player.isActionRight()) {
         player.setMoving(true);
      } else {
         player.setMoving(false);
      }
      if (player.isOnItem()) {
         grabItem();
      }
      if (player.isOnTrigger() && player.isActionDown()) {
         switchTriggerState();
      }
      if (player.isOnSpikes()) {
         player.die();
      }
   }

   @Override
   protected void updateAccel(int delta) {
      player.setAccelX(0);
      player.setAccelY(0);
      final boolean right = player.isActionRight();
      final boolean left = player.isActionLeft();
      final boolean up = player.isActionUp();
      final boolean down = player.isActionDown();
      if (player.isDying()) {
         player.setAccelY(FALLING_ACCEL);
         return;
      }
      if (player.isFalling()) {
         player.setAccelY(FALLING_ACCEL);
      }
      if (player.isJumping()) {
         player.setAccelY(FALLING_ACCEL - JUMP_ACCEL);
      }
      if (player.isFlying()) {
         if (left != right) {
            player.setAccelX(FLYING_ACCEL * (left ? -1 : 1));
         }
         if (up != down) {
            player.setAccelY(FLYING_ACCEL * (up ? -1 : 0));
         }
         return;
      }
      if (left != right) {
         if (player.isOnIce()) {
            player.setAccelX(ONICE_ACCEL * (left ? -1 : 1));
            return;
         }
         if (player.isOnGrass()) {
            player.setAccelX(WALKING_ACCEL * .5f * (left ? -1 : 1));
            return;
         }
         if (player.isOnGround() || player.isOnTopOfLadder() || player.isFalling()) {
            player.setAccelX(WALKING_ACCEL * (left ? -1 : 1));
            return;
         }
//            player.setAccelX(FLYING_ACCEL * (left ? -1 : 1));
      } else {
         player.setAccelX(0);
      }

   }

   @Override
   protected void updateSpeed(int delta) {
      player.setSpeedX(player.getSpeedX() + player.getAccelX() * delta);
      player.setSpeedY(player.getSpeedY() + player.getAccelY() * delta);

      if (player.isFlying()) {
         if (Math.abs(player.getSpeedX()) > FLYING_SPEED) {
            player.setSpeedX(FLYING_SPEED * (player.getSpeedX() < 0 ? -1 : 1));
         }
         if (player.getSpeedY() < -FLYING_SPEED) {
            player.setSpeedY(-FLYING_SPEED);
         }
      } else {
         if (Math.abs(player.getSpeedX()) > WALKING_SPEED) {
            player.setSpeedX(WALKING_SPEED * (player.getSpeedX() < 0 ? -1 : 1));
         }
      }
      if (player.getSpeedY() > FALLING_SPEED) {
         player.setSpeedY(FALLING_SPEED);
      }


      final boolean right = player.isActionRight();
      final boolean left = player.isActionLeft();
      final boolean up = player.isActionUp();
      final boolean down = player.isActionDown();

      if (player.isClimbing()) {
         if (up != down) {
            player.setSpeedY(WALKING_SPEED * (up ? -1 : 1));
         } else {
            player.setSpeedY(0);
         }
         if (left != right) {
            player.setSpeedX(WALKING_SPEED * (left ? -1 : 1));
         } else {
            player.setSpeedX(0);
         }
         updateSpeedOnSpecialTiles();
         return;
      }

      if (player.isFlying()) {
         if (player.getSpeedY() < -FLYING_SPEED) {
            player.setSpeedY(-FLYING_SPEED);
         }
         if (Math.abs(player.getSpeedX()) > FLYING_SPEED) {
            player.setSpeedX(FLYING_SPEED * (player.getSpeedX() < 0 ? -1 : 1));
         }
         updateSpeedOnSpecialTiles();
         return;
      }

      if (player.isJumping() && !jumped) {
         player.setSpeedY(-JUMP_SPEED);
         jumped = true;
         updateSpeedOnSpecialTiles();
         return;
      }

      if (!player.isMoving()
              && (player.isOnGround() || player.isOnTopOfLadder())
              && !player.isOnIce()) {
         player.setSpeedX(0);
         updateSpeedOnSpecialTiles();
         return;
      }
      // dont use acell on rolls or we wont move
      if (player.isOnRollLeft() || player.isOnRollRight()) {
         if (player.isActionLeft() != player.isActionRight()) {
            player.setSpeedX(WALKING_SPEED * (player.isActionLeft() ? -1 : 1));
            updateSpeedOnSpecialTiles();
            return;
         }
      }
      updateSpeedOnSpecialTiles();
   }

   @Override
   protected void updateSpeedOnSpecialTiles() {
      super.updateSpeedOnSpecialTiles();
      if (player.isOnGrass() && Math.abs(player.getSpeedX()) > WALKING_SPEED * .5f) {
         player.setSpeedX(WALKING_SPEED * .5f * (player.isActionLeft() ? -1 : 1));
         return;
      }
   }

   @Override
   protected void updatePos(int delta) {
      if (parent.isStuck() || parent.isTeleporting()) {
         return;
      }
      float x = parent.getPosX();
      float y = parent.getPosY();

      x += parent.getSpeedX() * delta;
      y += parent.getSpeedY() * delta;

      if (x != parent.getPosX() || y != parent.getPosY()) {
         staticCollisionCheckNeeded = true;
      }
      parent.setPosX(x);
      parent.setPosY(y);
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

      checkBreakBlocksCollisions(map, centerX, topY, tilePosY, tilePosX, botY, leftX, centerY, rightX);
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

   protected void checkBreakBlocksCollisions(GameMap map, float centerX, float topY, int tilePosY, int tilePosX, float botY, float leftX, float centerY, float rightX) {
      int breakTilePoxX = -1;
      int breakTilePosY = -1;
      if (player.isActionFire()) {
         if (player.isActionLeft()) {
            if (map.isTileBreakableFromRight(leftX - 3, centerY)) {
               breakTilePoxX = tilePosX - 1;
               breakTilePosY = tilePosY;
            }
         } else if (player.isActionRight()) {
            if (map.isTileBreakableFromLeft(rightX + 3, centerY)) {
               breakTilePoxX = tilePosX + 1;
               breakTilePosY = tilePosY;
            }
         } else if (player.isActionUp()) {
            if (map.isTileBreakableFromBottom(centerX, topY - 3)) {
               breakTilePosY = tilePosY - 1;
               breakTilePoxX = tilePosX;
            }
         } else if (player.isActionDown()) {
            if (map.isTileBreakableFromTop(centerX, botY + 3)) {
               breakTilePosY = tilePosY + 1;
               breakTilePoxX = tilePosX;
            }
         } else if (player.isFacingLeft()) {
            if (map.isTileBreakableFromRight(leftX - 3, centerY)) {
               breakTilePoxX = tilePosX - 1;
               breakTilePosY = tilePosY;
            }
         } else if (!player.isFacingLeft()) {
            if (map.isTileBreakableFromLeft(rightX + 3, centerY)) {
               breakTilePoxX = tilePosX + 1;
               breakTilePosY = tilePosY;
            }
         }
         if (breakingTilePosX != breakTilePoxX || breakingTilePosY != breakTilePosY) {
            switchBreakBlock(breakTilePoxX, breakTilePosY);
         }
      }
   }

   @Override
   protected void checkTileType(GameMap map, float leftX, float botY, float rightX, float topY, float centerX, float centerY) {
      super.checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
      player.setOnDoor(map.isTileDoor(player.getPosX(), player.getPosY()));
      player.setOnItem(map.isTileItem(player.getPosX(), player.getPosY()));

   }

   @Override
   public void updateTimers(int delta) {
      super.updateTimers(delta);
      if (player.isActionFire()) {
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
      if (player.isFlying()) {
         player.setFuel(player.getFuel() - (int) (delta * 0.8f));
      }
      if (player.isOnFillFuel()) {
         player.setFuel(player.getFuel() + (int) (delta * 0.5f));
         player.getEnv().getSoundManager().startFuelFill();
      } else {
         player.getEnv().getSoundManager().stopFuelFill();
      }
      if (player.isOnWasteFuel()) {
         player.setFuel(player.getFuel() - (int) (delta * 0.7f));
         player.getEnv().getSoundManager().startFuelWaste();
      } else {
         player.getEnv().getSoundManager().stopFuelWaste();
      }
   }

   private void switchBreakBlock(int tilePosX, int tilePosY) {
      GameMap map = player.getEnv().getMap();
      int tileId = map.getTileId(tilePosX, tilePosY);
      int timer = Utils.getBreakableTileTimeout(tileId);
      breakingTimer = timer;
      breakingTilePosX = tilePosX;
      breakingTilePosY = tilePosY;
   }

   private void grabItem() {
      final GameMap map = player.getEnv().getMap();
      int x = map.getTilePosX(player.getPosX());
      int y = map.getTilePosY(player.getPosY());
      int tileId = map.getTileId(x, y);
      switch (tileId) {
         case Utils.TILE_ID_GOLD1:
            player.addPoints(Utils.SCORE_GOLD1);
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.getEnv().getSoundManager().playCoin();
            break;
         case Utils.TILE_ID_GOLD2:
            player.addPoints(Utils.SCORE_GOLD2);
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.getEnv().getSoundManager().playCoin();
            break;
         case Utils.TILE_ID_GOLD_STATUE:
            player.addPoints(Utils.SCORE_GOLD3);
            player.setLives(player.getLives() + 1);
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.getEnv().getSoundManager().playCoin();
            player.getEnv().getSoundManager().playNewLife();
            break;
         case Utils.TILE_ID_DOUBLE_FUEL:
            player.setFuel(Player.MAX_FUEL);
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.getEnv().getSoundManager().playGoing();
            break;
         case Utils.TILE_ID_SINGLE_FUEL:
            player.setFuel(Math.min(player.getFuel() + Player.MAX_FUEL / 2, Player.MAX_FUEL));
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.getEnv().getSoundManager().playGoing();
            break;
         case Utils.TILE_ID_DROP:
            player.getEnv().getGameManager().setRemainingdrops(player.getEnv().getGameManager().getRemainingdrops() - 1);
            player.addPoints(Utils.SCORE_DROP);
            player.getEnv().getSoundManager().playDrop();
            if (player.getEnv().getGameManager().getRemainingdrops() == 0) {
               openDoor();
               player.getEnv().getSoundManager().playLevelCleared();
            }
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_EMPTY_DROP);
            break;
         case Utils.TILE_ID_POWERUP_BLUESTAR:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.startPowerUpPause();
            break;
         case Utils.TILE_ID_POWERUP_LIGHT:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, -1);
            player.startPowerUpGod();
            break;
      }
   }

   private void breakTile() {
      GameMap map = player.getEnv().getMap();
      brokenTileId = map.getTileId(breakingTilePosX, breakingTilePosY);
      if (brokenTileId == Utils.TILE_ID_QUICK_BREAK) {
         QuickBreakEntity entity = new QuickBreakEntity(50, 1000);
         entity.setPosX(breakingTilePosX);
         entity.setPosY(breakingTilePosY);
         entity.register(player.getEnv());
      } else if (brokenTileId == Utils.TILE_ID_WOODCRATE) {
         map.setTile(breakingTilePosX, breakingTilePosY, Utils.MAP_Z_NORMAL, -1);
         player.getEnv().getSoundManager().playBoxBreak();
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
         breakableEntity.register(player.getEnv());
      }
      breakingTilePosX = -1;
      breakingTilePosY = -1;
      breakingTimer = -1;
      staticCollisionCheckNeeded = true; // update map collision
   }

   public void setJumped(boolean jumpCount) {
      this.jumped = jumpCount;
   }

   @Override
   protected void checkMovablesCollision(int delta) {
      Rectangle playerRect = new Rectangle(player.getPosX() - player.getWidth() / 2,
                                           player.getPosY() - player.getHeigth() / 2,
                                           player.getWidth(), player.getHeigth());

      for (Entity movable : player.getEnv().getMovablesManager().getMovables()) {
         if (movable.isStuck()) {
            continue;
         }
         Rectangle movableRect = new Rectangle(movable.getPosX() - movable.getWidth() / 2,
                                               movable.getPosY() - movable.getHeigth() / 2,
                                               movable.getWidth(), movable.getHeigth());

         if (playerRect.intersects(movableRect)) {
            onCollision(movable);
         }
      }
   }

   private void onCollision(Entity movable) {
      switch (movable.getEntityId()) {
         case Utils.ENTITY_TYPE_ENNEMY:
            player.die();
            break;
      }
   }

   private void openDoor() {
      player.getEnv().getMap().getMapDrawer().triggerDoor();
   }

   private void endLevel() {
      player.levelCleared();
   }

   private void switchTriggerState() {
      if (triggerTimer > 0) {
         return;
      }
      final GameMap map = player.getEnv().getMap();
      final GameManager gameManager = player.getEnv().getGameManager();
      int x = map.getTilePosX(player.getPosX());
      int y = map.getTilePosY(player.getPosY());
      int triggerId = map.getTileId(x, y);
      switch (triggerId) {
         case Utils.TILE_ID_RED_DOOR_TRIGGER_UP:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_RED_DOOR_TRIGGER_DOWN);
            gameManager.setRedLock(!gameManager.isRedLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
         case Utils.TILE_ID_RED_DOOR_TRIGGER_DOWN:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_RED_DOOR_TRIGGER_UP);
            gameManager.setRedLock(!gameManager.isRedLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
         case Utils.TILE_ID_BLUE_DOOR_TRIGGER_UP:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_BLUE_DOOR_TRIGGER_DOWN);
            gameManager.setBlueLock(!gameManager.isBlueLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
         case Utils.TILE_ID_BLUE_DOOR_TRIGGER_DOWN:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_BLUE_DOOR_TRIGGER_UP);
            gameManager.setBlueLock(!gameManager.isBlueLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
         case Utils.TILE_ID_GREEN_DOOR_TRIGGER_UP:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_GREEN_DOOR_TRIGGER_DOWN);
            gameManager.setGreenLock(!gameManager.isGreenLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
         case Utils.TILE_ID_GREEN_DOOR_TRIGGER_DOWN:
            map.setTile(x, y, Utils.MAP_Z_NORMAL, Utils.TILE_ID_GREEN_DOOR_TRIGGER_UP);
            gameManager.setGreenLock(!gameManager.isGreenLock());
            triggerTimer = TRIGER_TIMER_MAX;
            break;
      }
   }

   @Override
   public void reset() {
      super.reset();
      breakingTilePosX = -1;
      breakingTilePosY = -1;
      breakingTimer = -1;
      jumped = false;
   }

   @Override
   public void climbingChanged() {
   }

   @Override
   public void collidedDown() {
   }

   @Override
   public void collidedLeft() {
      player.setSpeedX(0);
   }

   @Override
   public void collidedRight() {
      player.setSpeedX(0);
   }

   @Override
   public void collidedUp() {
   }

   @Override
   public void facingLeftChanged() {
   }

   @Override
   public void fallingChanged() {
      if (!player.isFalling()) {
         player.setSpeedY(0);
         // avoid drifting
         if (!player.isOnIce()) {
            if (Math.signum(player.getSpeedX()) != Math.signum(player.getAccelX())) {
               player.setSpeedX(0);
            }
         }
      }
   }

   @Override
   public void movingChanged() {
   }

   @Override
   public void stuckChanged() {
      if (player.isStuck()) {
         player.explode();
      }
   }

   @Override
   public void teleportingChanged() {
   }
}
