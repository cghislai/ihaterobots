/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.Rocket;
import ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class RocketDrawer {

//    private Image fallingImage;
    private Animation animation;
//    private Image climbingImage;
    private Rocket rocket;

    public RocketDrawer(Rocket rocket) {
        this.rocket = rocket;
    }

    public void init() {
        SpriteSheet sheet = rocket.getEnv().getDrawableManager().getSheet(Utils.SHEET_ROCKET);
        animation = new Animation(sheet, 100);
        animation.setLooping(true);
        animation.setPingPong(true);
    }

    public void update(int delta) {
        animation.update(delta);
    }

    public void draw(Graphics g) {
        float alpha = rocket.getSimulator().getTeleportStatus();
        Color alphaColor = new Color(1f, 1f, 1f, alpha);
        if (rocket.getEnv().getGameManager().isEnnemiesPaused()) {
            alphaColor = new Color(.7f, .7f, 1f, .9f);
        }
        if (rocket.isActionDown()) {
            g.pushTransform();
            g.translate(rocket.getPosX() - rocket.getWidth() / 2, rocket.getPosY() - rocket.getHeigth() / 2);
            g.pushTransform();
            g.rotate(rocket.getWidth() / 2, rocket.getHeigth() / 2, 180);
            g.drawAnimation(animation, 0, 0, alphaColor);
            g.popTransform();
            g.popTransform();
        } else if (rocket.isActionLeft()) {
            g.pushTransform();
            g.translate(rocket.getPosX() - rocket.getHeigth() / 2, rocket.getPosY() - rocket.getWidth() / 2);
            g.pushTransform();
            // width/heighth already updated
            g.rotate(rocket.getHeigth() / 2, rocket.getWidth() / 2, -90);
            g.drawAnimation(animation, 0, 0, alphaColor);
            g.popTransform();
            g.popTransform();
        } else if (rocket.isActionRight()) {
            g.pushTransform();
            g.translate(rocket.getPosX() - rocket.getHeigth() / 2, rocket.getPosY() - rocket.getWidth() / 2);
            g.pushTransform();
            g.rotate(rocket.getHeigth() / 2, rocket.getWidth() / 2, 90);
            g.drawAnimation(animation, 0, 0, alphaColor);
            g.popTransform();
            g.popTransform();
        } else {
            g.pushTransform();
            g.translate(rocket.getPosX() - rocket.getWidth() / 2, rocket.getPosY() - rocket.getHeigth() / 2);
            g.drawAnimation(animation, 0, 0, alphaColor);
            g.popTransform();
        }
    }
}
