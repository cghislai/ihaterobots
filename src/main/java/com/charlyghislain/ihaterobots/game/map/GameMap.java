/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;

/**
 *
 * @author charly
 */
public final class GameMap {

    public static final float CORNERS_CUTOFF = .33f;
    protected int[][][] mapData; //x,y,z
    protected int width;
    protected int heigth;
    protected int tileSize;
    protected String name;
    protected GameEnv env;
    protected MapDrawer mapDrawer;

    public GameMap(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
        this.tileSize = Utils.TILE_SIZE;
        mapData = new int[width][heigth][6];
        mapDrawer = new MapDrawer();
    }

    public void init(GameEnv env) {
        this.env = env;
        mapDrawer.init(env);
    }

    public int getTilePosX(float posX) {
        int x = (int) Math.floor(posX / tileSize);
        return x;
    }

    public int getTilePosY(float posY) {
        int y = (int) Math.floor(posY / tileSize);
        return y;
    }

    // FOr placeover and collision. 
    public float getInTileXFromLeft(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return (x) * Utils.TILE_SIZE;
        }
        int tileId = getTileId(x, y);
        if (tileId == Utils.TILE_ID_RED_DOOR_VERT
                || tileId == Utils.TILE_ID_BLUE_DOOR_VERT
                || tileId == Utils.TILE_ID_GREEN_DOOR_VERT) {
            return x * Utils.TILE_SIZE + 2;
        }
        int id = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        float relx = (posx - x * Utils.TILE_SIZE) / Utils.TILE_SIZE;
        float rely = (posy - y * Utils.TILE_SIZE) / Utils.TILE_SIZE;

        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_LEFTTOPBOT
                || id == Utils.TILE_ID_PO_TOPLEFT) {
            if (rely < CORNERS_CUTOFF) {
                return (x + CORNERS_CUTOFF - rely) * Utils.TILE_SIZE;
            }
        }
        if (id == Utils.TILE_ID_PO_BOTLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_LEFTTOPBOT
                || id == Utils.TILE_ID_PO_BOTLEFT) {
            if (rely > 1 - CORNERS_CUTOFF) {
                return (x - 1 + CORNERS_CUTOFF + rely) * Utils.TILE_SIZE;
            }
        }
        return (x) * Utils.TILE_SIZE;
    }

    public float getInTileXFromRight(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return (x + 1) * Utils.TILE_SIZE;
        }
        int tileId = getTileId(x, y);
        if (tileId == Utils.TILE_ID_RED_DOOR_VERT
                || tileId == Utils.TILE_ID_BLUE_DOOR_VERT
                || tileId == Utils.TILE_ID_GREEN_DOOR_VERT) {
            return (x + 1) * Utils.TILE_SIZE - 2;
        }
        int id = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        float relx = (posx - x * Utils.TILE_SIZE) / Utils.TILE_SIZE;
        float rely = (posy - y * Utils.TILE_SIZE) / Utils.TILE_SIZE;

        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_TOPRIGHT
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT) {
            if (rely < CORNERS_CUTOFF) {
                return (x + 1 - CORNERS_CUTOFF + rely) * Utils.TILE_SIZE;
            }
        }
        if (id == Utils.TILE_ID_PO_BOTLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_BOTRIGHT
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT) {
            if (rely > 1 - CORNERS_CUTOFF) {
                return (x + 2 - CORNERS_CUTOFF - rely) * Utils.TILE_SIZE;
            }
        }
        return (x + 1) * Utils.TILE_SIZE;
    }

    public float getInTileYFromBottom(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return (y + 1) * Utils.TILE_SIZE;
        }
        int tileId = getTileId(x, y);
        if (tileId == Utils.TILE_ID_RED_DOOR_HOR
                || tileId == Utils.TILE_ID_BLUE_DOOR_HOR
                || tileId == Utils.TILE_ID_GREEN_DOOR_HOR) {
            return (y + 1) * Utils.TILE_SIZE - 2;
        }
        int id = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        float relx = (posx - x * Utils.TILE_SIZE) / Utils.TILE_SIZE;
        float rely = (posy - y * Utils.TILE_SIZE) / Utils.TILE_SIZE;

        if (id == Utils.TILE_ID_PO_BOTLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_LEFTTOPBOT
                || id == Utils.TILE_ID_PO_BOTLEFT) {
            if (relx < CORNERS_CUTOFF) {
                return (y + 1 - CORNERS_CUTOFF + relx) * Utils.TILE_SIZE;
            }
        }
        if (id == Utils.TILE_ID_PO_BOTLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT
                || id == Utils.TILE_ID_PO_BOTRIGHT) {
            if (relx > 1 - CORNERS_CUTOFF) {
                return (y + 2 - CORNERS_CUTOFF - relx) * Utils.TILE_SIZE;
            }
        }
        return (y + 1) * Utils.TILE_SIZE;
    }

    public float getInTileYFromTop(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return (y) * Utils.TILE_SIZE;
        }
        int tileId = getTileId(x, y);
        if (tileId == Utils.TILE_ID_RED_DOOR_HOR
                || tileId == Utils.TILE_ID_BLUE_DOOR_HOR
                || tileId == Utils.TILE_ID_GREEN_DOOR_HOR) {
            return (y) * Utils.TILE_SIZE + 2;
        }
        int id = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        float relx = (posx - x * Utils.TILE_SIZE) / Utils.TILE_SIZE;
        float rely = (posy - y * Utils.TILE_SIZE) / Utils.TILE_SIZE;

        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_LEFTTOPBOT
                || id == Utils.TILE_ID_PO_TOPLEFT) {
            if (relx < CORNERS_CUTOFF) {
                return (y + CORNERS_CUTOFF - relx) * Utils.TILE_SIZE;
            }
        }
        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT
                || id == Utils.TILE_ID_PO_TOPRIGHT) {
            if (relx > 1 - CORNERS_CUTOFF) {
                return (y - 1 + CORNERS_CUTOFF + relx) * tileSize;
            }
        }
        return (y) * Utils.TILE_SIZE;
    }

    public boolean isTileSolid(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return true;
        }
        return isTileSolid(x, y, posx, posy);
    }

    public boolean isTileSolidMapCoords(int x, int y) {
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_ROCK:
            case Utils.TILE_ID_DIRT:
            case Utils.TILE_ID_BRICKS:
            case Utils.TILE_ID_SAND: {
                return true;
            }
            case Utils.TILE_ID_RED_DOOR_HOR:
            case Utils.TILE_ID_RED_DOOR_VERT: {
                return env.getGameManager().isRedLock();
            }
            case Utils.TILE_ID_BLUE_DOOR_HOR:
            case Utils.TILE_ID_BLUE_DOOR_VERT: {
                return env.getGameManager().isBlueLock();
            }
            case Utils.TILE_ID_GREEN_DOOR_HOR:
            case Utils.TILE_ID_GREEN_DOOR_VERT: {
                return env.getGameManager().isGreenLock();
            }
            case Utils.TILE_ID_QUICK_BREAK:
            case Utils.TILE_ID_WOODCRATE: {
                return true;
            }
        }
        return false;
    }

    private boolean isTileSolid(int x, int y, float posx, float posy) {
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_ROCK:
            case Utils.TILE_ID_METAL:
            case Utils.TILE_ID_GOLD:
            case Utils.TILE_ID_DIRT:
            case Utils.TILE_ID_BRICKS:
            case Utils.TILE_ID_SAND: {
                return isInTileSolid(posx, posy, x, y);
            }
            case Utils.TILE_ID_RED_DOOR_HOR:
            case Utils.TILE_ID_RED_DOOR_VERT: {
                if (env.getGameManager().isRedLock()) {
                    float inPosx = posx - x * Utils.TILE_SIZE;
                    if (inPosx < 2 || Utils.TILE_SIZE - inPosx < 2) {
                        return false;
                    }
                    float inPosY = posy - y * Utils.TILE_SIZE;
                    if (inPosY < 2 || Utils.TILE_SIZE - inPosY < 2) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
            case Utils.TILE_ID_BLUE_DOOR_HOR:
            case Utils.TILE_ID_BLUE_DOOR_VERT: {
                if (env.getGameManager().isBlueLock()) {
                    float inPosx = posx - x * Utils.TILE_SIZE;
                    if (inPosx < 2 || Utils.TILE_SIZE - inPosx < 2) {
                        return false;
                    }
                    float inPosY = posy - y * Utils.TILE_SIZE;
                    if (inPosY < 2 || Utils.TILE_SIZE - inPosY < 2) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
            case Utils.TILE_ID_GREEN_DOOR_HOR:
            case Utils.TILE_ID_GREEN_DOOR_VERT: {
                if (env.getGameManager().isGreenLock()) {
                    float inPosx = posx - x * Utils.TILE_SIZE;
                    if (inPosx < 2 || Utils.TILE_SIZE - inPosx < 2) {
                        return false;
                    }
                    float inPosY = posy - y * Utils.TILE_SIZE;
                    if (inPosY < 2 || Utils.TILE_SIZE - inPosY < 2) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
            case Utils.TILE_ID_QUICK_BREAK:
            case Utils.TILE_ID_WOODCRATE: {
                return true;
            }
        }
        return false;
    }

    private boolean isInTileSolid(float posx, float posy, int x, int y) {
        int id = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        if (id == -1) {
            return true;
        }
        float relx = (posx - x * Utils.TILE_SIZE) / Utils.TILE_SIZE;
        float rely = (posy - y * Utils.TILE_SIZE) / Utils.TILE_SIZE;

        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_LEFTTOPBOT
                || id == Utils.TILE_ID_PO_TOPLEFT) {
            if (relx < CORNERS_CUTOFF && rely < CORNERS_CUTOFF
                    && relx + rely < CORNERS_CUTOFF) {
                return false;
            }
        }
        if (id == Utils.TILE_ID_PO_TOPLEFTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT
                || id == Utils.TILE_ID_PO_TOPRIGHT) {
            if (relx > 1 - CORNERS_CUTOFF && rely < CORNERS_CUTOFF
                    && relx - rely > 1 - CORNERS_CUTOFF) {
                return false;
            }
        }
        if (id == Utils.TILE_ID_PO_BOTLEFT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_BOTLEFTRIGHT
                || id == Utils.TILE_ID_PO_LEFTTOPBOT) {
            if (relx < CORNERS_CUTOFF && rely > 1 - CORNERS_CUTOFF
                    && rely - relx > 1 - CORNERS_CUTOFF) {
                return false;
            }
        }
        if (id == Utils.TILE_ID_PO_BOTRIGHT
                || id == Utils.TILE_ID_PO_BALL
                || id == Utils.TILE_ID_PO_RIGHTTOPBOT
                || id == Utils.TILE_ID_PO_BOTLEFTRIGHT) {
            if (relx > 1 - CORNERS_CUTOFF && rely > 1 - CORNERS_CUTOFF
                    && relx + rely > 2 - 2 * CORNERS_CUTOFF) {
                return false;
            }
        }
        return true;
    }

    public boolean isTileIce(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileIceMapCoords(x, y);
    }

    public boolean isTileIceMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (tileId) {
            case Utils.TILE_ID_PO_ICE:
                return true;

        }
        return false;
    }

    public boolean isTileGrass(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileGrassMapCoords(x, y);
    }

    public boolean isTileGrassMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (tileId) {
            case Utils.TILE_ID_PO_GRASS:
                return true;
        }
        return false;
    }

    public boolean isTileRollLeft(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileRollLeftMapCoords(x, y);
    }

    public boolean isTileRollLeftMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (tileId) {
            case Utils.TILE_ID_PO_ROLL_LEFT:
                return true;
        }
        return false;
    }

    public boolean isTileRollRight(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileRollRightMapCoords(x, y);
    }

    public boolean isTileRollRightMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (tileId) {
            case Utils.TILE_ID_PO_ROLL_RIGHT:
                return true;
        }
        return false;
    }

    public boolean isTileSpike(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        if (!isTileSpikeMapCoords(x, y)) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_SPIKES:
                if ((y + 1) * Utils.TILE_SIZE - posy < 5) {
                    return true;
                }
                return false;
            case Utils.TILE_ID_SPIKES_LEFT:
                if (posx - (x) * Utils.TILE_SIZE < 5) {
                    return true;
                }
                return false;
            case Utils.TILE_ID_SPIKES_RIGHT:
                if ((x + 1) * Utils.TILE_SIZE - posx < 5) {
                    return true;
                }
                return false;
            case Utils.TILE_ID_SPIKES_TOP:
                if (posy - (y) * Utils.TILE_SIZE < 5) {
                    return true;
                }
                return false;
        }
        return false;
    }

    public boolean isTileSpikeMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_SPIKES:
            case Utils.TILE_ID_SPIKES_LEFT:
            case Utils.TILE_ID_SPIKES_RIGHT:
            case Utils.TILE_ID_SPIKES_TOP:
                return true;
        }
        return false;
    }

    public boolean isTileTrigger(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTiletriggerMapCoords(x, y);
    }

    public boolean isTiletriggerMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_RED_DOOR_TRIGGER_DOWN:
            case Utils.TILE_ID_RED_DOOR_TRIGGER_UP:
            case Utils.TILE_ID_RED_DOOR_TRIGGER_AUTO:
            case Utils.TILE_ID_BLUE_DOOR_TRIGGER_DOWN:
            case Utils.TILE_ID_BLUE_DOOR_TRIGGER_UP:
            case Utils.TILE_ID_BLUE_DOOR_TRIGGER_AUTO:
            case Utils.TILE_ID_GREEN_DOOR_TRIGGER_DOWN:
            case Utils.TILE_ID_GREEN_DOOR_TRIGGER_UP:
            case Utils.TILE_ID_GREEN_DOOR_TRIGGER_AUTO:
                return true;
        }
        return false;
    }

    public boolean isTileTeleport(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileTeleportMapCoords(x, y);
    }

    public boolean isTileTeleportMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_RED_TELEPORT:
            case Utils.TILE_ID_BLUE_TELEPORT:
            case Utils.TILE_ID_GREEN_TELEPORT:
                return true;
        }
        return false;
    }

    public boolean isTileLadder(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileLadderMapCoords(x, y);
    }

    public boolean isTileLadderMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_LADDER:
            case Utils.TILE_ID_LADDER_UP:
            case Utils.TILE_ID_LADDER_DOWN:
                return true;
        }
        return false;
    }

    public boolean isTileDoor(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileDoorMapCoords(x, y);
    }

    public boolean isTileDoorMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_DOOR:
            case Utils.TILE_ID_DOOR_TOPLEFT:
                return true;
        }
        return false;
    }

    public boolean isTileFillFuel(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileFillFuelMapCoords(x, y);
    }

    public boolean isTileFillFuelMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_FUEL_FILL:
                return true;
        }
        return false;
    }

    public boolean isTileWasteFuel(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileWasteFuelMapCoords(x, y);
    }

    public boolean isTileWasteFuelMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_FUEL_WASTE:
                return true;
        }
        return false;
    }

    public boolean isTileLadderUp(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileLadderUpMapCoords(x, y);
    }

    public boolean isTileLadderUpMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_LADDER_UP:
                return true;
        }
        return false;
    }

    public boolean isTileLadderDown(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileLadderDownMapCoords(x, y);
    }

    public boolean isTileLadderDownMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_LADDER_DOWN:
                return true;
        }
        return false;
    }

    public boolean isTileBreakableFromLeft(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileBreakableFromLeftMapCoords(x, y);
    }

    public boolean isTileBreakableFromLeftMapCoords(int x, int y) {
        if (isTileBreakableMapCoords(x, y)) {
            return isPlaceOverBreakableFromLeft(x, y);
        }
        return false;
    }

    public boolean isPlaceOverBreakableFromLeft(int x, int y) {
        int placeOverId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (placeOverId) {
            case Utils.TILE_ID_PO_ROCK_ALL:
            case Utils.TILE_ID_PO_ROCK_LEFT:
            case Utils.TILE_ID_PO_ROCK_LEFTRIGHT:
            case Utils.TILE_ID_PO_ROCK_TOPLEFT:
            case Utils.TILE_ID_PO_ROCK_BOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTTOP:
            case Utils.TILE_ID_PO_ROCK_NOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_NOTDOWN:
                return false;
        }
        return true;
    }

    public boolean isTileBreakableFromRight(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileBreakableFromRightMapCoords(x, y);
    }

    public boolean isTileBreakableFromRightMapCoords(int x, int y) {
        if (isTileBreakableMapCoords(x, y)) {
            return isPlaceOverBreakableFromRight(x, y);
        }
        return false;
    }

    public boolean isPlaceOverBreakableFromRight(int x, int y) {
        int placeOverId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (placeOverId) {
            case Utils.TILE_ID_PO_ROCK_ALL:
            case Utils.TILE_ID_PO_ROCK_RIGHT:
            case Utils.TILE_ID_PO_ROCK_LEFTRIGHT:
            case Utils.TILE_ID_PO_ROCK_TOPRIGHT:
            case Utils.TILE_ID_PO_ROCK_BOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_NOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTTOP:
            case Utils.TILE_ID_PO_ROCK_NOTDOWN:
                return false;
        }
        return true;
    }

    public boolean isTileBreakableFromTop(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileBreakableFromTopMapCoords(x, y);
    }

    public boolean isTileBreakableFromTopMapCoords(int x, int y) {
        if (isTileBreakableMapCoords(x, y)) {
            return isPlaceOverBreakableFromTop(x, y);
        }
        return false;
    }

    public boolean isPlaceOverBreakableFromTop(int x, int y) {
        int placeOverId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (placeOverId) {
            case Utils.TILE_ID_PO_ROCK_ALL:
            case Utils.TILE_ID_PO_ROCK_TOP:
            case Utils.TILE_ID_PO_ROCK_TOPBOT:
            case Utils.TILE_ID_PO_ROCK_TOPRIGHT:
            case Utils.TILE_ID_PO_ROCK_TOPLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_NOTDOWN:
                return false;
        }
        return true;
    }

    public boolean isTileBreakableFromBottom(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileBreakableFromBottomMapCoords(x, y);
    }

    public boolean isTileBreakableFromBottomMapCoords(int x, int y) {
        if (isTileBreakableMapCoords(x, y)) {
            return isPlaceOverBreakableFromBottom(x, y);
        }
        return false;
    }

    public boolean isPlaceOverBreakableFromBottom(int x, int y) {
        int placeOverId = getTileId(x, y, Utils.MAP_Z_PLACEOVER_NORMAL);
        switch (placeOverId) {
            case Utils.TILE_ID_PO_ROCK_ALL:
            case Utils.TILE_ID_PO_ROCK_DOWN:
            case Utils.TILE_ID_PO_ROCK_TOPBOT:
            case Utils.TILE_ID_PO_ROCK_BOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_BOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTLEFT:
            case Utils.TILE_ID_PO_ROCK_NOTRIGHT:
            case Utils.TILE_ID_PO_ROCK_NOTTOP:
                return false;
        }
        return true;
    }

    private boolean isTileBreakableMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_DIRT:
            case Utils.TILE_ID_BRICKS:
            case Utils.TILE_ID_SAND:
            case Utils.TILE_ID_QUICK_BREAK:
            case Utils.TILE_ID_WOODCRATE:
                return true;
        }
        return false;
    }

    public boolean isTileItem(float posx, float posy) {
        int x = getTilePosX(posx);
        int y = getTilePosY(posy);
        return isTileItemMapCoords(x, y);
    }

    public boolean isTileItemMapCoords(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= heigth) {
            return false;
        }
        int tileId = getTileId(x, y);
        switch (tileId) {
            case Utils.TILE_ID_GOLD1:
            case Utils.TILE_ID_GOLD2:
            case Utils.TILE_ID_GOLD_STATUE:
            case Utils.TILE_ID_DOUBLE_FUEL:
            case Utils.TILE_ID_SINGLE_FUEL:
            case Utils.TILE_ID_DROP:
            case Utils.TILE_ID_POWERUP_BLUESTAR:
            case Utils.TILE_ID_POWERUP_LIGHT:
                return true;
        }
        return false;
    }

    public void setTile(int posX, int posY, int posZ, int tileId) {
        mapData[posX][posY][posZ] = tileId;
    }

    public int getTileId(int posX, int posY) {
        try {
            return mapData[posX][posY][Utils.MAP_Z_NORMAL];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public int getTileId(int posX, int posY, int posZ) {
        return mapData[posX][posY][posZ];
    }

    public int getHeigth() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }

    public void setMapData(int[][][] mapData) {
        for (int x = 0; x < mapData.length; x++) {
            for (int y = 0; y < mapData[x].length; y++) {
                System.arraycopy(mapData[x][y], 0, this.mapData[x][y], 0, mapData[x][y].length);
            }
        }
    }

    public int[][][] getMapData() {
        return mapData;
    }

    public MapDrawer getMapDrawer() {
        return mapDrawer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
