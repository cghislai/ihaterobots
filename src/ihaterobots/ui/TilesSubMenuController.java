/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ihaterobots.game.MapEditorState;
import ihaterobots.game.env.GameEnv;
import ihaterobots.game.map.editor.MapEditor;

/**
 *
 * @author charly
 */
public class TilesSubMenuController implements ScreenController {
   private int baseTileId;
   private GameEnv env;
   private Nifty nifty;
   private Screen screen;
   private final MapEditor mapEditor;
   private final MapEditorState mapEditorState;

   public TilesSubMenuController(int baseTileId, GameEnv env, MapEditor mapEditor, MapEditorState mapEditorState) {
      this.baseTileId = baseTileId;
      this.env = env;
      this.mapEditor = mapEditor;
      this.mapEditorState = mapEditorState;
   }

   public void onClick(String placeOverId) {
      mapEditor.setSelectedTileId(baseTileId);
      mapEditor.setSelectedPlaceOverId(new Integer(placeOverId));
      mapEditorState.hideUi();
   }

   public void onBackButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_MAP_EDITOR);
   }

   @Override
   public void bind(Nifty nifty, Screen screen) {
      this.nifty = nifty;
      this.screen = screen;
   }

   @Override
   public void onStartScreen() {
   }

   @Override
   public void onEndScreen() {
   }
}
