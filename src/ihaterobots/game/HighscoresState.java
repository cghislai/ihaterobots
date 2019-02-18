/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game;

import ihaterobots.game.env.GameEnv;
import ihaterobots.game.serializables.Highscore;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class HighscoresState extends BasicGameState {
   private GameEnv env;
   private UnicodeFont font;
   private String[][] strings;
   private float firstLinePosY;

   public HighscoresState(GameEnv env) {
      this.env = env;
   }

   @Override
   public void init(GameContainer container, StateBasedGame game) throws SlickException {
      font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
      font.addAsciiGlyphs();
      font.addGlyphs(400, 600);
      font.loadGlyphs();
   }

   @Override
   public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      g.setFont(font);
      int frstLinePosYInt = Math.round(firstLinePosY);
      int posY = frstLinePosYInt;
      int firstStringPosY = frstLinePosYInt + 90;
      while (posY < 620) {
         float alpha = 1f;
         if (posY > 500) {
            alpha = 1f - ((float) posY - 500f) / 120f;
         } else if (posY < 100) {
            alpha = (float) posY / 100f;
         }
         Color c = new Color(1f, 1f, 0f, alpha);
         g.setColor(c);

         if (posY == frstLinePosYInt) {
            g.drawString("Hiscores:", 300, posY);
            posY += 60;
            continue;
         } else if (posY == frstLinePosYInt + 60) {
            g.drawString("Player", 100, posY);
            g.drawString("Time", 300, posY);
            g.drawString("Last level", 400, posY);
            g.drawString("Score", 600, posY);
            posY += 30;
            continue;
         } else {
            int stringIdx = Math.round((float) (posY - firstStringPosY) / 22f);
            if (stringIdx >= strings.length) {
               break;
            }
            g.drawLine(100, posY, 700, posY);
            g.drawString(strings[stringIdx][0], 100, posY);
            g.drawString(strings[stringIdx][1], 300, posY);
            g.drawString(strings[stringIdx][2], 400, posY);
            g.drawString(strings[stringIdx][3], 600, posY);
            posY += 22;
         }
      }
   }

   @Override
   public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      firstLinePosY -= delta * .03f;
   }

   @Override
   public void keyPressed(int key, char c) {
      env.getGame().enterState(Utils.STATE_ID_MAINMENU);
   }

   @Override
   public int getID() {
      return Utils.STATE_ID_HIGHSCORES;
   }

   @Override
   public void enter(GameContainer container, StateBasedGame game) throws SlickException {
      Highscore highscore = env.getGameManager().getHighscore();
      strings = new String[highscore.levels.size()][4];
      for (int i = 0; i < highscore.levels.size(); i++) {
         strings[i][0] = highscore.names.get(i);
         strings[i][1] = Utils.timeToString(highscore.times.get(i));
         strings[i][2] = Integer.toString(highscore.levels.get(i));
         strings[i][3] = Integer.toString(highscore.scores.get(i));
      }
      firstLinePosY = 610;
   }
}