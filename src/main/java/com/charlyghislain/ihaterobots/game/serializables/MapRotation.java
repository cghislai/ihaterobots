/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.serializables;

import com.charlyghislain.ihaterobots.game.Utils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author charly
 */
public class MapRotation implements Serializable {
   public List<String> maps;
   public String name;
   public final int version;

   public MapRotation() {
      version = Utils.MAP_ROTATION_VERSION;
   }

   public List<String> getMaps() {
      return maps;
   }

   public void setMaps(List<String> maps) {
      this.maps = maps;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getVersion() {
      return version;
   }
}
