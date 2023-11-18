/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.charlyghislain.ihaterobots.game.InGameState;
import com.charlyghislain.ihaterobots.game.MapEditorState;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.game.env.GameManager;
import com.charlyghislain.ihaterobots.game.serializables.MapData;
import com.charlyghislain.ihaterobots.game.serializables.MapRotation;
import com.charlyghislain.ihaterobots.game.map.editor.MapEditor;
import com.charlyghislain.ihaterobots.ui.controls.EditableDropDown;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.bushe.swing.event.EventTopicSubscriber;

/**
 *
 * @author charly
 */
public class MapEditorMenuController implements ScreenController {
   private GameEnv env;
   private Nifty nifty;
   private final MapEditor mapEditor;
   private final MapEditorState mapEditorState;
   private MapData keptData;
   private List<String> availableMaps;
   private String helpPopupId;

   public MapEditorMenuController(GameEnv env, MapEditor mapEditor, MapEditorState mapEditorState) {
      this.env = env;
      this.mapEditor = mapEditor;
      this.mapEditorState = mapEditorState;
   }

   public void onTestButtonClick() {
      GameManager gameManager = env.getGameManager();
      gameManager.prepareNewGame();
      gameManager.setMapData(mapEditor.getMapData());
      gameManager.sendMapData();
      gameManager.startMap();
      InGameState state = (InGameState) env.getGame().getState(Utils.STATE_ID_INGAME);
      state.setFromStateId(mapEditorState.getID());
      env.getGame().enterState(state.getID());
   }

   public void onTileButtonClick(String tileId) {
      mapEditor.setSelectedTileId(new Integer(tileId));
      mapEditor.setSelectedPlaceOverId(-1);
      mapEditorState.hideUi();
   }

   public void onSaveButtonClick() {
      TextField nameField = nifty.getScreen(UIConstants.SCREEN_SAVE_MAP).findNiftyControl(UIConstants.TEXTFIELD_SAVE_MAPNAME, TextField.class);
      TextField authorField = nifty.getScreen(UIConstants.SCREEN_SAVE_MAP).findNiftyControl(UIConstants.TEXTFIELD_SAVE_AUTHOR, TextField.class);
      String mapName = nameField.getText();
      if (mapName.trim().isEmpty()) {
         setSaveInfoLabel("Map name can't be empty");
         return;
      }
      String authorName = authorField.getText();
      MapData data = env.getGameManager().getMapData();
      data.setName(mapName);
      data.setAuthor(authorName);
      new SaveMapFuture(data).run();
   }

