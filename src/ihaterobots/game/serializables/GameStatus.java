/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.serializables;

import java.io.Serializable;

/**
 *
 * @author charly
 */
public class GameStatus implements Serializable{
   public int score;
   public MapRotation rotation;
   public int level;
   public long gameTime;
   public int lives;

   public long getGameTime() {
      return gameTime;
   }

   public void setGameTime(long gameTime) {
      this.gameTime = gameTime;
   }

   public int getLevel() {
      return level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public MapRotation getRotation() {
      return rotation;
   }

   public void setRotation(MapRotation rotation) {
      this.rotation = rotation;
   }

   public int getScore() {
      return score;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public int getLives() {
      return lives;
   }

   public void setLives(int lives) {
      this.lives = lives;
   }
}
