/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.drawer;

import com.charlyghislain.ihaterobots.entities.Player;
import com.charlyghislain.ihaterobots.game.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author charly
 */
public class PlayerDrawer {
   private Animation walkingAnim;
   private Animation standingAnim;
   private Animation fallingAnim;
   private Animation flyingAnim;
   private Animation climbingAnim;
   private Animation firingUpAnim;
   private Animation firingDownAnim;
   private Animation firingAnim;
   private Animation dyingAnim;
   private Animation explodingAnim;
   private Animation wonAnimation;
   private Animation wonAnimation1;
   private Animation wonAnimation2;
   private Image jumpImage;
   private ParticleSystem particleSystem;
   private ConfigurableEmitter particleEmitter;
   private Player player;

   public PlayerDrawer(Player player) {
      this.player = player;
   }

   public void init() {
      SpriteSheet standingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_STANDING);
      SpriteSheet fallingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_FALLING);
      SpriteSheet flyingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_FLYING);
      SpriteSheet walkingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_WALKING);
      SpriteSheet climbingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_CLIMBING);
      SpriteSheet firingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_FIRING);
      SpriteSheet dyingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_DYING);
      SpriteSheet explodingSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_EXPLODING);
      SpriteSheet wonSheet = player.getEnv().getDrawableManager().getSheet(Utils.SHEET_PLAYER_WON);
      standingAnim = new Animation(standingSheet, 0, 0, 1, 0, true, 1000, false);
      fallingAnim = new Animation(fallingSheet, 0, 0, 1, 0, true, 500, false);
      flyingAnim = new Animation(flyingSheet, 0, 0, 1, 0, true, 100, false);
      walkingAnim = new Animation(walkingSheet, 0, 0, 1, 0, true, 200, false);
      climbingAnim = new Animation(climbingSheet, 0, 0, 1, 0, true, 200, false);
      firingUpAnim = new Animation(firingSheet, 0, 0, 1, 0, true, 50, false);
      firingAnim = new Animation(firingSheet, 2, 0, 3, 0, true, 50, false);
      firingDownAnim = new Animation(firingSheet, 4, 0, 5, 0, true, 50, false);
      dyingAnim = new Animation(dyingSheet, 0, 0, 3, 0, true, 250, false);
      explodingAnim = new Animation(explodingSheet, 0, 0, 3, 0, true, 100, false);
      wonAnimation1 = new Animation();
      wonAnimation1.addFrame(wonSheet.getSubImage(0, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(1, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(0, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(1, 0).getFlippedCopy(true, false), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(0, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(2, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(3, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(3, 0).getFlippedCopy(true, false), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(2, 0).getFlippedCopy(true, false), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(4, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(5, 0), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(5, 0).getFlippedCopy(true, false), 300);
      wonAnimation1.addFrame(wonSheet.getSubImage(4, 0).getFlippedCopy(true, false), 300);
      wonAnimation1.setLooping(true);
      wonAnimation2 = new Animation();
      wonAnimation2.addFrame(wonSheet.getSubImage(6, 0), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(7, 0), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(8, 0), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(9, 0), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(8, 0).getFlippedCopy(true, false), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(9, 0).getFlippedCopy(true, false), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(6, 0).getFlippedCopy(true, false), 150);
      wonAnimation2.addFrame(wonSheet.getSubImage(7, 0).getFlippedCopy(true, false), 150);
      wonAnimation2.setLooping(true);
      dyingAnim.setLooping(false);
      explodingAnim.setLooping(false);
      jumpImage = player.getEnv().getDrawableManager().getImage(Utils.IMAGE_PLAYER_JUMP);
      try {
         particleSystem = ParticleIO.loadConfiguredSystem("assets/particle/jet.xml");
         particleEmitter = (ConfigurableEmitter) particleSystem.getEmitter(0);
         particleSystem.setVisible(true);
      } catch (IOException ex) {
         Logger.getLogger(PlayerDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void update(int delta) {
      if (player.isClimbing()) {
         if (player.isActionUp() == player.isActionDown()) {
            climbingAnim.stop();
         } else {
            climbingAnim.start();
         }
      }
      if (particleEmitter.isEnabled() && !player.isFlying() && !particleEmitter.completed()) {
         particleEmitter.wrapUp();
      }
      if (particleEmitter.completed() && particleEmitter.isEnabled()) {
         particleEmitter.resetState();
         particleEmitter.setEnabled(false);
      }
      if (!particleEmitter.isEnabled() && player.isFlying()) {
         particleEmitter.setEnabled(true);
//         particleEmitter.reset();
      }
      if (particleEmitter.isEnabled()) {
         particleSystem.update(delta);
      }
      particleEmitter.setPosition(player.getPosX() + (player.isFacingLeft() ? 5 : -5),
                                  player.getPosY(), false);
      particleEmitter.angularOffset.setValue((player.isFacingLeft() ? 160 : 200));
      if (getRightAnim() != null) {
         getRightAnim().update(delta);
      }
   }

   public void draw(Graphics g) {
      draw(g, player.getSimulator().getTeleportStatus());
   }

   public void draw(Graphics g, float alpha) {
      Animation correctAnim = getRightAnim();
      Image image = null;

      Color color = new Color(1f, 1f, 1f, alpha);
      if (player.isGodMode()) {
         color = new Color(1f, 0f, 0f, alpha);
      }

      int width = 0;
      int heigth = 0;

      if (correctAnim != null) {
         width = correctAnim.getWidth();
         heigth = correctAnim.getHeight();
      } else {
         image = getRightImage();
         width = image.getWidth();
         heigth = image.getHeight();
      }

      g.pushTransform();
      g.translate(player.getPosX() - width / 2, player.getPosY() - heigth / 2);
      if (player.isFacingLeft()) {
         g.pushTransform();
         g.translate(width, 0);
         g.pushTransform();
         g.scale(-1, 1);
      }

      if (correctAnim != null) {
         g.drawAnimation(correctAnim, 0, 0, color);
      } else {
         g.drawImage(image, 0, 0, color);
      }

      if (player.isFacingLeft()) {
         g.popTransform();
         g.translate(-width, 0);
         g.popTransform();
      }
      g.popTransform();


      if (particleEmitter.isEnabled()) {
         particleSystem.render();
      }


//        g.setColor(Color.red);
//        g.drawRect(player.getPosX() - player.getWidth() / 2,
//                player.getPosY() - player.getHeigth() / 2,
//                player.getWidth(), player.getHeigth());
   }

   private Animation getRightAnim() {
      if (player.isFrozen()) {
         return standingAnim;
      }
      if (player.isDying()) {
         if (player.isExploding()) {
            return explodingAnim;
         }
         return dyingAnim;
      }
      if (player.isClearingLevel()) {
         return wonAnimation;
      }
      if (player.isActionFire()) {
         if (player.isActionLeft() || player.isActionRight()) {
            return firingAnim;
         }
         if (player.isActionUp()) {
            return firingUpAnim;
         }
         if (player.isActionDown()) {
            return firingDownAnim;
         }
         return firingAnim;
      }
      if (player.isClimbing()) {
         return climbingAnim;
      }
      if (player.isFalling()) {
         return fallingAnim;
      }
      if (player.isFlying()) {
         return flyingAnim;
      }
      if (player.isTeleporting() || !player.isMoving()) {
         return standingAnim;
      }

      if (player.isMoving()) {
         return walkingAnim;
      }
      return null;
   }

   private Image getRightImage() {
      if (player.isJumping()) {
         return jumpImage;
      }
      return null;
   }

   public void startDyingANim() {
      if (player.isExploding()) {
         explodingAnim.restart();
         return;
      }
      dyingAnim.restart();
   }

   public void chooseWonAnim() {
      if (Math.random() < .5f) {
         wonAnimation = wonAnimation1;
      } else {
         wonAnimation = wonAnimation2;
      }
   }
}
