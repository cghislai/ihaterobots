/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.serializables;

import ihaterobots.game.Utils;
import java.io.Serializable;

/**
 *
 * @author charly
 */
public class MapData implements Serializable {

    public int[][][] tiles;
    public String name;
    public String author;
    public int version;

    public MapData() {
        version = Utils.MAP_DATA_VERSION;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[][][] getTiles() {
        return tiles;
    }

    public void setTiles(int[][][] tiles) {
        this.tiles = tiles;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
