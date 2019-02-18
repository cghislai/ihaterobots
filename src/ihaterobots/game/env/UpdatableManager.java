/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.env;

import ihaterobots.interfaces.Updatable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author charly
 */
public class UpdatableManager {
   private final List<Updatable> updatables;
   private final List<Updatable> toAddUpdatables;
   private final List<Updatable> toRemoveUpdatables;

   public UpdatableManager() {
      updatables = Collections.synchronizedList(new ArrayList<Updatable>());
      toAddUpdatables = Collections.synchronizedList(new ArrayList<Updatable>());
      toRemoveUpdatables = Collections.synchronizedList(new ArrayList<Updatable>());
   }

   public void update(int delta) {

      synchronized (updatables) {
         synchronized (toRemoveUpdatables) {
            for (Updatable updatable : toRemoveUpdatables) {
               updatables.remove(updatable);
            }
            toRemoveUpdatables.clear();
         }
         for (Updatable updatable : updatables) {
            updatable.update(delta);
         }
         synchronized (toAddUpdatables) {
            for (Updatable updatable : toAddUpdatables) {
               updatables.add(updatable);
            }
            toAddUpdatables.clear();
         }
      }
   }

   public void addUpdatable(Updatable u) {
      toAddUpdatables.add(u);
   }

   public void removeUpdatable(Updatable u) {
      toRemoveUpdatables.add(u);
   }
}
