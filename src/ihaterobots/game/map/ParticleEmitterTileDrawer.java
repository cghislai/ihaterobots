/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.map;

import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.interfaces.Updatable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author charly
 */
public class ParticleEmitterTileDrawer extends TileDrawer implements Updatable {
   private ParticleSystem particleSystem;
   private String particleFileName;

   public ParticleEmitterTileDrawer(String particleFileName) {
      this.particleFileName = particleFileName;
   }

   @Override
   public void init(GameEnv env) throws SlickException {
      env.getUpdatableManager().addUpdatable(this);
      try {
         particleSystem = ParticleIO.loadConfiguredSystem("assets/particle/" + particleFileName);
      } catch (IOException ex) {
         Logger.getLogger(ParticleEmitterTileDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }
      particleSystem.setVisible(true);
   }

   @Override
   public void draw(Graphics g, int posx, int posy, float alpha) {
      particleSystem.render((posx + .5f) * Utils.TILE_SIZE, (posy + .5F) * Utils.TILE_SIZE);
   }

   @Override
   public void update(int delta) {
      particleSystem.update(delta);
   }
}
