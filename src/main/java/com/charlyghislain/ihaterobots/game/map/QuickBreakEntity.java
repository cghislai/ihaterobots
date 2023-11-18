/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.interfaces.Drawable;
import com.charlyghislain.ihaterobots.interfaces.Entity;
import com.charlyghislain.ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author cghislai
 */
public class QuickBreakEntity implements Entity, Updatable, Drawable {
   protected int posX;
   protected int posY;
   private int frameTime;
   private int animTime;
   private int breakTime;
   private int breakTimer;
   private Animation animation;
   private GameEnv env;

   public QuickBreakEntity(int frameTime, int animTime) {
      this.frameTime = frameTime;
      this.animTime = animTime;
      //frames 1 2 3 4 - break - 3 2 1
      breakTime = animTime - 7 * frameTime;
   }

   @Override
   public void register(GameEnv env) {
      this.env = env;
      SpriteSheet sheet = env.getDrawableManager().getSheet(Utils.SHEET_BREAKABLE_QUICKBREAK);
      animation = new Animation(sheet, frameTime);
      animation.setLooping(false);
      animation.setPingPong(true);
      animation.setAutoUpdate(false);
      
      env.getDrawableManager().addDrawable(this);
      env.getUpdatableManager().addUpdatable(this);
      env.getEntitiesManager().addEntity(this);
      env.getMap().setTile(posX, posY, Utils.MAP_Z_NORMAL, -1);
      env.getSoundManager().playQuickBreakOpen();
   }

   @Override
   public void unregister() {
      env.getDrawableManager().removeDrawable(this);
      env.getUpdatableManager().removeUpdatable(this);
      env.getEntitiesManager().removeEntity(this);
      env.getMap().setTile(posX, posY, Utils.MAP_Z_NORMAL, Utils.TILE_ID_QUICK_BREAK);
   }

   @Override
   public void update(int delta) {
      if (animation.getFrame() == animation.getFrameCount() - 1) {
         if (breakTime > 0) {
            breakTime -= delta;
            return;
         }
         float ppx = env.getPlayer().getPosX();
         float ppy = env.getPlayer().getPosY();
         GameMap map = env.getMap();
         if (map.getTilePosX(ppx) == posX 
                 && map.getTilePosY(ppy) == posY) {
            return; // dont reseal on player
         }
         env.getSoundManager().playQuickBreakClose();
      }
      animation.update(delta);
      if (animation.isStopped()) {
         unregister();
      }
   }

   @Override
   public void draw(Graphics g) {
      g.drawAnimation(animation, posX * Utils.TILE_SIZE, posY * Utils.TILE_SIZE);
   }

   @Override
   public int getEntityId() {
      return Utils.ENTITY_TYPE_BACKGROUND;
   }

   @Override
   public float getPosX() {
      return posX;
   }

   @Override
   public float getPosY() {
      return posY;
   }

   @Override
   public int getWidth() {
      return Utils.TILE_SIZE;
   }

   @Override
   public int getHeigth() {
      return Utils.TILE_SIZE;
   }

   @Override
   public short getDrawablePriority() {
      return Utils.DRAWABLE_PRIORITY_BACKGROUND;
   }

   public void setPosX(int posX) {
      this.posX = posX;
   }

   public void setPosY(int posY) {
      this.posY = posY;
   }

   @Override
   public boolean isStuck() {
      return false;
   }

    @Override
    public void doorLockChanged() {
    }
}
