/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.Bowl;
import ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class BowlDrawer {
   private Animation animation;
   private Bowl bowl;

   public BowlDrawer(Bowl bowl) {
      this.bowl = bowl;
   }

   public void init() {
      SpriteSheet sheet = bowl.getEnv().getDrawableManager().getSheet(Utils.SHEET_BOWL);
      animation = new Animation(sheet, 100);
      animation.setLooping(true);
   }

   public void update(int delta) {
      if (bowl.getEnv().getGameManager().isEnnemiesPaused()
              || bowl.isTeleporting()
              || bowl.isClimbing()
              || bowl.isStuck()) {
         return;
      }
      animation.update(delta);
   }

   public void draw(Graphics g) {
      Image image = getRightImage();
      float alpha = bowl.getSimulator().getTeleportStatus();
      Color alphaColor = new Color(1f, 1f, 1f, alpha);
      if (bowl.getEnv().getGameManager().isEnnemiesPaused()) {
         alphaColor = new Color(.7f, .7f, 1f, .9f);
      }

      g.pushTransform();
      g.translate(bowl.getPosX() - bowl.getWidth() / 2, bowl.getPosY() - bowl.getHeigth() / 2);
      if (bowl.isFacingLeft()) {
         g.pushTransform();
         g.translate(bowl.getWidth(), 0);
         g.pushTransform();
         g.scale(-1, 1);
      }
      g.drawImage(image, 0, 0, alphaColor);
      if (bowl.isFacingLeft()) {
         g.popTransform();
         g.translate(-bowl.getWidth(), 0);
         g.popTransform();
      }
      g.popTransform();

   }

   private Image getRightImage() {
      return animation.getCurrentFrame();
   }
}
