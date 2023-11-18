/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Updatable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class AnimatedTileDrawer extends TileDrawer implements Updatable {
   protected Animation animation;
   protected final String imageName;
   private final boolean repeat;
   private final int duration;
   protected int sizeX;
   protected int sizeY;
   private final int sx;
   private final int sy;
   private final int ex;
   private final int ey;

   public AnimatedTileDrawer(String imageName) {
      this(imageName, false, 100, Utils.TILE_SIZE, Utils.TILE_SIZE);
   }

   public AnimatedTileDrawer(String imageName, boolean repeat, int duration) {
      this(imageName, repeat, duration, Utils.TILE_SIZE, Utils.TILE_SIZE);
   }

   public AnimatedTileDrawer(String imageName, boolean repeat, int duration, int sizeX, int sizeY) {
      this.imageName = imageName;
      this.repeat = repeat;
      this.duration = duration;
      animation = new Animation(false);
      sx = -1;
      sy = -1;
      ex = -1;
      ey = -1;
      this.sizeX = sizeX;
      this.sizeY = sizeY;
   }

   public AnimatedTileDrawer(String imageName, int sx, int sy, int ex, int ey, int duration) {
      this.imageName = imageName;
      this.sx = sx;
      this.sy = sy;
      this.ex = ex;
      this.ey = ey;
      this.duration = duration;
      repeat = true;
      sizeX = Utils.TILE_SIZE;
      sizeY = Utils.TILE_SIZE;
   }

   @Override
   public void init(GameEnv env) throws SlickException {
      if (sx == -1) {
         try {
            SpriteSheet sheet = new SpriteSheet(imagePath + imageName, sizeX, sizeY);
            animation = new Animation(sheet, duration);
            animation.setLooping(repeat);
         } catch (SlickException ex) {
            Logger.getLogger(AnimatedTileDrawer.class.getName()).log(Level.SEVERE, null, ex);
         }
      } else {
         try {
            SpriteSheet sheet = new SpriteSheet(imagePath + imageName, sizeX, sizeY);
            animation = new Animation(sheet, sx, sy, ex, ey, true, duration, true);
            animation.setLooping(repeat);
            sizeX = sheet.getSubImage(0, 0).getWidth();
            sizeX = sheet.getSubImage(0, 0).getHeight();
         } catch (SlickException ex) {
            Logger.getLogger(AnimatedTileDrawer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   @Override
   public void draw(Graphics g, int posx, int posy, float alpha) {
      g.drawAnimation(animation, posx * Utils.TILE_SIZE,
                      posy * Utils.TILE_SIZE, new Color(1, 1, 1, alpha));
   }

   @Override
   public void update(int delta) {
      if (!animation.isStopped()) {
         animation.update(delta);
      }
   }

   public void start() {
      animation.restart();
   }

   public Animation getAnimation() {
      return animation;
   }
}
