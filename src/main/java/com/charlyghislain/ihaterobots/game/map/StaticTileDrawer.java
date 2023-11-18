/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map;

import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author charly
 */
public class StaticTileDrawer extends TileDrawer {
   protected String fileName;
   protected GameEnv env;
   protected Image image;

   public StaticTileDrawer(String fileName) {
      this.fileName = fileName;
   }

   public StaticTileDrawer(Image image) {
      this.image = image;
   }

   @Override
   public void init(GameEnv env) throws SlickException {
      this.env = env;
      if (image == null && fileName != null && !fileName.trim().isEmpty()) {
         image = new Image(imagePath + fileName).getSubImage(0, 0, 32, 32);
      }
      if (image == null) {
         System.out.println("FAILED TO INITIALIZE IMAGE!! (" + getClass().getName() + ")");
      }
   }

   @Override
   public void draw(Graphics g, int posx, int posy, float alpha) {
      g.drawImage(image, (posx + .5f) * Utils.TILE_SIZE - image.getWidth() / 2,
                  (posy + .5f) * Utils.TILE_SIZE - image.getHeight() / 2, new Color(1, 1, 1, alpha));
//      g.setDrawMode(Graphics.MODE_NORMAL);
   }
}
