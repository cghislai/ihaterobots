/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.map.editor;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import com.charlyghislain.ihaterobots.game.MapEditorState;
import com.charlyghislain.ihaterobots.game.Utils;
import com.charlyghislain.ihaterobots.game.env.GameEnv;
import com.charlyghislain.ihaterobots.game.map.AlphaMaskDrawer;
import com.charlyghislain.ihaterobots.game.serializables.MapData;
import com.charlyghislain.ihaterobots.game.map.TileDrawer;
import com.charlyghislain.ihaterobots.interfaces.Drawable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

/**
 *
 * @author charly
 */
public class MapEditor implements Drawable {
   private final MapEditorState mapEditorState;
   private MapData mapData;
   private GameEnv env;
   private int cursorPosX = 0;
   private int cursorPosY = 0;
   private int anchorPosX = -1;
   private int anchorPosY = -1;
   private int selectedTileId = -1;
   private int selectedPlaceOverId = -1;
   private boolean anchoring = false;
   private boolean addTileKeyPressed = false;
   private boolean removeTileKeyPressed = false;
   private boolean addTileButtonDown = false;
   private boolean removeTileButtonDown = false;
   // Keep track of single tiles
   private int playerPosX = -1;
   private int playerPosY = -1;
   private int doorPosX = -1;
   private int doorPosY = -1;

   public MapEditor(GameEnv env, MapEditorState mapEditorState) {
      this.mapEditorState = mapEditorState;
      this.env = env;
      mapData = new MapData();
      mapData.tiles = new int[env.getMap().getWidth()][env.getMap().getHeigth()][6];
      clearMapData();
   }

   public void register() {
      env.getDrawableManager().addDrawable(this);
   }

   public void unregister() {
      anchoring = false;
      anchorPosX = -1;
      anchorPosY = -1;
      addTileKeyPressed = false;
      addTileButtonDown = false;
      removeTileKeyPressed = false;
      removeTileButtonDown = false;
//      env.getGame().mouseReleased(Input.MOUSE_LEFT_BUTTON, 0, 0);
//      env.getGame().mouseReleased(Input.MOUSE_RIGHT_BUTTON, 0, 0);
      env.getDrawableManager().removeDrawable(this);
   }

   @Override
   public void draw(Graphics g) {
      // Draw selected tile
      int tSize = Utils.TILE_SIZE;
      TileDrawer tileDrawer = env.getMap().getMapDrawer().getTileDrawer(selectedTileId);
      TileDrawer placeOverDrawer = env.getMap().getMapDrawer().getTileDrawer(selectedPlaceOverId);
      if (placeOverDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
         placeOverDrawer.draw(g, cursorPosX, cursorPosY);
      }
      if (tileDrawer != null) {
         tileDrawer.draw(g, cursorPosX, cursorPosY, .5f);
         g.setDrawMode(Graphics.MODE_NORMAL);
         
      }
      if (placeOverDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
         placeOverDrawer.draw(g, cursorPosX, cursorPosY);
      }
      // draw selection rectangle
      if (anchoring) {
         int fromX = Math.min(cursorPosX, anchorPosX);
         int toX = Math.max(cursorPosX, anchorPosX);
         int fromY = Math.min(cursorPosY, anchorPosY);
         int toY = Math.max(cursorPosY, anchorPosY);
         int width = (toX - fromX + 1) * tSize;
         int height = (toY - fromY + 1) * tSize;
         Rectangle rect = new RoundedRectangle(fromX * tSize, fromY * tSize,
                                               width, height, tSize / 5);
         g.setColor(new Color(.9f, .3f, .3f, .3f));
         g.fill(rect);
      }
      // draw cursor
      Rectangle rect = new Rectangle(cursorPosX * tSize, cursorPosY * tSize, tSize, tSize);
      g.setColor(new Color(1f, 0, 0, .8f));
      g.setLineWidth(.4f);
      g.draw(rect);
   }

   public void update(int delta) {
   }

