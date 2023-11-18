/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.drawer;

import com.charlyghislain.ihaterobots.entities.LadderPimp;
import com.charlyghislain.ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author cghislai
 */
public class LadderPimpDrawer {

    private Animation idleAnimation;
    private Image chargingImage;
    private LadderPimp pimp;

    public LadderPimpDrawer(LadderPimp pimp) {
        this.pimp = pimp;
    }

    public void init() {
               if (pimp.getEnv().getGameManager().isEnnemiesPaused()
                || pimp.isTeleporting()
                || pimp.isStuck()) {
            return;
        }

        SpriteSheet spriteSheet = pimp.getEnv().getDrawableManager().getSheet(Utils.SHEET_LADDER_PIMP);
        chargingImage = spriteSheet.getSubImage(3, 0);
        idleAnimation = new Animation(spriteSheet, 0, 0, 2, 0, true, 150, false);
        idleAnimation.setLooping(true);
        idleAnimation.setPingPong(true);
    }

    public void update(int delta) {
        if (!pimp.isCharging()) {
            idleAnimation.update(delta);
        }
    }

    public void draw(Graphics g) {
        g.pushTransform();
        Color alphaColor = new Color(1f, 1f, 1f, 1f);
        if (pimp.getEnv().getGameManager().isEnnemiesPaused()) {
            alphaColor = new Color(.7f, .7f, 1f, .9f);
        }

        g.translate(pimp.getPosX(), pimp.getPosY());
        g.pushTransform();
        float angleDeg = (float) (180 / Math.PI * pimp.getAngle());
        g.rotate(0, 0, 90 - angleDeg);
        g.pushTransform();
        g.translate(-pimp.getWidth() / 2, -pimp.getHeigth() / 2);

        if (pimp.isCharging()) {
            g.drawImage(chargingImage, 0, 0, alphaColor);
        } else {
            g.drawAnimation(idleAnimation, 0, 0, alphaColor);
        }
        g.popTransform(); //translate
        g.popTransform(); // rotate
//        g.drawRect(0, 0, pimp.getWidth(), pimp.getHeigth());
//        g.setColor(Color.red);
//        g.drawLine(0, 0, 10 * (float) Math.cos(pimp.getController().getPlayerAngle()),
//                10 * (float) -Math.sin(pimp.getController().getPlayerAngle()));
//        g.setColor(Color.blue);
//        float dif = (float) (Math.PI / 15);
//        g.drawLine(0, 0, 300 * (float) Math.cos(pimp.getAngle() - dif),
//                200 * (float) -Math.sin(pimp.getAngle() - dif));
//        g.drawLine(0, 0, 300 * (float) Math.cos(pimp.getAngle() + dif),
//                200 * (float) -Math.sin(pimp.getAngle() + dif));

        g.popTransform();
//        g.setColor(Color.yellow);
//        g.drawOval(pimp.getPosX() - 1, pimp.getPosY() - 1, 2, 2);
//        g.drawOval(pimp.getPosX() - 100, pimp.getPosY() - 100, 200, 200);
//        g.setColor(Color.magenta);
//        g.drawOval(pimp.getEnv().getPlayer().getPosX() - 2, pimp.getEnv().getPlayer().getPosY() - 2,
//                4, 4);

    }
}
