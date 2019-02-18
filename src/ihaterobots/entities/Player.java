/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities;

import ihaterobots.entities.drawer.PlayerHudDrawer;
import ihaterobots.entities.drawer.PlayerDrawer;
import ihaterobots.entities.simulator.PlayerSimulator;
import ihaterobots.entities.controller.Controller.Action;
import ihaterobots.entities.controller.KeyboardController;
import ihaterobots.game.InGameState;
import ihaterobots.game.env.GameEnv;
import ihaterobots.game.Utils;
import ihaterobots.interfaces.Controllable;
import ihaterobots.interfaces.Drawable;
import ihaterobots.interfaces.Entity;
import ihaterobots.interfaces.Updatable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author charly
 */
public final class Player extends BaseEntity implements Controllable, Updatable, Drawable, Entity {
   public static final int DEAD_TIMER = 8000; //before respawn
   public static final int RESPAWN_TIMER = 5000; // god mode on respawn
   public static final int END_TIMER = 5000; // spend some time after clearing level
   private static final int NEW_LIFE_SCORE = 5000;
   private static final int POWERUP_DURATION = 15000;
   private static final int POWERUP_WARNING = 3000;
   public static final int MAX_FUEL = 12000;
   // action states
   private boolean actionFire;
   // player states
   private boolean jumping;
   private boolean flying;
   private boolean dying;
   private boolean exploding;
   private boolean clearingLevel;
   private boolean godMode;
   private boolean onDoor;
   private boolean onItem;
   private boolean frozen; // used in level entrance to ignore input
   // delegates
   private KeyboardController controller;
   private PlayerDrawer drawer;
   private PlayerHudDrawer hudDrawer;
   private PlayerSimulator simulator;
   // ammo & co
   private int fuel;
   private int lives;
   private int score;
   // timer
   private int deadTimer;
   private int respawnTimer;
   private int endTimer;
   private int powerUpPauseTimer;
   private int powerUpGodTimer;
   private boolean registered;

   public Player() {
      super();
      controller = new KeyboardController(this);

      controller.registerKey(Action.UP, Input.KEY_UP);
      controller.registerKey(Action.DOWN, Input.KEY_DOWN);
      controller.registerKey(Action.LEFT, Input.KEY_LEFT);
      controller.registerKey(Action.RIGHT, Input.KEY_RIGHT);
      controller.registerKey(Action.FIRE, Input.KEY_SPACE);
      controller.registerKey(Action.DELETE, Input.KEY_K);
      drawer = new PlayerDrawer(this);
      hudDrawer = new PlayerHudDrawer(this);
      simulator = new PlayerSimulator(this);
      width = 16;
      heigth = 30;
      renewPlayer();
   }

   public void goToNextLevel() {
      env.getSoundManager().stopLevelEndLoop();
      env.getGameManager().prepareNewMap();
      if (env.getGameManager().nextLevel()) {
         env.getGameManager().loadMap();
         env.getGameManager().startMap();
         env.getGame().enterState(Utils.STATE_ID_LEVEL_ENTRANCE);
      }
   }

   @Override
   public void register(GameEnv env) {
      if (registered) {
         System.out.println("Already registered!");
         return;
      }
      registered = true;
      this.env = env;
      drawer.init();
      env.getDrawableManager().addDrawable(this);
      env.getDrawableManager().addDrawable(hudDrawer);
      env.getUpdatableManager().addUpdatable(this);
      env.getEntitiesManager().addEntity(this);
      resetPlayer();
//      env.getInput().addKeyListener(controller);
   }

   @Override
   public void unregister() {
      registered = false;
      env.getDrawableManager().removeDrawable(this);
      env.getDrawableManager().removeDrawable(hudDrawer);
      env.getUpdatableManager().removeUpdatable(this);
      env.getEntitiesManager().removeEntity(this);
//      env.getInput().removeKeyListener(controller);
   }

   @Override
   public short getDrawablePriority() {
      return Utils.DRAWABLE_PRIORITY_NORMAL;
   }

   @Override
   public void draw(Graphics g) {
      drawer.draw(g);
   }

   public void draw(Graphics g, float alpha) {
      drawer.draw(g, alpha);
   }

