/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.charlyghislain.ihaterobots.game.MainMenuState;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.game.serializables.GameOptions;
import org.bushe.swing.event.EventTopicSubscriber;
import org.newdawn.slick.Input;

/**
 *
 * @author charly
 */
public class OptionsMenuController implements ScreenController {
   private final static String WAITING_INPUT_TEXT = "Press a key...";
   private GameEnv env;
   private Nifty nifty;
   private Screen screen;
   private Slider fxSlider;
   private Label fxLabel;
   private Slider musicSlider;
   private Label musicLabel;
   private boolean waitingInput;
   private int waitingInputControl;
   private Button[] controlsButtons; //LEFT/RIGHT/UP/DOWN/FIRE/KILL
   private int[] controlsKeyCodes;
   private MainMenuState state;

   public OptionsMenuController(GameEnv env, MainMenuState state) {
      this.env = env;
      this.state = state;
      controlsButtons = new Button[6];
      controlsKeyCodes = new int[6];
   }

   public void onLeftButtonClick() {
      waitingInput = true;
      waitingInputControl = 0;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onRightButtonClick() {
      waitingInput = true;
      waitingInputControl = 1;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onUpButtonClick() {
      waitingInput = true;
      waitingInputControl = 2;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onDownButtonClick() {
      waitingInput = true;
      waitingInputControl = 3;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onFireButtonClick() {
      waitingInput = true;
      waitingInputControl = 4;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onSuicideButtonClick() {
      waitingInput = true;
      waitingInputControl = 5;
      controlsButtons[waitingInputControl].setText(WAITING_INPUT_TEXT);
      state.stealKeyInput();
   }

   public void onBackButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_MAIN_MENU);
      env.getGameManager().applyOptions();
   }

   public void onSaveButtonClick() {
      GameOptions gameOptions = env.getGameManager().getGameOptions();
      gameOptions.setFxVol(fxSlider.getValue());
      gameOptions.setMusicVol(musicSlider.getValue());
      gameOptions.setKeyLeft(controlsKeyCodes[0]);
      gameOptions.setKeyRight(controlsKeyCodes[1]);
      gameOptions.setKeyUp(controlsKeyCodes[2]);
      gameOptions.setKeyDown(controlsKeyCodes[3]);
      gameOptions.setKeyFire(controlsKeyCodes[4]);
      gameOptions.setKeyDie(controlsKeyCodes[5]);
      env.getGameManager().applyOptions();
      env.getGameManager().saveOptions();
   }

   @Override
   public void bind(Nifty nifty, Screen screen) {
      this.nifty = nifty;
      this.screen = screen;
      fxSlider = screen.findNiftyControl(UIConstants.SLIDER_FX, Slider.class);
      fxLabel = screen.findNiftyControl(UIConstants.LABEL_FX, Label.class);
      musicSlider = screen.findNiftyControl(UIConstants.SLIDER_MUSIC, Slider.class);
      musicLabel = screen.findNiftyControl(UIConstants.LABEL_MUSIC, Label.class);

      fxSlider.setup(0, 1, 1f, .02f, .02f);
      musicSlider.setup(0, 1, .3f, .02f, .02f);

      controlsButtons[0] = screen.findNiftyControl("leftButton", Button.class);
      controlsButtons[1] = screen.findNiftyControl("rightButton", Button.class);
      controlsButtons[2] = screen.findNiftyControl("upButton", Button.class);
      controlsButtons[3] = screen.findNiftyControl("downButton", Button.class);
      controlsButtons[4] = screen.findNiftyControl("fireButton", Button.class);
      controlsButtons[5] = screen.findNiftyControl("suicideButton", Button.class);

      EventTopicSubscriber<SliderChangedEvent> fxSliderSubscriber = new EventTopicSubscriber<SliderChangedEvent>() {
         @Override
         public void onEvent(String string, SliderChangedEvent t) {
            fxSliderValueChanged(t);
         }
      };
      EventTopicSubscriber<SliderChangedEvent> musicSliderSubscriber = new EventTopicSubscriber<SliderChangedEvent>() {
         @Override
         public void onEvent(String string, SliderChangedEvent t) {
            musicSliderValueChanged(t);
         }
      };
      nifty.subscribe(screen, UIConstants.SLIDER_FX, SliderChangedEvent.class, fxSliderSubscriber);
      nifty.subscribe(screen, UIConstants.SLIDER_MUSIC, SliderChangedEvent.class, musicSliderSubscriber);
   }

   @Override
   public void onStartScreen() {
      GameOptions gameOptions = env.getGameManager().getGameOptions();
      fxSlider.setValue(gameOptions.getFxVol());
      musicSlider.setValue(gameOptions.getMusicVol());
      fxLabel.setText(Integer.toString((int) (fxSlider.getValue() * 100)));
      musicLabel.setText(Integer.toString((int) (musicSlider.getValue() * 100)));
      controlsKeyCodes[0] = gameOptions.getKeyLeft();
      controlsKeyCodes[1] = gameOptions.getKeyRight();
      controlsKeyCodes[2] = gameOptions.getKeyUp();
      controlsKeyCodes[3] = gameOptions.getKeyDown();
      controlsKeyCodes[4] = gameOptions.getKeyFire();
      controlsKeyCodes[5] = gameOptions.getKeyDie();
      for (int i = 0; i < 6; i++) {
         controlsButtons[i].setText(Input.getKeyName(controlsKeyCodes[i]));
      }

   }

   @Override
   public void onEndScreen() {
   }

   public void keyPressed(int keyCode) {
      String keyName = Input.getKeyName(keyCode);
      controlsButtons[waitingInputControl].setText(keyName);
      controlsKeyCodes[waitingInputControl] = keyCode;
      waitingInput = false;
      waitingInputControl = -1;
      state.restoreKeyInput();
   }

   public boolean isWaitingInput() {
      return waitingInput;
   }

   private void fxSliderValueChanged(SliderChangedEvent event) {
      fxLabel.setText(Integer.toString((int) (event.getValue() * 100)));
      env.getSoundManager().setFxVolume(event.getValue());
      env.getSoundManager().playDrop();
   }

   private void musicSliderValueChanged(SliderChangedEvent event) {
      musicLabel.setText(Integer.toString((int) (event.getValue() * 100)));
      env.getSoundManager().setMusicVolume(event.getValue());
   }
}
