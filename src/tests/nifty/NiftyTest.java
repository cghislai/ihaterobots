/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.nifty;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class NiftyTest extends StateBasedGame {

   public NiftyTest() {
      super("Nifty test");
   }
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         AppGameContainer container = new AppGameContainer(new NiftyTest());
         container.setDisplayMode(800, 600, false);
         container.setTargetFrameRate(1000);
         container.setMinimumLogicUpdateInterval(10);
         container.setMinimumLogicUpdateInterval(20);
         container.start();
      } catch (SlickException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void initStatesList(GameContainer container) throws SlickException {
      StateOne stateOne = new StateOne(0);
      StateTwo stateTwo = new StateTwo(1);
      addState(stateOne);
      addState(stateTwo);
   }
}
