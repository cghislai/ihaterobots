/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;
import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author charly
 */
public class MainMenuController implements ScreenController, Controller {
   private GameEnv env;
   private Nifty nifty;
   private Screen screen;
   private String creditPopupId;
   private String helpPopupId;

   public MainMenuController(GameEnv env) {
      this.env = env;
   }

   public void onPlayButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_PLAY_MENU);
   }

   public void onMapEditorButtonClick() {
      env.getGame().enterState(Utils.STATE_ID_MAPEDITOR);
   }

   public void onOptionsButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_OPTIONS);
   }

   public void onResumeButtonClick() {
      env.getGame().enterState(Utils.STATE_ID_INGAME);
   }
   
   public void onHighSCoresButtonClick() {
      env.getGame().enterState(Utils.STATE_ID_HIGHSCORES);
   }

   public void onExitButtonClick() {
      System.exit(0);
   }

   public void onHelpButtonClick() {
      nifty.showPopup(screen, helpPopupId, null);
   }

   public void onCreditsButtonClick() {
      nifty.showPopup(screen, creditPopupId, null);
   }

   public void onBackToMainClick() {
      nifty.closePopup(nifty.getTopMostPopup().getId());
   }

   @Override
   public void bind(Nifty nifty, Screen screen) {
      this.screen = screen;
      this.nifty = nifty;

      InputStream helpIS = getClass().getClassLoader().getResourceAsStream("assets/ui/help.txt");
      Utils.buildTextPopup(helpIS, "helpPopup", nifty, "onBackToMainClick()");
      Element helpPopUp = nifty.createPopup("helpPopup");
      helpPopupId = helpPopUp.getId();
      InputStream creditsIS = getClass().getClassLoader().getResourceAsStream("assets/ui/credits.txt");
      Utils.buildTextPopup(creditsIS, "creditsPopup", nifty, "onBackToMainClick()");
      Element creditsPopUp = nifty.createPopup("creditsPopup");
      creditPopupId = creditsPopUp.getId();
   }

   @Override
   public void onStartScreen() {
      if (nifty == null) {
         return;
      }
      if (nifty.getTopMostPopup() != null) {
         nifty.closePopup(nifty.getTopMostPopup().getId());
      }
   }

   @Override
   public void onEndScreen() {
   }

   @Override
   public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
   }

   @Override
   public void init(Properties parameter, Attributes controlDefinitionAttributes) {
   }

   @Override
   public void onFocus(boolean getFocus) {
   }

   @Override
   public boolean inputEvent(NiftyInputEvent inputEvent) {
      return false;
   }
}
