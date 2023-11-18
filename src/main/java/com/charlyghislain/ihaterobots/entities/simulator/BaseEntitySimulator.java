/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.simulator;

import com.charlyghislain.ihaterobots.entities.BaseEntity;
import com.charlyghislain.ihaterobots.game.map.AutoTriggerDrawer;
import com.charlyghislain.ihaterobots.game.map.TeleportAnimDrawer;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.map.GameMap;

/**
 *
 * @author charly
 */
public abstract class BaseEntitySimulator {

    public static final float WALK_ON_EDGE_CUTOFF = .2f;
    public static final float FALLING_ACCEL = .001f;
    protected BaseEntity parent;
    protected int triggerTimer;
    protected int teleportTimer;
    protected int teleportTimer2; // before being able to go on it again
    protected int[] teleportDest;
    protected AutoTriggerDrawer triggerDrawer;
    protected int TRIGER_TIMER_MAX = 500;
    protected int TELEPORT_TIMER_MAX = 500;
    protected boolean staticCollisionCheckNeeded; // dont check collisions if we didnt move

    public BaseEntitySimulator(BaseEntity baseEntity) {
        this.parent = baseEntity;
        staticCollisionCheckNeeded = true;
    }

    public void update(int delta) {
//        printStates();
        updateStates(delta);
        updateAccel(delta);
        updateSpeed(delta);
        updatePos(delta);
        updateTimers(delta);
        checkStaticCollisions(delta);
        checkMovablesCollision(delta);
    }

    protected boolean checkStuckStatus(GameMap map, float centerX, float centerY) {
        // HERE
        if (map.isTileSolid(centerX, centerY)) {
            if (!parent.isStuck()) {
                parent.setStuck(true);
                parent.setPosX((map.getTilePosX(centerX) + .5f) * Utils.TILE_SIZE);
                parent.setPosY((map.getTilePosY(centerY) + .5f) * Utils.TILE_SIZE);
                staticCollisionCheckNeeded = true;
            }
            return true;
        } else {
            if (parent.isStuck()) {
                parent.setStuck(false);
                staticCollisionCheckNeeded = true;
            }
        }
        return false;
    }

    protected void printStates() {
        System.out.println(" O ====== ENTITY " + getClass().getSimpleName());
        System.out.println(" O ====== FRAME AT " + System.currentTimeMillis());
        System.out.println(" |  ");
        System.out.println(" |  climbing: " + Boolean.toString(parent.isClimbing()));
        System.out.println(" |  falling : " + Boolean.toString(parent.isFalling()));
        System.out.println(" |  ladbelow: " + Boolean.toString(parent.isLadderBelow()));
        System.out.println(" |  onladder: " + Boolean.toString(parent.isOnLadder()));
        System.out.println(" |  onGround: " + Boolean.toString(parent.isOnGround()));
        System.out.println(" |  moving: " + Boolean.toString(parent.isMoving()));
        System.out.println(" |  stuck: " + Boolean.toString(parent.isStuck()));
        System.out.println(" |  down : " + Boolean.toString(parent.isActionDown()));
        System.out.println(" |  up   : " + Boolean.toString(parent.isActionUp()));
        System.out.println(" |  left : " + Boolean.toString(parent.isActionLeft()));
        System.out.println(" |  right: " + Boolean.toString(parent.isActionRight()));
        System.out.println(" O ---------------------------------");
    }

