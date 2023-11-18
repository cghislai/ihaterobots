/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author cghislai
 */
public class AutoTriggerDrawer extends AnimatedTileDrawer {

    private Image frontImage;
    private GameEnv env;
    private boolean isOn;

    public AutoTriggerDrawer(String imagePath) {
        super(imagePath, 1, 0, 3, 0, 150);
    }

    @Override
    public void init(GameEnv env) throws SlickException {
        super.init(env);
        this.env = env;
        animation.setPingPong(true);
        animation.setLooping(true);
        animation.setAutoUpdate(true);

        frontImage = new Image(imagePath + imageName).getSubImage(0, 0, sizeX, sizeY);
    }

    @Override
    public void draw(Graphics g, int posx, int posy, float alpha) {
        if (isOn) {
            g.drawImage(frontImage, posx * Utils.TILE_SIZE, posy * Utils.TILE_SIZE);
        } else {
            g.drawAnimation(animation, posx * Utils.TILE_SIZE, posy * Utils.TILE_SIZE);
        }
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }
}
