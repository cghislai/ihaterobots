/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.entities.controller;

import ihaterobots.entities.controller.Controller.Action;
import ihaterobots.game.map.GameMap;
import ihaterobots.interfaces.Controllable;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 *
 * @author charly
 */
public class KeyboardController extends Controller implements KeyListener {
   private final Map<Integer, Action> keyCodes;

   public KeyboardController(Controllable controllable) {
      super(controllable);
      keyCodes = new HashMap<Integer, Action>();
   }

   public void registerKey(Action action, int keyCode) {
      keyCodes.put(keyCode, action);
   }

   public void clearKeys() {
      keyCodes.clear();
   }

   @Override
   public void keyPressed(int key, char c) {
      Action action = keyCodes.get(key);
      if (action == null) {
         return;
      }
      controllable.processAction(action, true);
   }

   @Override
   public void keyReleased(int key, char c) {
      Action action = keyCodes.get(key);
      if (action == null) {
         return;
      }
      controllable.processAction(action, false);
   }

   @Override
   public void setInput(Input input) {
   }

   @Override
   public boolean isAcceptingInput() {
      return true;
   }

   @Override
   public void inputEnded() {
   }

   @Override
   public void inputStarted() {
   }
}
