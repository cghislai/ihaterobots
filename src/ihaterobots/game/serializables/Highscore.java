/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.serializables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charly
 */
public class Highscore implements Serializable {
   public List<String> names;
   public List<Long> times;
   public List<Integer> scores;
   public List<Integer> levels;

   public Highscore() {
      names = new ArrayList<String>();
      times = new ArrayList<Long>();
      scores = new ArrayList<Integer>();
      levels = new ArrayList<Integer>();
   }

   public List<Integer> getLevels() {
      return levels;
   }

   public void setLevels(List<Integer> levels) {
      this.levels = levels;
   }

   public List<String> getNames() {
      return names;
   }

   public void setNames(List<String> names) {
      this.names = names;
   }

   public List<Integer> getScores() {
      return scores;
   }

   public void setScores(List<Integer> scores) {
      this.scores = scores;
   }

   public List<Long> getTimes() {
      return times;
   }

   public void setTimes(List<Long> times) {
      this.times = times;
   }
}
