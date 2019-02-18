/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.VertJumper;
import ihaterobots.game.Utils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author cghislai
 */
public class VertJumperDrawer {
   private Animation bounceAnimation;
   private VertJumper vertJumper;

   public VertJumperDrawer(VertJumper vertJumper) {
      this.vertJumper = vertJumper;
   }

   public void init() {
      SpriteSheet sheet = vertJumper.getEnv().getDrawableManager().getSheet(Utils.SHEET_VERTJUMPER);
      bounceAnimation = new Animation(sheet, 100); // 4 ping pong frames = 7
      bounceAnimation.setPingPong(true);
      bounceAnimation.setLooping(false);
   }

   public void update(int delta) {
      if (!bounceAnimation.isStopped()) {
         bounceAnimation.update(delta);
      }
   }

   public void draw(Graphics g) {
      float alpha = vertJumper.getSimulator().getTeleportStatus();
      Color alphaColor = new Color(1f, 1f, 1f, alpha);
      if (vertJumper.getEnv().getGameManager().isEnnemiesPaused()) {
         alphaColor = new Color(.7f, .7f, 1f, .9f);
      }

      g.pushTransform();
      g.translate(vertJumper.getPosX(), vertJumper.getPosY());

      if (!vertJumper.isBouncingUp()) {
         g.pushTransform();
         g.scale(1, -1);
      }
      g.pushTransform();
      g.translate(-vertJumper.getWidth() / 2, -vertJumper.getHeigth() / 2);

      g.drawAnimation(bounceAnimation, 0, 0, alphaColor);
//        g.setColor(Color.red);
//        g.drawRect(0, 0, vertJumper.getWidth(), vertJumper.getHeigth());

      g.popTransform();
      g.popTransform();
      if (!vertJumper.isBouncingUp()) {
         g.popTransform();
      }
   }

   public void startBounce() {
      if (bounceAnimation.isStopped()) {
         bounceAnimation.restart();
      }
   }
}