   public void onSaveRotationButtonClick() {
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      EditableDropDown dropDown = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.DROPDOWN_ROTATIONS, EditableDropDown.class);
      if (dropDown.getText().trim().isEmpty()) {
         setRotationInfoLabel("Rotation name can't be empty!");
         return;
      }
      MapRotation rotation = new MapRotation();
      rotation.setMaps(mapsListBox.getItems());
      rotation.setName(dropDown.getText());
      new SaveRotationFuture(rotation).run();
   }

   public void onPushIntoRotationButtonClick() {
      ListBox availableMapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_AVAILABLE, ListBox.class);
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      List<String> selectedMaps = availableMapsListBox.getSelection();
      for (String map : selectedMaps) {
         availableMapsListBox.removeItem(map);
         mapsListBox.addItem(map);
      }
   }

   public void onPopFromRotationButtonClick() {
      ListBox availableMapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_AVAILABLE, ListBox.class);
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      List<String> selectedMaps = mapsListBox.getSelection();
      for (String map : selectedMaps) {
         mapsListBox.removeItem(map);
         availableMapsListBox.addItem(map);
      }
   }

   public void onMoveMapUpButtonClick() {
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      List<String> selectedMaps = mapsListBox.getSelection();
      List<String> items = new ArrayList<String>(mapsListBox.getItems());
      for (String map : selectedMaps) {
         int idx = items.indexOf(map);
         if (idx == 0) {
            break;
         }
         items.remove(map);
         items.add(idx - 1, map);
      }
      mapsListBox.clear();
      mapsListBox.addAllItems(items);
      for (String item : selectedMaps) {
         mapsListBox.selectItem(item);
      }
   }

   public void onMoveMapDownButtonClick() {
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      List<String> selectedMaps = mapsListBox.getSelection();
      List<String> items = new ArrayList<String>(mapsListBox.getItems());
      for (int i = selectedMaps.size() - 1; i >= 0; i--) { //reverse order
         String map = selectedMaps.get(i);
         int idx = items.indexOf(map);
         if (idx == items.size() - 1) {
            break;
         }
         items.remove(map);
         items.add(idx + 1, map);
      }
      mapsListBox.clear();
      mapsListBox.addAllItems(items);
      for (String item : selectedMaps) {
         mapsListBox.selectItem(item);
      }
   }

   public void onLoadButtonClick() {
      onBackToEditorMenuClick();
   }

   public void onBackFromLoadMenuClick() {
      loadMapData(keptData);
      onBackToEditorMenuClick();
   }

   public void onRotationsButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_MAP_ROTATION);
      new ListRotationsFuture().run();
      new ListMapsFuture().run();
   }

   public void onRockButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_ROCK_TILES);
   }

   public void onMetalButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_METAL_TILES);
   }

   public void onGOldButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_GOLD_TILES);
   }

   public void onDirtButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_DIRT_TILES);
   }

   public void onBrickButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_BRICK_TILES);
   }

   public void onSandButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_SAND_TILES);
   }

   public void onTriggersButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_TRIGGERS_TILES);
   }

   public void onBackButtonClick() {
      env.getGame().enterState(Utils.STATE_ID_MAINMENU);
   }

   public void onLoadMenuButtonClick() {
      keptData = env.getGameManager().getMapData();
      nifty.gotoScreen(UIConstants.SCREEN_LOAD_MAP);
      new ListMapsFuture().run();
   }

   public void onSaveMenuButtonClick() {
      nifty.gotoScreen(UIConstants.SCREEN_SAVE_MAP);
   }

   public void onHelpButtonClick() {
      nifty.showPopup(nifty.getCurrentScreen(), helpPopupId, null);
      nifty.getTopMostPopup().layoutElements();
   }

   public void onBackToEditorMenuClick() {
      nifty.gotoScreen(UIConstants.SCREEN_MAP_EDITOR);
   }

   public String arrow(String dir) {
      if (dir.equals("up")) {
         return "\u2191";
      } else if (dir.equals("down")) {
         return "\u2193";
      } else if (dir.equals("left")) {
         return "\u8592";
      } else if (dir.equals("right")) {
         return "\u2192";
      }
      return "?";
   }

   private void setSaveInfoLabel(String info) {
      Label label = nifty.getScreen(UIConstants.SCREEN_SAVE_MAP).findNiftyControl(UIConstants.LABEL_SAVE_INFO, Label.class);
      label.setText(info);
      label.getElement().getParent().layoutElements();
   }

   private void setLoadInfoLabel(String info) {
      Label label = nifty.getScreen(UIConstants.SCREEN_LOAD_MAP).findNiftyControl(UIConstants.LABEL_LOAD_INFO, Label.class);
      label.setText(info);
      label.getElement().getParent().layoutElements();
   }

   private void setRotationInfoLabel(String info) {
      Label label = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LABEL_ROTATION_INFO, Label.class);
      label.setText(info);
      label.getElement().getParent().layoutElements();
   }

   private void setLoadAuthorLabel(String info) {
      Label label = nifty.getScreen(UIConstants.SCREEN_LOAD_MAP).findNiftyControl(UIConstants.LABEL_LOAD_AUTHOR, Label.class);
      label.setText(info);
      label.getElement().getParent().layoutElements();
   }

   private void setMapsNames(List<String> maps) {
      availableMaps = maps;
      ListBox loadScreenListBox = nifty.getScreen(UIConstants.SCREEN_LOAD_MAP).findNiftyControl(UIConstants.LISTBOX_MAPS, ListBox.class);
      ListBox availableMapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_AVAILABLE, ListBox.class);
      loadScreenListBox.clear();
      availableMapsListBox.clear();
      loadScreenListBox.addAllItems(maps);
      availableMapsListBox.addAllItems(maps);
      onMapSelectionChanged();
   }

   private void setRotationsNames(List<String> rotations) {
      EditableDropDown dropDown = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.DROPDOWN_ROTATIONS, EditableDropDown.class);
      dropDown.addAllItems(rotations);
      onMapRotationSelectionChanged();
   }

   private void onMapSelectionChanged() {
      Screen loadScreen = nifty.getScreen(UIConstants.SCREEN_LOAD_MAP);
      ListBox listBox = loadScreen.findNiftyControl(UIConstants.LISTBOX_MAPS, ListBox.class);
      if (listBox.getSelection().isEmpty()) {
         return;
      }
      String selectedMap = (String) listBox.getSelection().get(0);
      new LoadMapFuture(selectedMap).run();
   }

   private void onMapRotationSelectionChanged() {
      Screen screen = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION);
      EditableDropDown dropDown = screen.findNiftyControl(UIConstants.DROPDOWN_ROTATIONS, EditableDropDown.class);
      String name = (String) dropDown.getSelection();
      new LoadRotationFuture(name).run();
   }

   private void loadMapData(MapData data) {
      env.getGameManager().setMapData(data);
      mapEditor.setMapData(data);
      setLoadAuthorLabel(data.author);
   }

   private void loadRotationData(MapRotation rotation) {
      ListBox availableMapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_AVAILABLE, ListBox.class);
      ListBox mapsListBox = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION).findNiftyControl(UIConstants.LISTBOX_ROTATION_MAPS, ListBox.class);
      availableMapsListBox.clear();
      mapsListBox.clear();
      availableMapsListBox.addAllItems(availableMaps);
      mapsListBox.addAllItems(rotation.maps);
      availableMapsListBox.removeAllItems(rotation.maps);
   }

   @Override
   public void bind(Nifty nifty, Screen screen) {
      this.nifty = nifty;

      Screen saveScreen = nifty.getScreen(UIConstants.SCREEN_SAVE_MAP);
      Screen loadScreen = nifty.getScreen(UIConstants.SCREEN_LOAD_MAP);
      Screen rotationScreen = nifty.getScreen(UIConstants.SCREEN_MAP_ROTATION);
      EventTopicSubscriber<TextFieldChangedEvent> mapNameSubs = new EventTopicSubscriber<TextFieldChangedEvent>() {
         @Override
         public void onEvent(String string, TextFieldChangedEvent t) {
            new CheckIfFileExistsFuture(t.getText()).run();
         }
      };

      EventTopicSubscriber<ListBoxSelectionChangedEvent> mapListSubs = new EventTopicSubscriber<ListBoxSelectionChangedEvent>() {
         @Override
         public void onEvent(String string, ListBoxSelectionChangedEvent t) {
            onMapSelectionChanged();
         }
      };
      EventTopicSubscriber<DropDownSelectionChangedEvent> rotationDropDownSubs = new EventTopicSubscriber<DropDownSelectionChangedEvent>() {
         @Override
         public void onEvent(String string, DropDownSelectionChangedEvent t) {
            onMapRotationSelectionChanged();
         }
      };
      nifty.subscribe(saveScreen, UIConstants.TEXTFIELD_SAVE_MAPNAME, TextFieldChangedEvent.class, mapNameSubs);
      nifty.subscribe(loadScreen, UIConstants.LISTBOX_MAPS, ListBoxSelectionChangedEvent.class, mapListSubs);
      nifty.subscribe(rotationScreen, UIConstants.DROPDOWN_ROTATIONS, DropDownSelectionChangedEvent.class, rotationDropDownSubs);

      // build popup
      InputStream helpIS = getClass().getClassLoader().getResourceAsStream("assets/ui/mapEditHelp.txt");
      Utils.buildTextPopup(helpIS, "helpPopup", nifty, "onBackButtonClick()");
      Element helpPopUp = nifty.createPopup("helpPopup");
      helpPopupId = helpPopUp.getId();
   }

   @Override
   public void onStartScreen() {
      if (nifty == null) {
         return;
      }
      if (nifty.getTopMostPopup() == null) {
         return;
      }
      nifty.closePopup(nifty.getTopMostPopup().getId());
   }

   @Override
   public void onEndScreen() {
   }

   //<editor-fold defaultstate="collapsed" desc="CheckIfFileExistsFuture">
   private class CheckIfFileExistsFuture extends FutureTask<Boolean> {
      public CheckIfFileExistsFuture(final String mapName) {
         super(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
               String fileName = mapName + Utils.MAP_EXT;
               File path = new File(Utils.MAP_SAVE_PATH);
               if (!path.exists()) {
                  return false;
               }
               for (String file : path.list()) {
                  if (file.equals(fileName)) {
                     return true;
                  }
               }
               return false;
            }
         });
      }

      @Override
      protected void done() {
         super.done();
         try {
            if (get()) {
               setSaveInfoLabel("File exits, it will be overwritten");
            } else {
               setSaveInfoLabel("");
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>

   //<editor-fold defaultstate="collapsed" desc="ListMapsFuture">
   private class ListMapsFuture extends FutureTask<List<String>> {
      public ListMapsFuture() {
         super(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
               File path = new File(Utils.MAP_SAVE_PATH);
               if (!path.exists()) {
                  return null;
               }
               List<String> files = new ArrayList<String>();
               for (String file : path.list()) {
                  if (file.endsWith(Utils.MAP_EXT)) {
                     files.add(file.replace(Utils.MAP_EXT, ""));
                  }
               }
               return files;
            }
         });
      }

      @Override
      protected void done() {
         super.done();
         try {
            List<String> list = get();
            if (list != null) {
               setMapsNames(list);
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>

   //<editor-fold defaultstate="collapsed" desc="SaveMapFuture">
   private class SaveMapFuture extends FutureTask<Boolean> {
      public SaveMapFuture(final MapData data) {
         super(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
               return Utils.saveMap(data);
            }
         });
      }

      @Override
      protected void done() {
         try {
            if (get()) {
               setSaveInfoLabel("Map saved.");
            } else {
               setSaveInfoLabel("An error occured while saveing map");
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>

   //<editor-fold defaultstate="collapsed" desc="LoadMapFuture">
   private class LoadMapFuture extends FutureTask<MapData> {
      public LoadMapFuture(final String mapName) {
         super(new Callable<MapData>() {
            @Override
            public MapData call() throws Exception {
               String path = Utils.MAP_SAVE_PATH + mapName + Utils.MAP_EXT;
               File file = new File(path);
               return Utils.loadMap(new FileInputStream(file));
            }
         });
      }

      @Override
      protected void done() {
         try {
            final MapData data = get();
            if (data != null) {
               loadMapData(data);
               setLoadInfoLabel(null);
            } else {
               setLoadInfoLabel("An error occured while loading map");
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>

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

   //<editor-fold defaultstate="collapsed" desc="SaveRotationFuture">
   private class SaveRotationFuture extends FutureTask<Boolean> {
      public SaveRotationFuture(final MapRotation data) {
         super(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
               return Utils.saveMapRotation(data);
            }
         });
      }

      @Override
      protected void done() {
         try {
            if (get()) {
               setRotationInfoLabel("Map rotation saved.");
            } else {
               setRotationInfoLabel("An error occured while saving rotation");
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>

   //<editor-fold defaultstate="collapsed" desc="LoadRotationFuture">
   private class LoadRotationFuture extends FutureTask<MapRotation> {
      public LoadRotationFuture(final String name) {
         super(new Callable<MapRotation>() {
            @Override
            public MapRotation call() throws Exception {
               String path = Utils.MAP_SAVE_PATH + name + Utils.MAP_EXT;
               File file = new File(path);
               return Utils.loadMapRotation(new FileInputStream(file));
            }
         });
      }

      @Override
      protected void done() {
         try {
            final MapRotation data = get();
            if (data != null) {
               loadRotationData(data);
               setRotationInfoLabel("");
            } else {
               setRotationInfoLabel("An error occured while loading rotations");
            }
         } catch (InterruptedException ex) {
         } catch (ExecutionException ex) {
         }
      }
   }
   //</editor-fold>
}
