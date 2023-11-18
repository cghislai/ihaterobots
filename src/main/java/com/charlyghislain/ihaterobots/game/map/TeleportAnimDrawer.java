/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Updatable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author cghislai
 */
public class TeleportAnimDrawer extends TileDrawer implements Updatable {

    private String color;
    private ParticleSystem part;
    private int timer;
    private final int TIME_MAX = 1500;
    private GameEnv env;

    public TeleportAnimDrawer(String color) {
        this.color = color;
    }

    @Override
    public void init(GameEnv env) throws SlickException {
        this.env = env;
        env.getUpdatableManager().addUpdatable(this);
        try {
            part = ParticleIO.loadConfiguredSystem("assets/particle/" + color + ".xml");
            part.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(TeleportAnimDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draw(Graphics g, int posx, int posy, float alpha) {
        if (part.isVisible()) {
            part.render((posx + .5f) * Utils.TILE_SIZE, (posy + .5F) * Utils.TILE_SIZE);
        }
    }

    public void startAnim() {
        part.setVisible(true);
        timer = TIME_MAX;
        part.getEmitter(0).resetState();
    }

    public void stopAnim() {
        part.setVisible(false);
    }

    @Override
    public void update(int delta) {
        if (timer > 0) {
            part.update(delta);
            timer -= delta;
            if (timer <= 0) {
                stopAnim();
            }
        }
    }
}
