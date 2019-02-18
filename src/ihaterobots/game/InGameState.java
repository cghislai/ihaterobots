/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game;

import ihaterobots.game.env.GameEnv;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class InGameState extends BasicGameState {
   public static final String INGAME_MENU_SCREEN_ID = "INGAME_MENU";
   public static final int SPEED_CHANGE_TIME = 2000;
   private static final float SPEED_STEP = 1.1f;
   private GameEnv env;
   private boolean acceptingInput = false;
   private int fromStateId; //remember id we come from, jump to it on escape
   private boolean paused;
   private int speedChangeTimer = -1;
   private GameContainer container;

   public InGameState(GameEnv env) {
      this.env = env;
   }

   @Override
   public void init(GameContainer container, StateBasedGame game) throws SlickException {
   }

   public void initStuff() {
   }

   @Override
   public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      env.getDrawableManager().draw(g);
      if (speedChangeTimer >= 0) {
         float speed = env.getGameManager().getGameOptions().getSpeed();
         g.drawString("Speed : " + Float.toString(speed) + "X", 700, 0);
      }
   }

   @Override
   public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      if (speedChangeTimer >= 0) {
         speedChangeTimer -= delta;
      }
      float speed = env.getGameManager().getGameOptions().getSpeed();
      env.getUpdatableManager().update(Math.round(delta * speed));
   }

   @Override
   public void enter(GameContainer container, StateBasedGame game) throws SlickException {
      super.enter(container, game);
      // For map test without player pos
      env.getSoundManager().resumeMusic();
      env.getGameManager().setGameRunning(true);
      env.getGameManager().setGamePaused(false);
      this.container = container;
   }

   @Override
   public void leave(GameContainer container, StateBasedGame game) throws SlickException {
      super.leave(container, game);
   }

   public void setFromStateId(int fromStateId) {
      this.fromStateId = fromStateId;
   }

   public int getFromStateId() {
      return fromStateId;
   }

   @Override
   public int getID() {
      return Utils.STATE_ID_INGAME;
   }

   @Override
   public void keyPressed(int key, char c) {
      if (key == Input.KEY_ESCAPE) {
         env.getGameManager().setGamePaused(true);
         env.getGame().enterState(Utils.STATE_ID_MAINMENU);
         return;
      }
      if (key == Input.KEY_H && Utils.EASY_SKIP) {
         env.getPlayer().goToNextLevel();
         return;
      }
      if (key == Input.KEY_F5) {
         changeSpeed(false);

         return;
      }
      if (key == Input.KEY_F6) {
         changeSpeed(true);
         speedChangeTimer = SPEED_CHANGE_TIME;
         return;
      }
      env.getPlayer().getController().keyPressed(key, c);
   }

   private void changeSpeed(boolean faster) {
      float speed = env.getGameManager().getGameOptions().getSpeed();
      if (faster) {
         speed *= SPEED_STEP;
         speed = Math.min(2f, speed);
      } else {
         speed /= SPEED_STEP;
         speed = Math.max(0.1f, speed);
      }
      env.getGameManager().getGameOptions().setSpeed(speed);
      container.setMinimumLogicUpdateInterval(Math.round(10 * speed));
      container.setMaximumLogicUpdateInterval(Math.round(10 * speed));
      env.getSoundManager().startMusic();
      speedChangeTimer = SPEED_CHANGE_TIME;
   }

   @Override
   public void keyReleased(int key, char c) {
      env.getPlayer().getController().keyReleased(key, c);
   }
}
