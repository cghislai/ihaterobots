/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.env;

import ihaterobots.game.map.GameMap;
import ihaterobots.entities.Player;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class GameEnv {
   private final GameMap map;
   private final GameManager gameManager;
   private final StateBasedGame game;
   private final DrawableManager drawableManager;
   private final UpdatableManager updatableManager;
   private final MovablesManager movablesManager;
   private final SoundManager soundManager;
   private final EntitiesManager entitiesManager;
   private final Player player;

   public GameEnv(StateBasedGame game) {
      this.game = game;
      drawableManager = new DrawableManager();
      updatableManager = new UpdatableManager();
      movablesManager = new MovablesManager();
      soundManager = new SoundManager();
      entitiesManager = new EntitiesManager();
      map = new GameMap(25, 18);
      gameManager = new GameManager();
      player = new Player();
   }

   public void init() {
      drawableManager.init(this);
      map.init(this);
      soundManager.init(this);
      gameManager.init(this);
   }

   public GameMap getMap() {
      return map;
   }

   public DrawableManager getDrawableManager() {
      return drawableManager;
   }

   public UpdatableManager getUpdatableManager() {
      return updatableManager;
   }

   public StateBasedGame getGame() {
      return game;
   }

   public Input getInput() {
      return game.getContainer().getInput();
   }

   public MovablesManager getMovablesManager() {
      return movablesManager;
   }

   public Player getPlayer() {
      return player;
   }

   public SoundManager getSoundManager() {
      return soundManager;
   }

   public EntitiesManager getEntitiesManager() {
      return entitiesManager;
   }

   public GameManager getGameManager() {
      return gameManager;
   }
}
