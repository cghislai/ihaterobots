/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.Digger;
import ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author cghislai
 */
public class DiggerDrawer {
   private Animation rollingAnimation;
   private Animation[] firingAnimations;
   private Digger digger;

   public DiggerDrawer(Digger digger) {
      this.digger = digger;
   }

   public void init() {
      SpriteSheet sheet = digger.getEnv().getDrawableManager().getSheet(Utils.SHEET_DIGGER);
      rollingAnimation = new Animation(sheet, 0, 0, 3, 0, true, 200, false);
      firingAnimations = new Animation[4];
      for (int i = 0; i < 4; i++) {
         Animation firingAnim = new Animation(sheet, i, 1, i, 2, false, 100, false);
         firingAnimations[i] = firingAnim;
      }
   }

   public void update(int delta) {
      if (digger.getEnv().getGameManager().isEnnemiesPaused()
              || digger.isTeleporting()
              || digger.isStuck()) {
         return;
      }
      if (digger.isMoving() && !digger.isActionFire()) {
         rollingAnimation.update(delta);
      }
      Animation anim = getRightFiringANimation();
      if (anim != null) {
         anim.update(delta);
      }
   }

   public void draw(Graphics g) {
      Image image = getRightImage();
      Image firingImage = getRightFiringImage();
      float alpha = digger.getSimulator().getTeleportStatus();
      Color alphaColor = new Color(1f, 1f, 1f, alpha);
      if (digger.getEnv().getGameManager().isEnnemiesPaused()) {
         alphaColor = new Color(.7f, .7f, 1f, .9f);
      }
      float angle = 0;
      if (digger.isActionDown()) {
         angle = 90;
      }
      if (digger.isActionUp()) {
         angle = -90;
      }

      g.pushTransform();
      g.translate(digger.getPosX(), digger.getPosY());
      g.pushTransform();
      if (digger.isFacingLeft() && !digger.isActionDown() && !digger.isActionDown()) {
         g.scale(-1, 1);
      } else {
         g.rotate(0, 0, angle);
      }

      g.drawImage(image, -digger.getWidth() / 2, -digger.getHeigth() / 2, alphaColor);
      if (firingImage != null) {
         g.drawImage(firingImage, -digger.getWidth() / 2, -digger.getHeigth() / 2);
      }

      g.popTransform();
      g.popTransform();

   }

   private Image getRightImage() {
      return rollingAnimation.getCurrentFrame();
   }

   private Animation getRightFiringANimation() {
      if (!digger.isActionFire()) {
         return null;
      }
      int idx = rollingAnimation.getFrame();
      Animation anim = firingAnimations[idx];
      return anim;
   }

   private Image getRightFiringImage() {
      Animation anim = getRightFiringANimation();
      if (anim == null) {
         return null;
      }
      return anim.getCurrentFrame();
   }
}
