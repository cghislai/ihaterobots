/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.map;

import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author charly
 */
public class AlphaMaskDrawer extends TileDrawer {

    private String filename;
    private Image image;

    public AlphaMaskDrawer(String filename) {
        this.filename = filename;

    }

    @Override
    public void init(GameEnv env) throws SlickException {
        image = new Image(imagePath + filename).getSubImage(0, 0, 32, 32);
    }

    @Override
    public void draw(Graphics g, int posx, int posy, float alpha) {
        g.setDrawMode(Graphics.MODE_ALPHA_MAP);
        g.drawImage(image, posx * Utils.TILE_SIZE, posy * Utils.TILE_SIZE);
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
    }
}
