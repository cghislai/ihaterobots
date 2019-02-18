/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ihaterobots.game.Utils;
import ihaterobots.game.serializables.MapData;
import java.io.InputStream;

/**
 *
 * @author charly
 */
public class PortMapToNewVersion {
   int oldStartEnt = 4;
   int newStartEnt = 5;

   public static void main(String[] args) {
      String path = PortMapToNewVersion.class.getClassLoader().getResource("assets/map/").getPath();
      portMap(path+"empty.map");
      portMap(path+"ihaterobots.map");
      portMap(path+"odetojetpack.map");
      portMap(path+"outta space.map");
      portMap(path+"queues.map");
   }

   private static MapData getNewMapData(MapData data) {
      MapData newMapData = new MapData();
      newMapData.setAuthor(data.getAuthor());
      newMapData.setName(data.getName());

      int[][][] tiles = new int[data.tiles.length][data.tiles[0].length][6];
      for (int x = 0; x < data.tiles.length; x++) {
         for (int y = 0; y < data.tiles[0].length; y++) {
            tiles[x][y][0] = data.tiles[x][y][0];
            tiles[x][y][1] = data.tiles[x][y][1];
            tiles[x][y][2] = data.tiles[x][y][2];
            tiles[x][y][3] = data.tiles[x][y][3];
            tiles[x][y][4] = -1;
            tiles[x][y][5] = data.tiles[x][y][4];
         }
      }
      newMapData.setTiles(tiles);
      return newMapData;
   }
   
   private static void portMap(String map) {
//      MapData data = Utils.loadMap(map);
//      MapData newData = getNewMapData(data);
//      Utils.saveMap(newData);
   }
}
