/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.FireBall;
import ihaterobots.game.Utils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author charly
 */
public class FireBallDrawer {
   private final static float rotSpeed = 0.3f;
   private Image image;
   private FireBall fireBall;
   private float angle;

   public FireBallDrawer(FireBall rocket) {
      this.fireBall = rocket;
      angle = 0;
   }

   public void init() {
      image = fireBall.getEnv().getDrawableManager().getImage(Utils.IMAGE_FIREBALL);
   }

   public void update(int delta) {
      if (fireBall.getEnv().getGameManager().isEnnemiesPaused()
              || fireBall.isTeleporting()
              || fireBall.isStuck()) {
         return;
      }
      angle -= rotSpeed * delta;
      angle = angle % 90;
   }

   public void draw(Graphics g) {
      float alpha = fireBall.getSimulator().getTeleportStatus();
      Color alphaColor = new Color(1f, 1f, 1f, alpha);
      if (fireBall.getEnv().getGameManager().isEnnemiesPaused()) {
         alphaColor = new Color(.7f, .7f, 1f, .9f);
      }
      g.pushTransform();
      g.translate(fireBall.getPosX() - fireBall.getWidth() / 2, fireBall.getPosY() - fireBall.getHeigth() / 2);
      g.pushTransform();
      g.rotate(fireBall.getWidth() / 2, fireBall.getHeigth() / 2, angle);
      g.drawImage(image, 0, 0, alphaColor);
      g.popTransform();
      g.popTransform();
   }
}