   public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
      moveCursor(env.getMap().getTilePosX(mouseX), env.getMap().getTilePosY(mouseY));
      if (button == Input.MOUSE_LEFT_BUTTON) {
         if (anchoring) {
            // set tiles on mouse button release
            if (addTileButtonDown && !buttonDown) {
               setTiles();
            }
         } else {
            // set tiles if button down
            // BUG: no event received when button down
            //BUG: buttondown = true when coming back to state
            if (buttonDown && !addTileButtonDown) {
               setTiles();
            }
         }
         addTileButtonDown = buttonDown;
      } else if (button == Input.MOUSE_RIGHT_BUTTON) {
         if (anchoring) {
            // set tiles on mouse button release
            if (removeTileButtonDown && !buttonDown) {
               removeTiles();
            }
         } else {
            // set tiles if button down
            // BUG: no event received when button down
            if (buttonDown) {
               removeTiles();
            }
         }
         removeTileButtonDown = buttonDown;
      }
      return true;
   }

   private void moveCursor(int x, int y) {
      cursorPosX = x;//
      cursorPosY = y;//
      cursorPosX = Math.max(0, cursorPosX);
      cursorPosX = Math.min(cursorPosX, env.getMap().getWidth() - 1);
      cursorPosY = Math.max(0, cursorPosY);
      cursorPosY = Math.min(cursorPosY, env.getMap().getHeigth() - 1);
   }

   public boolean processKeyboardEvent(KeyboardInputEvent keyEvent) {
      if (keyEvent.getKey() == Input.KEY_LSHIFT) {
         anchoring = keyEvent.isKeyDown();
         if (anchoring) {
            anchorPosX = cursorPosX;
            anchorPosY = cursorPosY;
         } else {
            anchorPosX = -1;
            anchorPosY = -1;
         }
         return true;
      }

      if (keyEvent.getKey() == Input.KEY_SPACE || keyEvent.getKey() == Input.KEY_ENTER) {
         addTileKeyPressed = keyEvent.isKeyDown();
         if (addTileKeyPressed) {
            setTiles();
         }
      }
      if (keyEvent.getKey() == Input.KEY_BACK) {
         removeTileKeyPressed = keyEvent.isKeyDown();
         if (removeTileKeyPressed) {
            removeTiles();
         }
      }
      if (!keyEvent.isKeyDown()) {
         return false;
      }
      if (keyEvent.getKey() == Input.KEY_ESCAPE) {
         mapEditorState.showHideUi();
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_LEFT) {
         moveCursor(cursorPosX - 1, cursorPosY);
         if (addTileKeyPressed) {
            setTiles();
         }
         if (removeTileKeyPressed) {
            removeTiles();
         }
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_RIGHT) {
         moveCursor(cursorPosX + 1, cursorPosY);
         if (addTileKeyPressed) {
            setTiles();
         }
         if (removeTileKeyPressed) {
            removeTiles();
         }
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_UP) {
         moveCursor(cursorPosX, cursorPosY - 1);
         if (addTileKeyPressed) {
            setTiles();
         }
         if (removeTileKeyPressed) {
            removeTiles();
         }
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_DOWN) {
         moveCursor(cursorPosX, cursorPosY + 1);
         if (addTileKeyPressed) {
            setTiles();
         }
         if (removeTileKeyPressed) {
            removeTiles();
         }
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_R) {
         setRandomlyTiles();
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_C) {
         clearMapData();
         env.getGameManager().setMapData(mapData);
         env.getGameManager().sendMapData();
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_P) {
         setSelectedTileId(Utils.TILE_ID_PLAYER);
         setSelectedPlaceOverId(-1);
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_D) {
         setSelectedTileId(Utils.TILE_ID_DOOR_TOPLEFT);
         setSelectedPlaceOverId(-1);
         return true;
      }
      if (keyEvent.getKey() == Input.KEY_T) {
         mapEditorState.getMapEditorMenuController().onTestButtonClick();
         return true;
      }
      return false;
   }

   public void clearMapData() {
      for (int i = 0; i < mapData.tiles.length; i++) {
         for (int j = 0; j < mapData.tiles[i].length; j++) {
            for (int z = 0; z < mapData.tiles[i][j].length; z++) {
               mapData.tiles[i][j][z] = -1;
            }
            mapData.tiles[i][j][Utils.MAP_Z_BACK] = 0;
         }
      }
   }

   public MapData getMapData() {
      return mapData;
   }

   public void setSelectedTileId(int selectedTileId) {
      this.selectedTileId = selectedTileId;
   }

   public void setSelectedPlaceOverId(int selectedPlaceOverId) {
      this.selectedPlaceOverId = selectedPlaceOverId;
   }

   @Override
   public short getDrawablePriority() {
      return Utils.DRAWABLE_PRIORITY_FRONT;
   }

   private void setTiles() {
      if (selectedTileId == -1) {
         return;
      }
      if (!anchoring) {
         setTile(cursorPosX, cursorPosY);
         return;
      }
      int fromX = Math.min(cursorPosX, anchorPosX);
      int toX = Math.max(cursorPosX, anchorPosX);
      int fromY = Math.min(cursorPosY, anchorPosY);
      int toY = Math.max(cursorPosY, anchorPosY);
      for (int x = fromX; x < toX + 1; x++) {
         for (int y = fromY; y < toY + 1; y++) {
            setTile(x, y);
         }
      }
   }

   private void setTile(int x, int y) {
      int z = Utils.getTilePosZ(selectedTileId);
      int zp = Utils.getTilePosZ(selectedPlaceOverId);
      if (zp == -1) {
         if (z == Utils.MAP_Z_NORMAL) {
            zp = Utils.MAP_Z_PLACEOVER_NORMAL;
         } else if (z == Utils.MAP_Z_FRONT) {
            zp = Utils.MAP_Z_PLACEOVER_FRONT;
         }
      }
      if (selectedTileId == Utils.TILE_ID_LADDER_PIMP) {
         if (mapData.tiles[x][y][Utils.MAP_Z_NORMAL] != Utils.TILE_ID_LADDER) {
            return;
         }
      }
      if (selectedTileId == Utils.TILE_ID_DOOR_TOPLEFT) {
         clearDoor();
         doorPosX = x;
         doorPosY = y;
         setTileNoCheck(x, y, z, Utils.TILE_ID_DOOR_TOPLEFT);
         setTileNoCheck(x + 1, y, z, Utils.TILE_ID_DOOR);
         setTileNoCheck(x, y + 1, z, Utils.TILE_ID_DOOR);
         setTileNoCheck(x + 1, y + 1, z, Utils.TILE_ID_DOOR);
         return;
      }
      if (selectedTileId == Utils.TILE_ID_PLAYER) {
         clearPlayer();
         playerPosX = x;
         playerPosY = y;
      }
      if (selectedTileId == Utils.TILE_ID_RED_TELEPORT) {
         setTileNoCheck(x, y, Utils.MAP_Z_FRONT, Utils.TILE_ID_RED_TELEPORT_ANIM);
      }
      if (selectedTileId == Utils.TILE_ID_BLUE_TELEPORT) {
         setTileNoCheck(x, y, Utils.MAP_Z_FRONT, Utils.TILE_ID_BLUE_TELEPORT_ANIM);
      }
      if (selectedTileId == Utils.TILE_ID_GREEN_TELEPORT) {
         setTileNoCheck(x, y, Utils.MAP_Z_FRONT, Utils.TILE_ID_GREEN_TELEPORT_ANIM);
      }
      if (zp == Utils.MAP_Z_PLACEOVER_FRONT) {
         // put the tile to front
         z = Utils.MAP_Z_FRONT;
      }
      if (zp >= 0) { // will not erase when changing background or start entities
         setTileNoCheck(x, y, zp, selectedPlaceOverId);
      }
      setTileNoCheck(x, y, z, selectedTileId);
   }

   private void setTileNoCheck(int x, int y, int z, int tileId) {
      mapData.tiles[x][y][z] = tileId;
      env.getMap().setTile(x, y, z, tileId);
   }

   private void removeTiles() {
      if (!anchoring) {
         removeTile(cursorPosX, cursorPosY);
         return;
      }
      int fromX = Math.min(cursorPosX, anchorPosX);
      int toX = Math.max(cursorPosX, anchorPosX);
      int fromY = Math.min(cursorPosY, anchorPosY);
      int toY = Math.max(cursorPosY, anchorPosY);
      for (int x = fromX; x < toX + 1; x++) {
         for (int y = fromY; y < toY + 1; y++) {
            removeTile(x, y);
         }
      }
   }

   private void removeTile(int x, int y) {
      for (int z : new int[]{Utils.MAP_Z_FRONT, Utils.MAP_Z_STARTENTITIES, Utils.MAP_Z_NORMAL, Utils.MAP_Z_BACK}) {
         int tileId = mapData.tiles[x][y][z];
         if (tileId == Utils.TILE_ID_PLAYER) {
            clearPlayer();
            return;
         }
         if (tileId == Utils.TILE_ID_DOOR_TOPLEFT || tileId == Utils.TILE_ID_DOOR) {
            clearDoor();
            return;
         }
         if (tileId > 0) {
            setTileNoCheck(x, y, z, -1);
            if (z == Utils.MAP_Z_FRONT) {
               setTileNoCheck(x, y, Utils.MAP_Z_PLACEOVER_FRONT, -1);
            }
            if (z == Utils.MAP_Z_NORMAL) {

               setTileNoCheck(x, y, Utils.MAP_Z_PLACEOVER_NORMAL, -1);
            }
            if (z == Utils.MAP_Z_BACK) {
               setTileNoCheck(x, y, z, 0);
            }
            return;
         }
      }
   }

   private void clearDoor() {
      if (doorPosX == -1 || doorPosY == -1) {
         return;
      }
      int z = Utils.getTilePosZ(Utils.TILE_ID_DOOR_TOPLEFT);
      setTileNoCheck(doorPosX, doorPosY, z, -1);
      setTileNoCheck(doorPosX + 1, doorPosY, z, -1);
      setTileNoCheck(doorPosX, doorPosY + 1, z, -1);
      setTileNoCheck(doorPosX + 1, doorPosY + 1, z, -1);
   }

   private void clearPlayer() {
      if (playerPosX == -1 || playerPosY == -1) {
         return;
      }
      setTileNoCheck(playerPosX, playerPosY, Utils.MAP_Z_STARTENTITIES, -1);
   }

   public void setDoorPosX(int doorPosX) {
      this.doorPosX = doorPosX;
   }

   public void setDoorPosY(int doorPosY) {
      this.doorPosY = doorPosY;
   }

   public void setPlayerPosX(int playerPosX) {
      this.playerPosX = playerPosX;
   }

   public void setPlayerPosY(int playerPosY) {
      this.playerPosY = playerPosY;
   }

   public void setMapData(MapData data) {
      this.mapData = data;
      playerPosX = -1;
      playerPosY = -1;
      doorPosX = -1;
      doorPosY = -1;

      int[][][] tilesData = data.tiles;
      for (int x = 0; x < tilesData.length; x++) {
         for (int y = 0; y < tilesData[x].length; y++) {
            int tileId = tilesData[x][y][Utils.MAP_Z_STARTENTITIES];
            if (tileId == Utils.TILE_ID_PLAYER) {
               playerPosX = x;
               playerPosY = y;
            }
            tileId = tilesData[x][y][Utils.MAP_Z_NORMAL];
            if (tileId == Utils.TILE_ID_DOOR_TOPLEFT) {
               doorPosX = x;
               doorPosY = y;
            }
            if (playerPosX >= 0 && playerPosY >= 0 && doorPosX >= 0 && doorPosY >= 0) {
               return;
            }
         }
      }
   }

   private void setRandomlyTiles() {
      for (int i = 0; i < mapData.tiles.length; i++) {
         for (int j = 0; j < mapData.tiles[i].length; j++) {
            if (Math.random() < .05f) {
               setTile(i, j);
            }
         }
      }
   }
}
