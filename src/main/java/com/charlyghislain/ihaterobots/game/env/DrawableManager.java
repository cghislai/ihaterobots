/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.env;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.interfaces.Drawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author charly
 */
public class DrawableManager {
   private final List<Drawable> drawables;
   private final DrawableComparator comparator;
   private final Map<Integer, Image> images;
   private final Map<Integer, Image> tilesImages;
   private final Map<Integer, SpriteSheet> spriteSheets;
   private final Map<Integer, SpriteSheet> tilesSpriteSheets;

   public DrawableManager() {
      drawables = new ArrayList<Drawable>();
      comparator = new DrawableComparator();
      images = new HashMap<Integer, Image>();
      tilesImages = new HashMap<Integer, Image>();
      spriteSheets = new HashMap<Integer, SpriteSheet>();
      tilesSpriteSheets = new HashMap<Integer, SpriteSheet>();
   }

   public void init(GameEnv env) {
      try {
         spriteSheets.put(Utils.SHEET_BOWL, new SpriteSheet("assets/img/ennemy/robot2.png", 30, 30));
         spriteSheets.put(Utils.SHEET_DIGGER, new SpriteSheet("assets/img/ennemy/digger.png", 24, 24));
         spriteSheets.put(Utils.SHEET_LADDER_PIMP, new SpriteSheet("assets/img/ennemy/ladderpimp.png", 24, 24));
         spriteSheets.put(Utils.SHEET_ROBOT1_WALK, new SpriteSheet("assets/img/ennemy/robot1_walking.png", 16, 30));
         spriteSheets.put(Utils.SHEET_ROCKET, new SpriteSheet("assets/img/ennemy/rocket.png", 10, 30));
         spriteSheets.put(Utils.SHEET_VERTJUMPER, new SpriteSheet("assets/img/ennemy/vertjumper.png", 10, 32));
         spriteSheets.put(Utils.SHEET_FLYINGJET, new SpriteSheet("assets/img/ennemy/flyingjet.png", 30, 24));
         spriteSheets.put(Utils.SHEET_PLAYER_CLIMBING, new SpriteSheet("assets/img/player/climbing.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_DYING, new SpriteSheet("assets/img/player/dying.png", 30, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_EXPLODING, new SpriteSheet("assets/img/player/exploding.png", 18, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_FALLING, new SpriteSheet("assets/img/player/falling.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_FIRING, new SpriteSheet("assets/img/player/firing.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_FLYING, new SpriteSheet("assets/img/player/flying.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_STANDING, new SpriteSheet("assets/img/player/standing.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_WALKING, new SpriteSheet("assets/img/player/walking.png", 16, 30));
         spriteSheets.put(Utils.SHEET_PLAYER_WON, new SpriteSheet("assets/img/player/won.png", 18, 30));
         spriteSheets.put(Utils.SHEET_BREAKABLE_DIRT, new SpriteSheet("assets/img/tile/dirt.png", 32, 32));
         spriteSheets.put(Utils.SHEET_BREAKABLE_BRICKS, new SpriteSheet("assets/img/tile/bricks.png", 32, 32));
         spriteSheets.put(Utils.SHEET_BREAKABLE_SAND, new SpriteSheet("assets/img/tile/sand.png", 32, 32));
         spriteSheets.put(Utils.SHEET_BREAKABLE_QUICKBREAK, new SpriteSheet("assets/img/tile/quickbreak.png", 32, 32));

         images.put(Utils.IMAGE_ROBOT1_CLIMBING, new Image("assets/img/ennemy/robot1_climbing.png"));
         images.put(Utils.IMAGE_ROBOT1_FALLING, new Image("assets/img/ennemy/robot1_falling.png"));
         images.put(Utils.IMAGE_PLAYER_JUMP, new Image("assets/img/player/jumping.png"));
         images.put(Utils.IMAGE_ENTERING_LEVEL_BOX, new Image("assets/img/enteringlevelbox.png"));
         images.put(Utils.IMAGE_ENTERING_LEVEL_DOOR, new Image("assets/img/enteringleveldoor.png"));
         images.put(Utils.IMAGE_FIREBALL, new Image("assets/img/ennemy/fireball.png"));

         tilesImages.put(Utils.TILE_ID_ROCK, new Image("assets/img/tile/rock.png"));
         tilesImages.put(Utils.TILE_ID_DIRT, new Image("assets/img/tile/dirt.png").getSubImage(0, 0, 32, 32));
         tilesImages.put(Utils.TILE_ID_SAND, new Image("assets/img/tile/sand.png").getSubImage(0, 0, 32, 32));

      } catch (SlickException ex) {
         Logger.getLogger(DrawableManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void addDrawable(Drawable d) {
      if (drawables.contains(d)) {
         return;
      }
      drawables.add(d);
      Collections.sort(drawables, comparator);
   }

   public void removeDrawable(Drawable d) {
      drawables.remove(d);
   }

   public void draw(Graphics g) {
      Iterator<Drawable> it = drawables.iterator();
      while (it.hasNext()) {
         Drawable d = it.next();
         d.draw(g);
      }
   }

   public Image getImage(int id) {
      return images.get(id);
   }

   public SpriteSheet getSheet(int id) {
      return spriteSheets.get(id);
   }

   private class DrawableComparator implements Comparator<Drawable> {
      @Override
      public int compare(Drawable o1, Drawable o2) {
         return o1.getDrawablePriority() - o2.getDrawablePriority();
      }
   }
}
