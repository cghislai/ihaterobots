/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.env.GameEnv;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author charly
 */
public abstract class TileDrawer {

    protected final String imagePath = "assets/img/tile/";

    public abstract void init(GameEnv env) throws SlickException;

    public void draw(Graphics g, int posx, int posy) {
        draw(g, posx, posy, 1f);
    }

    public abstract void draw(Graphics g, int posx, int posy, float alpha);
}
