/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.game.map.AlphaMaskDrawer;
import ihaterobots.game.map.MapDrawer;
import ihaterobots.game.map.TileDrawer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

/**
 *
 * @author charly
 */
public class writeButtonsFile extends BasicGame {
   private static int[] placeOverIds;
   private static int[] rockPlaceOverIds;
   private MapDrawer mapDrawer;

   public writeButtonsFile() {
      super("po");
      mapDrawer = new MapDrawer();
      placeOverIds = new int[]{
         -1, Utils.TILE_ID_PO_BALL,
         Utils.TILE_ID_PO_WINDOW, Utils.TILE_ID_PO_FRONT,
         Utils.TILE_ID_PO_TOPLEFT, Utils.TILE_ID_PO_TOPRIGHT,
         Utils.TILE_ID_PO_BOTLEFT, Utils.TILE_ID_PO_BOTRIGHT,
         Utils.TILE_ID_PO_TOPLEFTRIGHT, Utils.TILE_ID_PO_RIGHTTOPBOT,
         Utils.TILE_ID_PO_BOTLEFTRIGHT, Utils.TILE_ID_PO_LEFTTOPBOT,
         Utils.TILE_ID_PO_NOTBOTLEFT, Utils.TILE_ID_PO_NOTBOTRIGHT,
         Utils.TILE_ID_PO_NOTTOPLEFT, Utils.TILE_ID_PO_NOTTOPRIGHT,
         Utils.TILE_ID_PO_ROLL_LEFT, Utils.TILE_ID_PO_ROLL_RIGHT,
         Utils.TILE_ID_PO_GRASS, Utils.TILE_ID_PO_ICE
      };
      rockPlaceOverIds = new int[]{
         Utils.TILE_ID_PO_ROCK_TOP, Utils.TILE_ID_PO_ROCK_LEFT,
         Utils.TILE_ID_PO_ROCK_RIGHT, Utils.TILE_ID_PO_ROCK_DOWN,
         Utils.TILE_ID_PO_ROCK_TOPLEFT, Utils.TILE_ID_PO_ROCK_TOPRIGHT,
         Utils.TILE_ID_PO_ROCK_BOTLEFT, Utils.TILE_ID_PO_ROCK_BOTRIGHT,
         Utils.TILE_ID_PO_ROCK_NOTDOWN, Utils.TILE_ID_PO_ROCK_NOTLEFT,
         Utils.TILE_ID_PO_ROCK_NOTRIGHT, Utils.TILE_ID_PO_ROCK_NOTTOP,
         Utils.TILE_ID_PO_ROCK_TOPBOT, Utils.TILE_ID_PO_ROCK_LEFTRIGHT,
         Utils.TILE_ID_PO_ROCK_ALL
      };
   }

   private static String getFileName(int tileId) {
      switch (tileId) {
         case Utils.TILE_ID_DIRT:
            return "dirt.png";
         case Utils.TILE_ID_ROCK:
            return "rock.png";
         case Utils.TILE_ID_SAND:
            return "sand.png";
         case Utils.TILE_ID_BRICKS:
            return "bricks.png";
         case Utils.TILE_ID_METAL:
            return "metal.png";
         case Utils.TILE_ID_GOLD:
            return "gold.png";
      }
      return null;
   }

