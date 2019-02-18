/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.Robot2;
import ihaterobots.game.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author charly
 */
public class Robot2Drawer {
   private Animation walkingAnimation;
   private Animation flyingAnimation;
   private Animation fallingAnimation;
   private Robot2 robot2;
   private ParticleSystem particleSystem;
   private ConfigurableEmitter particleEmitter;

   public Robot2Drawer(Robot2 robot1) {
      this.robot2 = robot1;
   }

   public void init() {
      SpriteSheet sheet = robot2.getEnv().getDrawableManager().getSheet(Utils.SHEET_FLYINGJET);
      walkingAnimation = new Animation(sheet, 0, 1, 1, 1, true, 500, false);
      walkingAnimation.setLooping(true);
      flyingAnimation = new Animation(sheet, 0, 2, 1, 2, true, 500, false);
      flyingAnimation.setLooping(true);
      fallingAnimation = new Animation(sheet, 0, 0, 1, 0, true, 500, false);
      fallingAnimation.setLooping(true);
      try {
         particleSystem = ParticleIO.loadConfiguredSystem("assets/particle/jet.xml");
         particleEmitter = (ConfigurableEmitter) particleSystem.getEmitter(0);
         particleSystem.setVisible(true);
      } catch (IOException ex) {
         Logger.getLogger(PlayerDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   public void update(int delta) {
      if (robot2.getEnv().getGameManager().isEnnemiesPaused()
              || robot2.isTeleporting()
              || robot2.isStuck()) {
         return;
      }
      getRightAnim().update(delta);
      if (particleEmitter.isEnabled() && !robot2.isFlying() && !particleEmitter.completed()) {
         particleEmitter.wrapUp();
      }
      if (particleEmitter.completed() && particleEmitter.isEnabled()) {
         particleEmitter.resetState();
         particleEmitter.setEnabled(false);
      }
      if (!particleEmitter.isEnabled() && robot2.isFlying()) {
         particleEmitter.setEnabled(true);
//         particleEmitter.reset();
      }
      if (particleEmitter.isEnabled()) {
         particleSystem.update(delta);
      }
      particleEmitter.setPosition(robot2.getPosX() + (robot2.isFacingLeft() ? 8 : -8),
                                  robot2.getPosY() + 2, false);
      particleEmitter.angularOffset.setValue((robot2.isFacingLeft() ? 160 : 200));

   }

   public void draw(Graphics g) {
      Animation correctAnim = getRightAnim();
      float alpha = robot2.getSimulator().getTeleportStatus();
      Color alphaColor = new Color(1f, 1f, 1f, alpha);
      if (robot2.getEnv().getGameManager().isEnnemiesPaused()) {
         alphaColor = new Color(.7f, .7f, 1f, .9f);
      }
      float fuel = robot2.getFuel() / Robot2.MAX_FUEL;
      Color fuelColor = new Color(1 - fuel, fuel, 0f);
      g.pushTransform();
      g.translate(robot2.getPosX() - robot2.getWidth() / 2, robot2.getPosY() - robot2.getHeigth() / 2);
      if (!robot2.isFacingLeft()) {
         g.pushTransform();
         g.translate(robot2.getWidth(), 0);
         g.pushTransform();
         g.scale(-1, 1);
      }
      g.drawAnimation(correctAnim, 0, 0, alphaColor);
      g.setColor(fuelColor);
      g.fillOval(10, 14, 3, 3);
      if (!robot2.isFacingLeft()) {
         g.popTransform();
         g.translate(-robot2.getWidth(), 0);
         g.popTransform();
      }
      g.popTransform();

      if (particleEmitter.isEnabled()) {
         particleSystem.render();
      }

   }

   private Animation getRightAnim() {
      if (robot2.isFlying()) {
         return flyingAnimation;
      }
      if (robot2.isOnGround()) {
         return walkingAnimation;
      }
      return fallingAnimation;
   }
}
