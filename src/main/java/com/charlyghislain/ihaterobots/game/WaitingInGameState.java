/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.game.env.GameEnv;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class WaitingInGameState extends BasicGameState {
   private final static String STRING = "Press any key to begin";
   private final int SCALE_TIMER_MAX = 3000;
   private final int BLINK_TIMER = 500;
   private GameEnv env;
   private int timer; //timer for scale anim & blinking;
   private boolean isVisible; //for blinking
   private boolean isBlinking; //for blinking
   private float scaleFactor;
   private float posx;
   private float posy;
   private UnicodeFont font;
   private float alphaFactor;

   public WaitingInGameState(GameEnv env) {
      this.env = env;
   }

   @Override
   public void init(GameContainer container, StateBasedGame game) throws SlickException {
      try {
         font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
         font.addAsciiGlyphs();
         font.addGlyphs(400, 600);
         font.loadGlyphs();
      } catch (SlickException ex) {
         Logger.getLogger(LevelEntranceState.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   @Override
   public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      env.getDrawableManager().draw(g);

      if (!isBlinking) {
         float scale = 1 + scaleFactor * container.getHeight() / env.getPlayer().getHeigth(); //from seomthin to 1
         g.pushTransform();
         g.translate(posx, posy);
         g.pushTransform();
         g.scale(scale, scale);
         g.pushTransform();
         g.translate(-env.getPlayer().getPosX(), -env.getPlayer().getPosY()); //to fool the player drawer and make it draw where we want
         env.getPlayer().draw(g, alphaFactor);
         g.popTransform();
         g.popTransform();
         g.popTransform();
      } else {
         if (isVisible) {
            env.getPlayer().draw(g);
            int width = font.getWidth(STRING);
            int height = font.getHeight(STRING);
            g.setColor(Color.red);
            g.drawString(STRING, container.getWidth() / 2 - width / 2, container.getHeight() / 2 - height / 2);
         }
      }
   }

   @Override
   public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
//      env.getUpdatableManager().update(delta);

      if (timer > 0) {
         timer -= delta;
      }
      if (timer <= 0) {
         timer = BLINK_TIMER;
         isVisible = !isVisible;
         isBlinking = true;
         return;
      }
      if (!isBlinking) {
         scaleFactor = ((float) timer / (float) SCALE_TIMER_MAX); //from 1 to 0
         float dx = env.getPlayer().getPosX() - container.getWidth() / 2; // negative if player left from center
         float dy = env.getPlayer().getPosY() - container.getHeight() / 2; //negative if player above center
         posx = env.getPlayer().getPosX() - scaleFactor * dx; // from center to player posx
         posy = env.getPlayer().getPosY() - scaleFactor * dy; // from center to player posx
         alphaFactor = 1f - .8f * scaleFactor;
      }
   }

   @Override
   public void keyPressed(int key, char c) {
      goInGame(key);
   }

   @Override
   public void enter(GameContainer container, StateBasedGame game) throws SlickException {
      timer = SCALE_TIMER_MAX;
      isVisible = true;
      isBlinking = false;
      env.getSoundManager().playIntro();
   }

   @Override
   public void leave(GameContainer container, StateBasedGame game) throws SlickException {
      super.leave(container, game);
      env.getSoundManager().stopIntro();
      env.getSoundManager().resumeMusic();
   }

   private void goInGame(int key) {
//      env.getPlayer().register(env);
      env.getPlayer().setFrozen(false);
      env.getPlayer().getController().keyPressed(key, ' ');
      env.getGame().enterState(Utils.STATE_ID_INGAME);
      // pass keypress event to ingmae state
   }

   @Override
   public int getID() {
      return Utils.STATE_ID_WAITING_INGAME;
   }
}
