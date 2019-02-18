/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.drawer;

import ihaterobots.entities.Player;
import ihaterobots.game.Utils;
import ihaterobots.interfaces.Drawable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author charly
 */
public class PlayerHudDrawer implements Drawable {
   private final int SONGTIMER = 3000;
   private Image heart;
   private Color fuelColor1;
   private Color fuelColor2;
   private UnicodeFont font;
   private Player player;
   private int newSongTimer;
   private String songName;

   public PlayerHudDrawer(Player player) {
      this.player = player;
      init();
   }

   private void init() {
      try {
         heart = new Image("assets/img/hud/heart.png");
      } catch (SlickException ex) {
         Logger.getLogger(PlayerHudDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }
      fuelColor1 = Color.red;
      fuelColor2 = Color.green;

      try {
         font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
         font.addAsciiGlyphs();
         font.addGlyphs(400, 600);
         font.loadGlyphs();
      } catch (SlickException ex) {
         Logger.getLogger(PlayerHudDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   public void newSong(String songName) {
      this.songName = songName;
      newSongTimer = SONGTIMER;
   }

   public void update(int delta) {
      if (newSongTimer > 0) {
         newSongTimer -= delta;
      }
   }

   @Override
   public void draw(Graphics g) {
      // lives
      int posx = 20;
      int posy = 576;
      for (int i = 0; i < Math.min(3, player.getLives()); i++) {
         g.drawImage(heart, posx, posy);
         posx += 30;
      }
      if (player.getLives() > 3) {
         posx += 10;
         font.drawString(posx, posy, "+" + (player.getLives() - 3));
      }
      //jetbar
      posx = 200;
      float length = ((float) player.getFuel() / (float) Player.MAX_FUEL) * 200;
      Rectangle shape = new Rectangle(posx + 1, posy + 1, length, 22);
      GradientFill fill = new GradientFill(0, 0, fuelColor1, 100, 0, fuelColor2, true);
      g.fill(shape, fill);
      g.setColor(Color.white);
      g.drawRect(posx, posy, 202, 24);


      posx = 420;
      if (newSongTimer > 0) {
         font.drawString(posx, posy, songName);
         return;
      }
      //score + time
      font.drawString(posx, posy, "Score:  " + player.getScore());

      long time = player.getEnv().getGameManager().getLevelTimer();
      posx = 640;
      font.drawString(posx, posy, "Time:  " + Utils.timeToString(time));
   }

   @Override
   public short getDrawablePriority() {
      return Utils.DRAWABLE_PRIORITY_FRONT;
   }
}
