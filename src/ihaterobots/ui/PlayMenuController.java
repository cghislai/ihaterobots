/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.game.serializables.GameStatus;
import ihaterobots.game.serializables.MapData;
import ihaterobots.game.serializables.MapRotation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charly
 */
public class PlayMenuController implements ScreenController {
   public static final String OFFICIAL_ROT_NAME = "Official";
   private GameEnv env;
   private Nifty nifty;
   private Screen screen;
   private GameStatus gameStatus;

   public PlayMenuController(GameEnv env) {
      this.env = env;
   }

   public void onContinueButtonClick() {
      env.getGameManager().prepareNewGame();
      if (gameStatus == null) {
         return;
      }
      env.getGameManager().setStatus(gameStatus);
      env.getGameManager().loadMap();
      env.getGameManager().startMap();
      env.getGame().enterState(Utils.STATE_ID_LEVEL_ENTRANCE);
   }

   public void onNewGameButtonClick() {
      env.getGameManager().prepareNewGame();
      DropDown levelSetDropDown = screen.findNiftyControl(UIConstants.DROPDOWN_LEVELSET, DropDown.class);
      String rotationName = (String) levelSetDropDown.getSelection();
      InputStream is = null;
      if (rotationName.equals(OFFICIAL_ROT_NAME)) {
         is = getClass().getClassLoader().getResourceAsStream("assets/map/official.rot");
      } else {
         File file = new File(Utils.SAVE_PATH + rotationName + Utils.MAP_ROTATION_EXT);
         try {
            is = new FileInputStream(file);
         } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayMenuController.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      MapRotation rotation = Utils.loadMapRotation(is);
      env.getGameManager().setMapRotation(rotation);
      env.getGameManager().setCurrentLevel(0);
      env.getGameManager().loadMap();
      env.getGameManager().startMap();
      env.getGame().enterState(Utils.STATE_ID_LEVEL_ENTRANCE);
   }

   private void loadStatus() {
      String fileName = Utils.SAVE_PATH + Utils.GAME_STATUS_FILENAME;
      Button button = screen.findNiftyControl("continueButton", Button.class);
      try {
         File file = new File(fileName);
         gameStatus = (GameStatus) Utils.loadXML(new FileInputStream(file));
         String levelName = gameStatus.getRotation().getMaps().get(gameStatus.getLevel());
         if ("official".equals(gameStatus.getRotation().getName())) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/map/" + levelName + Utils.MAP_EXT);
            MapData mapData = Utils.loadMap(is);
            levelName = mapData.getName();
         }

         Label levelLabel = screen.findNiftyControl("levelLabel", Label.class);
         Label scoreLabel = screen.findNiftyControl("scoreLabel", Label.class);
         Label timeLabel = screen.findNiftyControl("timeLabel", Label.class);
         Label livesLabel = screen.findNiftyControl("livesLabel", Label.class);
         int lives = gameStatus.getLives();
         String livesString = (lives > 99 ? "**" : Integer.toString(lives));
         levelLabel.setText(Integer.toString(gameStatus.getLevel()) + ": " + levelName);
         scoreLabel.setText(Integer.toString(gameStatus.getScore()));
         timeLabel.setText(Utils.timeToString(gameStatus.getGameTime()));
         livesLabel.setText(livesString);
         levelLabel.getElement().getParent().layoutElements();
         button.setEnabled(true);
      } catch (Exception ex) {
         button.setEnabled(false);
      }
   }

   private void loadRotations() {
      new ListRotationsFuture().run();
   }

   private void setRotationsNames(List<String> list) {
      DropDown levelSetDropDown = screen.findNiftyControl("levelSetDropDown", DropDown.class);
      levelSetDropDown.clear();
      levelSetDropDown.addItem(OFFICIAL_ROT_NAME);
      levelSetDropDown.addAllItems(list);
   }

   public void onBackToMainButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_MAIN_MENU);
   }

   @Override
   public void bind(Nifty nifty, Screen screen) {
      this.nifty = nifty;
      this.screen = screen;
   }

   @Override
   public void onStartScreen() {
      loadStatus();
      loadRotations();
   }

   @Override
   public void onEndScreen() {
   }

   //<editor-fold defaultstate="collapsed" desc="ListRotationsFuture">
   private class ListRotationsFuture extends FutureTask<List<String>> {
      public ListRotationsFuture() {
         super(new Utils.LoadRotationCallable());
      }

      @Override
      protected void done() {
         try {
            List<String> list = get();
            setRotationsNames(list);
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
         super.done();
      }
   }
   //</editor-fold>
}