    protected void updateStates(int delta) {
        if (parent.isClimbing()) {
            // drop from ladder
            if (!parent.isOnLadder() && !parent.isLadderBelow()) {
                parent.setClimbing(false);
            }
            if (parent.isOnGround() && parent.isActionDown()) {
                parent.setClimbing(false);
            }
            if (parent.isOnGround() && parent.isOnLadderDown()) {
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
                    parent.setPosY(posY - 1);
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
        // fall if not on ground or ladder
        if (!parent.isOnGround()
                && !parent.isClimbing()
                && !parent.isOnTopOfLadder()
                && !parent.isLadderBelow()) { // dont fall when crossing a ladder
            parent.setFalling(true);
        }
    }

    protected void updateAccel(int delta) {
        parent.setAccelX(0);
        parent.setAccelY(0);
        if (parent.isFalling()) {
            parent.setAccelY(FALLING_ACCEL);
        }
    }

    protected void updateSpeed(int delta) {
        parent.setSpeedX(parent.getSpeedX() + parent.getAccelX() * delta);
        parent.setSpeedY(parent.getSpeedY() + parent.getAccelY() * delta);
        updateSpeedOnSpecialTiles();
    }

    protected void updateSpeedOnSpecialTiles() {
        if (parent.isClimbing() && (parent.isLadderBelowDown() || parent.isOnLadderDown())) {
            parent.setSpeedY(parent.getSpeedY() + Utils.ROLL_SPEED);
        }
        if (parent.isClimbing() && (parent.isLadderBelowUp() || parent.isOnLadderUp())) {
            parent.setSpeedY(parent.getSpeedY() - Utils.ROLL_SPEED);
        }
        if (parent.isOnRollLeft()) {
            parent.setSpeedX(parent.getSpeedX() - Utils.ROLL_SPEED);
        }
        if (parent.isOnRollRight()) {
            parent.setSpeedX(parent.getSpeedX() + Utils.ROLL_SPEED);
        }
    }

    protected void updatePos(int delta) {
        if (parent.isStuck() || parent.isTeleporting()
                || parent.getEnv().getGameManager().isEnnemiesPaused()) {
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
        checkMapCollisionNoPlaceOver(map, centerX, centerY, leftX, rightX, topY, botY, tilePosX, tilePosY);
        checkAutoTrigger(map, tilePosX, tilePosY);
        staticCollisionCheckNeeded = false;
    }


    protected void checkMapCollisionNoPlaceOver(GameMap map, float centerX, float centerY, float leftX, float rightX, float topY, float botY, int tilePosX, int tilePosY) {
        boolean leftbot = map.isTileSolid(leftX + 1, botY - 1);
        boolean lefttop = map.isTileSolid(leftX + 1, topY + 1);
        boolean rightbot = map.isTileSolid(rightX - 1, botY - 1);
        boolean righttop = map.isTileSolid(rightX - 1, topY + 1);
        boolean leftcenter = map.isTileSolid(leftX + 1, centerY);
        boolean rightcenter = map.isTileSolid(rightX - 1, centerY);
        boolean topcenter = map.isTileSolid(centerX, topY + 1);
        boolean botcenter = map.isTileSolid(centerX, botY - 1);

        float xFromLeftCollision = -1;
        float xFromRightCollision = -1;
        float yFromBottomCollision = -1;
        float yFromTopCollision = -1;

        if (parent.getSpeedX() <= 0) {
            // collide left if any left
            if ((leftbot && !botcenter)
                    || (lefttop && !topcenter)
                    || leftcenter) {
                float leftX1 = map.getInTileXFromRight(leftX, centerY);
                leftX1 = Math.max(leftX1, map.getInTileXFromRight(leftX, topY));
                leftX1 = Math.max(leftX1, map.getInTileXFromRight(leftX, botY));
                xFromLeftCollision = leftX1 + parent.getWidth() / 2;
//            if (!leftcenter) { // we will have a up/down collision and we are probably on a placeover edge, so weaken the correction
//               if ((parent.getSpeedY() <= 0 && lefttop)
//                       || parent.getSpeedY() >= 0 && leftbot) {
//                  xFromLeftCollision = leftX1 - (leftX1 - leftX) * .5f + parent.getWidth() / 2;
//               }
//            }
            }
        }
        if (parent.getSpeedX() >= 0) {
            if ((rightbot && !botcenter)
                    || (righttop && !topcenter)
                    || rightcenter) {
                float rightX1 = map.getInTileXFromLeft(rightX, centerY);
                rightX1 = Math.min(rightX1, map.getInTileXFromLeft(rightX, topY));
                rightX1 = Math.min(rightX1, map.getInTileXFromLeft(rightX, botY));
                xFromRightCollision = rightX1 - parent.getWidth() / 2;
//            if (!rightcenter) { // we will have a up/down collision and we are probably on a placeover edge, so weaken the correction
//               if ((parent.getSpeedY() <= 0 && righttop)
//                       || parent.getSpeedY() >= 0 && rightbot) {
//                  xFromRightCollision = rightX1 - (rightX - rightX1) * .5f + parent.getWidth() / 2;
//               }
//            }
            }
        }
        if (parent.getSpeedY() <= 0) {
            if ((lefttop && !leftcenter)
                    || (righttop && !rightcenter)
                    || topcenter) {
                float topY1 = map.getInTileYFromBottom(centerX, topY);
                topY1 = Math.max(topY1, map.getInTileYFromBottom(leftX, topY));
                topY1 = Math.max(topY1, map.getInTileYFromBottom(rightX, topY));
                yFromTopCollision = topY1 + parent.getHeigth() / 2;
//            if (!topcenter) { // we will have a up/down collision and we are probably on a placeover edge, so weaken the correction
//               if ((parent.getSpeedX() <= 0 && lefttop)
//                       || parent.getSpeedX() >= 0 && righttop) {
//                  yFromTopCollision = topY1 - (topY1 - topY) * .5f + parent.getHeigth() / 2;
//               }
//            }
            }
        }
        if (parent.getSpeedY() >= 0) {
            if ((leftbot && !leftcenter)
                    || (rightbot && !rightcenter)
                    || botcenter) {
                float botY1 = map.getInTileYFromTop(centerX, botY);
                botY1 = Math.min(botY1, map.getInTileYFromTop(leftX, botY));
                botY1 = Math.min(botY1, map.getInTileYFromTop(rightX, botY));
                yFromBottomCollision = botY1 - parent.getHeigth() / 2;
//            if (!botcenter) { // we will have a up/down collision and we are probably on a placeover edge, so weaken the correction
//               if ((parent.getSpeedX() <= 0 && leftbot)
//                       || parent.getSpeedX() >= 0 && rightbot) {
//                  yFromBottomCollision = botY1 - (botY - botY1) * .5f + parent.getHeigth() / 2;
//               }
//            }
            }
        }

        if (xFromLeftCollision >= 0) {
            collideLeft(xFromLeftCollision);
        }
        if (xFromRightCollision >= 0) {
            collideRight(xFromRightCollision);
        }
        if (yFromBottomCollision >= 0) {
            collideDown(yFromBottomCollision);
        }
        if (yFromTopCollision >= 0) {
            collideUp(yFromTopCollision);
        }
        checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
    }

 /**
*  Warning! This function needs to be rewritten and is prone to bugs!
*
*
**/ 
    protected void checkMapCollisionOnPlaceovers(GameMap map, float centerX, float centerY, float leftX, float rightX, float topY, float botY, int tilePosX, int tilePosY) {
        boolean leftbot = map.isTileSolid(leftX + 1, botY - 1);
        boolean lefttop = map.isTileSolid(leftX + 1, topY + 1);
        boolean rightbot = map.isTileSolid(rightX - 1, botY - 1);
        boolean righttop = map.isTileSolid(rightX - 1, topY + 1);
        boolean leftcenter = map.isTileSolid(leftX + 1, centerY);
        boolean rightcenter = map.isTileSolid(rightX - 1, centerY);
        boolean topcenter = map.isTileSolid(centerX, topY + 1);
        boolean botcenter = map.isTileSolid(centerX, botY - 1);

        if (leftcenter || (leftbot && lefttop)) {
            centerX = tilePosX * Utils.TILE_SIZE + parent.getWidth() / 2;
            while (map.isTileSolid(centerX, centerY)) {
                centerX += Utils.TILE_SIZE;
            }
            collideLeft(centerX);
            leftX = centerX - parent.getWidth() / 2;
            rightX = centerX + parent.getWidth() / 2;
            leftbot = map.isTileSolid(leftX + 1, botY - 1);
            lefttop = map.isTileSolid(leftX + 1, topY + 1);
            rightbot = map.isTileSolid(rightX - 1, botY - 1);
            righttop = map.isTileSolid(rightX - 1, topY + 1);
            rightcenter = map.isTileSolid(rightX - 1, centerY);
            topcenter = map.isTileSolid(centerX, topY + 1);
            botcenter = map.isTileSolid(centerX, botY - 1);
        }
        if (rightcenter || (righttop && rightbot)) {
            centerX = (tilePosX + 1) * Utils.TILE_SIZE - parent.getWidth() / 2;
            collideRight(centerX);
            leftX = centerX - parent.getWidth() / 2;
            rightX = centerX + parent.getWidth() / 2;
            leftbot = map.isTileSolid(leftX + 1, botY - 1);
            lefttop = map.isTileSolid(leftX + 1, topY + 1);
            rightbot = map.isTileSolid(rightX - 1, botY - 1);
            righttop = map.isTileSolid(rightX - 1, topY + 1);
            rightcenter = map.isTileSolid(rightX - 1, centerY);
            topcenter = map.isTileSolid(centerX, topY + 1);
            botcenter = map.isTileSolid(centerX, botY - 1);
        }
        if (topcenter || (lefttop && righttop)) {
            centerY = tilePosY * Utils.TILE_SIZE + parent.getHeigth() / 2;
            collideUp(centerY);
            botY = centerY + parent.getHeigth() / 2;
            topY = centerY - parent.getHeigth() / 2;
            leftbot = map.isTileSolid(leftX + 1, botY - 1);
            lefttop = map.isTileSolid(leftX + 1, topY + 1);
            rightbot = map.isTileSolid(rightX - 1, botY - 1);
            righttop = map.isTileSolid(rightX - 1, topY + 1);
            rightcenter = map.isTileSolid(rightX - 1, centerY);
            topcenter = map.isTileSolid(centerX, topY + 1);
            botcenter = map.isTileSolid(centerX, botY - 1);
        }
        if (botcenter || (rightbot && leftbot)) {
            centerY = (tilePosY + 1) * Utils.TILE_SIZE - parent.getHeigth() / 2;
            collideDown(centerY);
            botY = centerY + parent.getHeigth() / 2;
            topY = centerY - parent.getHeigth() / 2;
            leftbot = map.isTileSolid(leftX + 1, botY - 1);
            lefttop = map.isTileSolid(leftX + 1, topY + 1);
            rightbot = map.isTileSolid(rightX - 1, botY - 1);
            righttop = map.isTileSolid(rightX - 1, topY + 1);
            rightcenter = map.isTileSolid(rightX - 1, centerY);
            topcenter = map.isTileSolid(centerX, topY + 1);
            botcenter = map.isTileSolid(centerX, botY - 1);
        }
        float speedtot = Math.abs(parent.getSpeedX()) + Math.abs(parent.getSpeedY());
        float relSpeedX = Math.abs(parent.getSpeedX() / speedtot * 1.1f);
        float relSpeedY = Math.abs(parent.getSpeedY() / speedtot * 1.1f);

        //BOTTOM_LEFT CORNER
        if (leftbot && !lefttop && !rightbot && !leftcenter && !botcenter) {
            float dx = map.getInTileXFromRight(leftX, botY) - leftX;
            float dy = botY - map.getInTileYFromTop(leftX, botY);
            float x = leftX + (1 - relSpeedX) * dx + parent.getWidth() / 2;
            float y = botY - (1 - relSpeedY) * dy - parent.getHeigth() / 2;
//            float x = leftX + dx + parent.getWidth() / 2;
            float y2 = botY - dy - parent.getHeigth() / 2;
            if (relSpeedX > 0.01f) {
                collideLeft(x);
                collideDown(y);
            } else {
                collideDown(y2);
            }
//            collideLeft(x);
//            collideDown(y);
        }
        if (lefttop && !leftbot && !righttop && !leftcenter && !topcenter) {
            float dx = map.getInTileXFromRight(leftX, topY) - leftX;
            float dy = map.getInTileYFromTop(leftX, botY) - topY;
            float x = leftX + (1 - relSpeedX) * dx + parent.getWidth() / 2;
            float y = topY + (1 - relSpeedY) * dy + parent.getHeigth() / 2;
            collideLeft(x);
            collideUp(y);
        }
        if (rightbot && !righttop && !leftbot && !rightcenter && !botcenter) {
            float dx = rightX - map.getInTileXFromLeft(rightX, botY);
            float dy = botY - map.getInTileYFromTop(rightX, botY);
//            float x = rightX - (1 - relSpeedX) * dx - parent.getWidth() / 2;
//            float y = botY - (1 - relSpeedY) * dy - parent.getHeigth() / 2;
            float x = rightX - dx - parent.getWidth() / 2;
            float y = botY - dy - parent.getHeigth() / 2;
            if (relSpeedX > 0.01f) {
                collideRight(x);
            } else {
                collideDown(y);
            }
        }
        if (righttop && !rightbot && !lefttop && !rightcenter && !topcenter) {
            float dx = rightX - map.getInTileXFromLeft(rightX, topY);
            float dy = map.getInTileYFromTop(rightX, botY) - topY;
            float x = rightX - (1 - relSpeedX) * dx - parent.getWidth() / 2;
            float y = topY - (1 - relSpeedY) * dy + parent.getHeigth() / 2;
            collideRight(x);
            collideUp(y);
        }
        checkTileType(map, leftX, botY, rightX, topY, centerX, centerY);
    }

    protected void collideLeft(float newX) {
        parent.setPosX(newX);
        parent.fireCollisionLeft();
    }

    protected void collideRight(float newX) {
        parent.setPosX(newX);
        parent.fireCollisionRight();
    }

    protected void collideUp(float newY) {
        parent.setPosY(newY);
        parent.fireCollisionUp();
    }

    protected void collideDown(float newY) {
        parent.setPosY(newY);
        parent.fireCollisionDown();
    }

    protected void checkTileType(GameMap map, float leftX, float botY, float rightX, float topY, float centerX, float centerY) {
        // on ladder if both left/right corners on ladder
        // check on topY +1, botY -1
        parent.setOnLadder((map.isTileLadder(leftX + 1, botY - 1) && map.isTileLadder(rightX - 1, botY - 1))
                || (map.isTileLadder(leftX + 1, topY + 1) && map.isTileLadder(rightX - 1, topY + 1)));
        parent.setOnLadderUp((map.isTileLadderUp(leftX + 1, botY - 1) && map.isTileLadderUp(rightX - 1, botY - 1))
                || (map.isTileLadderUp(leftX + 1, topY + 1) && map.isTileLadderUp(rightX - 1, topY + 1)));
        parent.setOnLadderDown((map.isTileLadderDown(leftX + 1, botY - 1) && map.isTileLadderDown(rightX - 1, botY - 1))
                || (map.isTileLadderDown(leftX + 1, topY + 1) && map.isTileLadderDown(rightX - 1, topY + 1)));
        // on top of ladder, either on below corners on ladder but not center and above ones
        parent.setOnTopOfLadder((map.isTileLadder(leftX + 1, botY + 1) || map.isTileLadder(rightX - 1, botY + 1))
                && (!map.isTileLadder(leftX + 1, botY - 1) && !map.isTileLadder(rightX - 1, botY - 1)));
        parent.setLadderBelow(map.isTileLadder(leftX + 1, botY + 1) && map.isTileLadder(rightX - 1, botY + 1));
        parent.setLadderBelowUp(parent.isLadderBelow() && map.isTileLadderUp(centerX, botY + 1)); // 
        parent.setLadderBelowDown(parent.isLadderBelow() && map.isTileLadderDown(centerX, botY + 1)); // 

        // On ground if any of bottom corner on ground 
        parent.setOnGround(map.isTileSolid(leftX + 1, botY + 1) || map.isTileSolid(rightX - 1, botY + 1));
        parent.setOnRollLeft(parent.isOnGround() && map.isTileRollLeft(leftX + 1, botY + 1) && map.isTileRollLeft(rightX - 1, botY + 1));
        parent.setOnRollRight(parent.isOnGround() && map.isTileRollRight(leftX + 1, botY + 1) && map.isTileRollRight(rightX - 1, botY + 1));
        parent.setOnGrass(parent.isOnGround() && map.isTileGrass(leftX + 1, botY + 1) && map.isTileGrass(rightX - 1, botY + 1));
        parent.setOnIce(parent.isOnGround() && map.isTileIce(leftX + 1, botY + 1) && map.isTileIce(rightX - 1, botY + 1));
        parent.setOnTrigger(map.isTileTrigger(centerX, centerY));
        parent.setOnTeleport(parent.isOnGround() && map.isTileTeleport(leftX + 1, centerY) && map.isTileTeleport(rightX - 1, centerY));
        parent.setOnSpikes(map.isTileSpike(leftX, botY) || map.isTileSpike(rightX, botY)
                || map.isTileSpike(leftX, topY) || map.isTileSpike(rightX, topY));
        parent.setOnFillFuel(map.isTileFillFuel(centerX, centerY));
        parent.setOnWasteFuel(map.isTileWasteFuel(centerX, centerY));
    }

    protected void checkAutoTrigger(GameMap map, int tilePosX, int tilePosY) {
        if (parent.isOnTrigger() && triggerTimer <= 0) {
            int tileId = map.getTileId(tilePosX, tilePosY);
            if (tileId == Utils.TILE_ID_BLUE_DOOR_TRIGGER_AUTO) {
                parent.getEnv().getGameManager().setBlueLock(!parent.getEnv().getGameManager().isBlueLock());
                triggerTimer = TRIGER_TIMER_MAX;
                if (triggerDrawer != null) {
                    triggerDrawer.setIsOn(false);
                }
                triggerDrawer = (AutoTriggerDrawer) map.getMapDrawer().getTileDrawer(Utils.TILE_ID_BLUE_DOOR_TRIGGER_AUTO);
                triggerDrawer.setIsOn(true);
            } else if (tileId == Utils.TILE_ID_RED_DOOR_TRIGGER_AUTO) {
                parent.getEnv().getGameManager().setRedLock(!parent.getEnv().getGameManager().isRedLock());
                triggerTimer = TRIGER_TIMER_MAX;
                if (triggerDrawer != null) {
                    triggerDrawer.setIsOn(false);
                }
                triggerDrawer = (AutoTriggerDrawer) map.getMapDrawer().getTileDrawer(Utils.TILE_ID_RED_DOOR_TRIGGER_AUTO);
                triggerDrawer.setIsOn(true);

            } else if (tileId == Utils.TILE_ID_GREEN_DOOR_TRIGGER_AUTO) {
                parent.getEnv().getGameManager().setGreenLock(!parent.getEnv().getGameManager().isGreenLock());
                triggerTimer = TRIGER_TIMER_MAX;
                if (triggerDrawer != null) {
                    triggerDrawer.setIsOn(false);
                }
                triggerDrawer = (AutoTriggerDrawer) map.getMapDrawer().getTileDrawer(Utils.TILE_ID_GREEN_DOOR_TRIGGER_AUTO);
                triggerDrawer.setIsOn(true);
            }
        }
    }

    protected void checkMovablesCollision(int delta) {
    }

    protected void updateTimers(int delta) {
        if (triggerTimer > 0) {
            triggerTimer -= delta;
            if (triggerTimer <= 0) {
                if (triggerDrawer != null) {
                    triggerDrawer.setIsOn(false);
                    triggerDrawer = null;
                }
            }
        }
        if (teleportTimer > 0) {
            updateTeleport(delta);
        }
        if (teleportTimer2 > 0) {
            teleportTimer2 -= delta;
        }
    }

    protected void updateTeleport(int delta) {
        teleportTimer -= delta;
        if (teleportTimer <= 0) {
            if (teleportDest != null) {
                parent.setPosX((teleportDest[0] + .5f) * Utils.TILE_SIZE);
                parent.setPosY((teleportDest[1] + .5f) * Utils.TILE_SIZE);
                teleportDest = null;
                teleportTimer = TELEPORT_TIMER_MAX;
                return;
            }
            parent.setTeleporting(false);
            staticCollisionCheckNeeded = true;
        }
    }

    protected void teleport() {
        if (teleportTimer2 > 0) {
            return;
        }
        parent.setTeleporting(true);
        GameMap map = parent.getEnv().getMap();
        int x = map.getTilePosX(parent.getPosX());
        int y = map.getTilePosY(parent.getPosY());
        int tileId = map.getTileId(x, y);
        teleportDest = parent.getEnv().getGameManager().findOtherTeleport(x, y, tileId);
//        teleportDest[0] = 
//        teleportDest[1] = map.findOtherTeleport(x, y, tileId)[1];
        tileId = map.getTileId(x, y, Utils.MAP_Z_FRONT); // animation tile id
        TeleportAnimDrawer drawer = (TeleportAnimDrawer) map.getMapDrawer().getTileDrawer(tileId);
        drawer.startAnim();
        parent.setSpeedX(0);
        parent.setSpeedY(0);
        teleportTimer = TELEPORT_TIMER_MAX;
        teleportTimer2 = 3 * TELEPORT_TIMER_MAX;
        parent.getEnv().getSoundManager().playTeleport();
    }

    /**
     * 
     * @return  0 when teleporting, 1 when not, in between while in between
     */
    public float getTeleportStatus() {
        if (!parent.isTeleporting()) {
            return 1;
        }
        float rel = (float) ((float) teleportTimer / TELEPORT_TIMER_MAX);
        if (teleportDest == null) {
            // already telepoted, fading in
            return 1 - rel;
        } else {
            return rel;
        }
    }

    public void reset() {
        staticCollisionCheckNeeded = true;
        teleportDest = null;
        teleportTimer = -1;
        teleportTimer2 = -1;
    }

    public void checkCollisionNeeded() {
        staticCollisionCheckNeeded = true;
    }
}
