/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;

import javax.annotation.Nonnull;
import java.io.InputStream;

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
   public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen, @Nonnull Element element, @Nonnull Parameters parameter) {

   }

   @Override
   public void init(@Nonnull Parameters parameter) {

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
   public void onFocus(boolean getFocus) {
   }

   @Override
   public boolean inputEvent(NiftyInputEvent inputEvent) {
      return false;
   }
}