   @Override
   public void update(int delta) {
      drawer.update(delta);
      hudDrawer.update(delta);
      if (frozen) {
         return;
      }
      if (clearingLevel) {
         updateClearingLevel(delta);
         return;
      }
      if (deadTimer > 0) {
         deadTimer -= delta;
         if (deadTimer <= 0 && lives >= 0) {
            resetPlayer();
            env.getGameManager().resetPlayerPos();
            env.getSoundManager().resumeMusic();
            respawnTimer = RESPAWN_TIMER;
            godMode = true;
            return;
         }
      }
      if (respawnTimer > 0) {
         respawnTimer -= delta;
         if (respawnTimer <= 0) {
            godMode = false;
         }
      }
      if (powerUpPauseTimer > 0) {
         powerUpPauseTimer -= delta;
         if (powerUpPauseTimer <= POWERUP_WARNING) {
            env.getSoundManager().startPowerUpEndLoop();
         }
         if (powerUpPauseTimer <= 0) {
            env.getGameManager().setEnnemiesPaused(false);
            endPowerUps();
         }
      }
      if (powerUpGodTimer > 0) {
         powerUpGodTimer -= delta;

         if (powerUpGodTimer <= POWERUP_WARNING) {
            env.getSoundManager().startPowerUpEndLoop();
         }
         if (powerUpGodTimer <= 0) {
            godMode = false;
            endPowerUps();
         }
      }

      if (onGround && moving) {
         env.getSoundManager().startSteps();
      } else {
         env.getSoundManager().stopSteps();
      }
      if (climbing && (actionUp != actionDown)) {
         env.getSoundManager().startLadder();
      } else {
         env.getSoundManager().stopLadder();
      }
      simulator.update(delta);
      env.getGameManager().updatePlayTimer(delta);
   }

   protected void endPowerUps() {
      if ((powerUpGodTimer > POWERUP_WARNING || powerUpGodTimer <= 0)
              && (powerUpPauseTimer > POWERUP_WARNING || powerUpPauseTimer <= 0)) {
         env.getSoundManager().stopPowerupEndLoop();
      }
      if (powerUpGodTimer <= 0 && powerUpPauseTimer <= 0) {
         env.getSoundManager().stopPowerupLoop();
         env.getSoundManager().resumeMusic();
      }
   }

   public void startPowerUpPause() {
      powerUpPauseTimer = POWERUP_DURATION;
      env.getGameManager().setEnnemiesPaused(true);
      env.getSoundManager().pauseMusic();
      env.getSoundManager().startPOwerupLoop();
   }

   public void startPowerUpGod() {
      powerUpGodTimer = POWERUP_DURATION;
      godMode = true;
      env.getSoundManager().pauseMusic();
      env.getSoundManager().startPOwerupLoop();
   }

   private void updateClearingLevel(int delta) {
      if (fuel > 0) {
         int fuelToRemove = (int) Math.min(fuel, delta * 1.5f);
         addPoints(fuelToRemove / 4);
         fuel -= fuelToRemove;
         return;
      }
      if (endTimer > 0) {
         endTimer -= delta;
         if (endTimer <= 0) {
            goToNextLevel();
         }
      }
   }

   @Override
   public void processAction(Action action, boolean started) {
      if (dying || teleporting || stuck || frozen) {
         if (started) {
            return;
         }
      }
      super.processAction(action, started);
      if (action == Action.UP && !started) {
         simulator.setJumped(false);
      }
      if (action == Action.UP && started && actionFire) {
         simulator.setJumped(true); // dont jump when firing or after
      }
      if (action == Action.FIRE) {
         actionFire = started;
         if (started) {
            env.getSoundManager().startFiring();
         } else {
            env.getSoundManager().stopFiring();
         }
      }
      if (action == Action.DELETE && started) {
         explode();
      }
   }

   public void newSong(String song) {
      hudDrawer.newSong(song);

   }

   public void explode() {
      if (godMode || dying || teleporting) {
         return;
      }
      exploding = true;
      env.getSoundManager().playExplode();
      drawer.startDyingANim();
      dieCommon();
   }

   public void die() {
      if (godMode || dying || teleporting) {
         return;
      }
      env.getSoundManager().playDie();
      drawer.startDyingANim();
      dieCommon();
   }

   private void dieCommon() {
      lives--;
      moving = false;
      climbing = false;
      dying = true;
      clearingLevel = false;
      jumping = false;
      flying = false;
      teleporting = false;
      deadTimer = DEAD_TIMER;
      powerUpPauseTimer = -1;
      stopActions();
      env.getGameManager().setEnnemiesPaused(false);
      env.getSoundManager().pauseMusic();
      env.getSoundManager().stopAllLoops();
      env.getSoundManager().playDeathMarch();
      if (lives < 0) {
         env.getGame().enterState(Utils.STATE_ID_GAMEOVER);
      }
   }