   @Override
   public void init(GameContainer container) throws SlickException {

      String destPath = "assets/img/tile/menu/";
      GameEnv env = new GameEnv(null);
      env.init();
      mapDrawer.init(env);
      // rock/dirt/sand shape placeover + some misc
      for (final int tileid : new int[]{Utils.TILE_ID_ROCK, Utils.TILE_ID_METAL, Utils.TILE_ID_GOLD, Utils.TILE_ID_DIRT, Utils.TILE_ID_BRICKS, Utils.TILE_ID_SAND}) {
//        for (int curRowId = 0; curRowId < tilesIds.length; curRowId++) {
         try {
            Image tileImage = new Image("assets/img/tile/" + getFileName(tileid)).getSubImage(0, 0, 32, 32);
            for (int placeOId = 0; placeOId < placeOverIds.length; placeOId++) {
               Image image = new Image(tileImage.getWidth(), tileImage.getHeight());
               TileDrawer placeOverDrawer = mapDrawer.getTileDrawer(placeOverIds[placeOId]);
               if (placeOverIds[placeOId] == Utils.TILE_ID_PO_FRONT) {
                  placeOverDrawer = new AlphaMaskDrawer("placeover/frontmapedit.png");
                  placeOverDrawer.init(new GameEnv(null));
               }
               Graphics g = image.getGraphics();

               if (placeOverDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                  g.setColor(
                          new Color(1, 1, 1, 0));
                  g.fillRect(
                          0, 0, image.getWidth(), image.getHeight());
                  placeOverDrawer.draw(g,
                                       0, 0);
               }
               g.drawImage(tileImage, 0, 0);

               if (placeOverDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                  placeOverDrawer.draw(g,
                                       0, 0);
               }
               g.destroy();
               String path = getClass().getClassLoader().getResource(destPath).getPath();
               String filename = getFileName(tileid, placeOverIds[placeOId]);
               ImageOut.write(image, path + filename, true);


            }
         } catch (SlickException ex) {
         }
      }



      // Rock around placeovers
      // rock/dirt/sand shape placeover + some misc
      for (final int tileid : new int[]{Utils.TILE_ID_DIRT, Utils.TILE_ID_SAND, Utils.TILE_ID_BRICKS}) {
         try {
            Image tileImage = new Image("assets/img/tile/" + getFileName(tileid)).getSubImage(0, 0, 32, 32);
            for (int placeOId = 0; placeOId < rockPlaceOverIds.length; placeOId++) {
               Image image = new Image(tileImage.getWidth(), tileImage.getHeight());
               TileDrawer placeOverDrawer = mapDrawer.getTileDrawer(rockPlaceOverIds[placeOId]);
               Graphics g = image.getGraphics();

               if (placeOverDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                  g.setColor(
                          new Color(1, 1, 1, 1));
                  g.fillRect(
                          0, 0, image.getWidth(), image.getHeight());
                  placeOverDrawer.draw(g,
                                       0, 0);
               }
               g.drawImage(tileImage, 0, 0);

               if (placeOverDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                  placeOverDrawer.draw(g,
                                       0, 0);
               }
               g.destroy();
               String path = getClass().getClassLoader().getResource(destPath).getPath();
               String filename = getFileName(tileid, rockPlaceOverIds[placeOId]);
               ImageOut.write(image, path + filename, true);
            }

         } catch (SlickException ex) {
         }
      }
      System.out.println("yo");
      System.exit(0);

   }

   private String getFileName(int tileId, int poId) {
      String fileName = "";
      switch (tileId) {
         case Utils.TILE_ID_ROCK:
            fileName += "rock";
            break;
         case Utils.TILE_ID_DIRT:
            fileName += "dirt";
            break;
         case Utils.TILE_ID_BRICKS:
            fileName += "bricks";
            break;
         case Utils.TILE_ID_SAND:
            fileName += "sand";
            break;
         case Utils.TILE_ID_METAL:
            fileName += "metal";
            break;
         case Utils.TILE_ID_GOLD:
            fileName += "gold";
            break;
      }
      fileName += "-";
      fileName += poId;
      fileName += ".png";
      return fileName;
   }

   @Override
   public void update(GameContainer container, int delta) throws SlickException {
   }

   @Override
   public void render(GameContainer container, Graphics g) throws SlickException {
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         writeButtonsFile dede = new writeButtonsFile();
         AppGameContainer app = new AppGameContainer(dede);
         app.setDisplayMode(800, 600, false);
         app.start();
      } catch (SlickException e) {
         e.printStackTrace();
      }
   }
}
