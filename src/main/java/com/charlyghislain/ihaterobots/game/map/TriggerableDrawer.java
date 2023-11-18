/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import org.newdawn.slick.Graphics;

/**
 *
 * @author cghislai
 */
public class TriggerableDrawer extends StaticTileDrawer {

    int tileId;

    public TriggerableDrawer(String filename, int tileId) {
        super(filename);
        this.tileId = tileId;
    }

    @Override
    public void draw(Graphics g, int posx, int posy, float alpha) {
        if (env.getGame().getCurrentState().getID() != Utils.STATE_ID_MAPEDITOR) {
            // if not in map edit, draw doors only if needed
            switch (tileId) {
                case Utils.TILE_ID_RED_DOOR_HOR:
                case Utils.TILE_ID_RED_DOOR_VERT:
                    if (!env.getGameManager().isRedLock()) {
                        return;
                    }
                    break;
                case Utils.TILE_ID_BLUE_DOOR_HOR:
                case Utils.TILE_ID_BLUE_DOOR_VERT:
                    if (!env.getGameManager().isBlueLock()) {
                        return;
                    }
                    break;
                case Utils.TILE_ID_GREEN_DOOR_HOR:
                case Utils.TILE_ID_GREEN_DOOR_VERT:
                    if (!env.getGameManager().isGreenLock()) {
                        return;
                    }
                    break;
            }
           super.draw(g, posx, posy, 1f);
           return;
        } 
        super.draw(g, posx, posy, .5f);
    }
}