   public void levelCleared() {
      InGameState currentState = (InGameState) env.getGame().getCurrentState();
      if (currentState.getFromStateId() == Utils.STATE_ID_MAPEDITOR) {
         env.getGame().enterState(Utils.STATE_ID_MAPEDITOR);
         return;
      }
      clearingLevel = true;
      stopActions();
      drawer.chooseWonAnim();
      endTimer = END_TIMER;
      env.getSoundManager().pauseMusic();
      env.getSoundManager().stopAllLoops();
      env.getSoundManager().startLevelEndLoop();
   }

   public boolean isActionFire() {
      return actionFire;
   }

   public int getFuel() {
      return fuel;
   }

   public void setFuel(int fuel) {
      if (fuel < 0) {
         fuel = 0;
      }
      if (fuel > Player.MAX_FUEL) {
         fuel = Player.MAX_FUEL;
      }
      this.fuel = fuel;
   }

   @Override
   public int getEntityId() {
      return Utils.ENTITY_TYPE_PLAYER;
   }

   public int getLives() {
      return lives;
   }

   public void setLives(int lives) {
      this.lives = lives;
   }

   public int getScore() {
      return score;
   }

   public void addPoints(int points) {
      if (this.score % NEW_LIFE_SCORE + points >= NEW_LIFE_SCORE) {
         lives++;
         env.getSoundManager().playNewLife();
      }
      setScore(score + points);
   }

   public void setScore(int score) {
      this.score = score;
   }

   public void renewPlayer() {
      resetPlayer();
      score = 0;
      lives = 3;
      fuel = 0;
   }

   public void resetPlayer() {
      stopActions();
      moving = false;
      climbing = false;
      dying = false;
      exploding = false;
      clearingLevel = false;
      godMode = false;
      flying = false;
      teleporting = false;
      onDoor = false;
      onItem = false;
      endTimer = -1;
      respawnTimer = -1;
      powerUpPauseTimer = -1;
      deadTimer = -1;
      endTimer = -1;
      simulator.reset();
   }

   public void stopActions() {
      speedX = 0;
      speedY = 0;
      accelX = 0;
      accelY = 0;
      actionDown = false;
      actionFire = false;
      actionLeft = false;
      actionUp = false;
      actionRight = false;
   }

   public boolean isClearingLevel() {
      return clearingLevel;
   }

   public void setClearingLevel(boolean clearingLevel) {
      this.clearingLevel = clearingLevel;
   }

   public boolean isDying() {
      return dying;
   }

   public boolean isExploding() {
      return exploding;
   }

   public void setDying(boolean dying) {
      this.dying = dying;
   }

   public boolean isFlying() {
      return flying;
   }

   public void setFlying(boolean flying) {
      if (this.flying == flying) {
         return;
      }
      this.flying = flying;
      if (flying) {
         env.getSoundManager().startFlying();
      } else {
         env.getSoundManager().stopFlying();

      }
   }

   public boolean isJumping() {
      return jumping;
   }

   public void setJumping(boolean jumping) {
      this.jumping = jumping;
   }

   public boolean isOnDoor() {
      return onDoor;
   }

   public void setOnDoor(boolean onDoor) {
      this.onDoor = onDoor;
   }

   public boolean isOnItem() {
      return onItem;
   }

   public void setOnItem(boolean onItem) {
      this.onItem = onItem;
   }

   public PlayerSimulator getSimulator() {
      return simulator;
   }

   @Override
   public void setClimbing(boolean climbing) {
      super.setClimbing(climbing);
      if (climbing) {
         setFlying(false);
      }
   }

   @Override
   public void setStuck(boolean stuck) {
      if (this.stuck == stuck) {
         return;
      }
      this.stuck = stuck;
      fireStuckChanged();
   }

   public boolean isGodMode() {
      return godMode;
   }

   public KeyboardController getController() {
      return controller;
   }

   public boolean isFrozen() {
      return frozen;
   }

   public void setFrozen(boolean frozen) {
      this.frozen = frozen;
   }

    @Override
    public void doorLockChanged() {
        simulator.checkCollisionNeeded();
    }
}
