/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.drawer;

import com.charlyghislain.ihaterobots.entities.Robot1;
import com.charlyghislain.ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class Robot1Drawer {

    private Image fallingImage;
    private Animation walkingAnimation;
    private Image climbingImage;
    private Robot1 robot1;

    public Robot1Drawer(Robot1 robot1) {
        this.robot1 = robot1;
    }

    public void init() {
        fallingImage = robot1.getEnv().getDrawableManager().getImage(Utils.IMAGE_ROBOT1_FALLING);
        climbingImage = robot1.getEnv().getDrawableManager().getImage(Utils.IMAGE_ROBOT1_CLIMBING);
        SpriteSheet sheet = robot1.getEnv().getDrawableManager().getSheet(Utils.SHEET_ROBOT1_WALK);
        walkingAnimation = new Animation(sheet, 500);
        walkingAnimation.setLooping(true);
    }

    public void update(int delta) {
        if (robot1.getEnv().getGameManager().isEnnemiesPaused()
                || robot1.isTeleporting()
                || robot1.isStuck()) {
            return;
        }
        if (robot1.isMoving()) {
            walkingAnimation.update(delta);
        }
    }

    public void draw(Graphics g) {
        Image image = getRightImage();
        float alpha = robot1.getSimulator().getTeleportStatus();
        Color alphaColor = new Color(1f, 1f, 1f, alpha);
        if (robot1.getEnv().getGameManager().isEnnemiesPaused()) {
            alphaColor = new Color(.7f, .7f, 1f, .9f);
        }
        g.pushTransform();
        g.translate(robot1.getPosX() - robot1.getWidth() / 2, robot1.getPosY() - robot1.getHeigth() / 2);
        if (robot1.isFacingLeft()) {
            g.pushTransform();
            g.translate(robot1.getWidth(), 0);
            g.pushTransform();
            g.scale(-1, 1);
        }
        g.drawImage(image, 0, 0, alphaColor);
        if (robot1.isFacingLeft()) {
            g.popTransform();
            g.translate(-robot1.getWidth(), 0);
            g.popTransform();
        }
        g.popTransform();

    }

    private Image getRightImage() {
        if (robot1.isClimbing()) {
            return climbingImage;
        }
        return walkingAnimation.getCurrentFrame();
    }
}
