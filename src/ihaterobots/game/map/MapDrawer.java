/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.map;

import ihaterobots.game.Utils;
import ihaterobots.game.env.GameEnv;
import ihaterobots.interfaces.Drawable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author charly
 */
public class MapDrawer {
   private GameEnv env;
   private Map<Integer, TileDrawer> drawers;
   private final AnimatedTileDrawer goldDrawer1;
   private final AnimatedTileDrawer doorDrawer;
   private boolean isInEditState;
   private AnimatedTileDrawer rollDrowerRight;
   private final AnimatedTileDrawer rollDrowerLeft;
   private final AnimatedTileDrawer ladderUpDrawer;
   private final AnimatedTileDrawer ladderDownDrawer;
   private AlphaMaskDrawer mapEditFrontDrawer;
   private final Drawable frontDrawer;
   private final Drawable backDrawer;
   private final AnimatedTileDrawer redAutoTileDrawer;
   private final AnimatedTileDrawer blueAutoTileDrawer;
   private final AnimatedTileDrawer greenAutoTileDrawer;
   private final AnimatedTileDrawer gold2Drawer;

   public MapDrawer() {
      frontDrawer = new MapFrontDrawer();
      backDrawer = new MapBackDrawer();
      drawers = new HashMap<Integer, TileDrawer>();
      drawers.put(Utils.TILE_ID_BACK1, new StaticTileDrawer("background.png"));
      final StaticTileDrawer rockDrawer = new StaticTileDrawer("rock.png");
      drawers.put(Utils.TILE_ID_ROCK, rockDrawer);
      final StaticTileDrawer dirtDrawer = new StaticTileDrawer("dirt.png");
      drawers.put(Utils.TILE_ID_DIRT, dirtDrawer);
      final StaticTileDrawer sandDrawer = new StaticTileDrawer("sand.png");
      drawers.put(Utils.TILE_ID_SAND, sandDrawer);
      drawers.put(Utils.TILE_ID_LADDER, new StaticTileDrawer("ladder.png"));
      goldDrawer1 = new AnimatedTileDrawer("goldItem1.png", true, 10, 32, 32);
      drawers.put(Utils.TILE_ID_GOLD1, goldDrawer1);
      drawers.put(Utils.TILE_ID_SINGLE_FUEL, new StaticTileDrawer("singlefuel.png"));
      drawers.put(Utils.TILE_ID_DOUBLE_FUEL, new StaticTileDrawer("doubleFuel.png"));
      drawers.put(Utils.TILE_ID_EMPTY_DROP, new StaticTileDrawer("emptydropholder.png"));
      drawers.put(Utils.TILE_ID_DROP, new StaticTileDrawer("dropholder.png"));
      doorDrawer = new AnimatedTileDrawer("door.png", false, 150, 64, 64);
      drawers.put(Utils.TILE_ID_DOOR_TOPLEFT, doorDrawer);
      drawers.put(Utils.TILE_ID_PO_TOPLEFT, new AlphaMaskDrawer("placeover/topleft.png"));
      drawers.put(Utils.TILE_ID_PO_TOPRIGHT, new AlphaMaskDrawer("placeover/topright.png"));
      drawers.put(Utils.TILE_ID_PO_BOTLEFT, new AlphaMaskDrawer("placeover/botleft.png"));
      drawers.put(Utils.TILE_ID_PO_BOTRIGHT, new AlphaMaskDrawer("placeover/botright.png"));
      drawers.put(Utils.TILE_ID_PO_TOPLEFTRIGHT, new AlphaMaskDrawer("placeover/topleftright.png"));
      drawers.put(Utils.TILE_ID_PO_RIGHTTOPBOT, new AlphaMaskDrawer("placeover/righttopbot.png"));
      drawers.put(Utils.TILE_ID_PO_BOTLEFTRIGHT, new AlphaMaskDrawer("placeover/botleftright.png"));
      drawers.put(Utils.TILE_ID_PO_LEFTTOPBOT, new AlphaMaskDrawer("placeover/lefttopbot.png"));
      drawers.put(Utils.TILE_ID_PO_NOTTOPLEFT, new AlphaMaskDrawer("placeover/nottopleft.png"));
      drawers.put(Utils.TILE_ID_PO_NOTTOPRIGHT, new AlphaMaskDrawer("placeover/nottopright.png"));
      drawers.put(Utils.TILE_ID_PO_NOTBOTLEFT, new AlphaMaskDrawer("placeover/notbotleft.png"));
      drawers.put(Utils.TILE_ID_PO_NOTBOTRIGHT, new AlphaMaskDrawer("placeover/notbotright.png"));
      drawers.put(Utils.TILE_ID_PO_BALL, new AlphaMaskDrawer("placeover/ball.png"));
      rollDrowerRight = new AnimatedTileDrawer("placeover/rollRight.png", true, 51);
      drawers.put(Utils.TILE_ID_PO_ROLL_RIGHT, rollDrowerRight);
      rollDrowerLeft = new AnimatedTileDrawer("placeover/rollLeft.png", true, 51);
      drawers.put(Utils.TILE_ID_PO_ROLL_LEFT, rollDrowerLeft);
      ladderUpDrawer = new AnimatedTileDrawer("ladderup.png", true, 200);
      drawers.put(Utils.TILE_ID_LADDER_UP, ladderUpDrawer);
      ladderDownDrawer = new AnimatedTileDrawer("ladderdown.png", true, 200);
      drawers.put(Utils.TILE_ID_LADDER_DOWN, ladderDownDrawer);
      drawers.put(Utils.TILE_ID_PO_ICE, new StaticTileDrawer("placeover/ice.png"));
      drawers.put(Utils.TILE_ID_PO_GRASS, new StaticTileDrawer("placeover/grass.png"));
      drawers.put(Utils.TILE_ID_BACK_ROCK, new StaticTileDrawer("backrock.png"));
      drawers.put(Utils.TILE_ID_BACK_DIRT, new StaticTileDrawer("backdirt.png"));
      drawers.put(Utils.TILE_ID_BACK_SAND, new StaticTileDrawer("backsand.png"));
      drawers.put(Utils.TILE_ID_PO_FRONT, new AlphaMaskDrawer("placeover/frontingame.png"));
      drawers.put(Utils.TILE_ID_RED_DOOR_HOR, new TriggerableDrawer("reddoorhor.png", Utils.TILE_ID_RED_DOOR_HOR));
      drawers.put(Utils.TILE_ID_RED_TELEPORT, new AnimatedTileDrawer("redteleport.png", true, 500));
      drawers.put(Utils.TILE_ID_RED_TELEPORT_ANIM, new TeleportAnimDrawer("red"));
      redAutoTileDrawer = new AutoTriggerDrawer("redtriggerauto.png");
      drawers.put(Utils.TILE_ID_RED_DOOR_TRIGGER_AUTO, redAutoTileDrawer);
      drawers.put(Utils.TILE_ID_RED_DOOR_TRIGGER_DOWN, new StaticTileDrawer("redtriggerdown.png"));
      drawers.put(Utils.TILE_ID_RED_DOOR_TRIGGER_UP, new StaticTileDrawer("redtriggerup.png"));
      drawers.put(Utils.TILE_ID_RED_DOOR_VERT, new TriggerableDrawer("reddoorvert.png", Utils.TILE_ID_RED_DOOR_VERT));
      drawers.put(Utils.TILE_ID_BLUE_DOOR_HOR, new TriggerableDrawer("bluedoorhor.png", Utils.TILE_ID_BLUE_DOOR_HOR));
      drawers.put(Utils.TILE_ID_BLUE_TELEPORT, new AnimatedTileDrawer("blueteleport.png", true, 500));
      drawers.put(Utils.TILE_ID_BLUE_TELEPORT_ANIM, new TeleportAnimDrawer("blue"));
      blueAutoTileDrawer = new AutoTriggerDrawer("bluetriggerauto.png");
      drawers.put(Utils.TILE_ID_BLUE_DOOR_TRIGGER_AUTO, blueAutoTileDrawer);
      drawers.put(Utils.TILE_ID_BLUE_DOOR_TRIGGER_DOWN, new StaticTileDrawer("bluetriggerdown.png"));
      drawers.put(Utils.TILE_ID_BLUE_DOOR_TRIGGER_UP, new StaticTileDrawer("bluetriggerup.png"));
      drawers.put(Utils.TILE_ID_BLUE_DOOR_VERT, new TriggerableDrawer("bluedoorvert.png", Utils.TILE_ID_BLUE_DOOR_VERT));
      drawers.put(Utils.TILE_ID_GREEN_DOOR_HOR, new TriggerableDrawer("greendoorhor.png", Utils.TILE_ID_GREEN_DOOR_HOR));
      drawers.put(Utils.TILE_ID_GREEN_TELEPORT, new AnimatedTileDrawer("greenteleport.png", true, 500));
      drawers.put(Utils.TILE_ID_GREEN_TELEPORT_ANIM, new TeleportAnimDrawer("green"));
      greenAutoTileDrawer = new AutoTriggerDrawer("greentriggerauto.png");
      drawers.put(Utils.TILE_ID_GREEN_DOOR_TRIGGER_AUTO, greenAutoTileDrawer);
      drawers.put(Utils.TILE_ID_GREEN_DOOR_TRIGGER_DOWN, new StaticTileDrawer("greentriggerdown.png"));
      drawers.put(Utils.TILE_ID_GREEN_DOOR_TRIGGER_UP, new StaticTileDrawer("greentriggerup.png"));
      drawers.put(Utils.TILE_ID_GREEN_DOOR_VERT, new TriggerableDrawer("greendoorvert.png", Utils.TILE_ID_GREEN_DOOR_VERT));
      drawers.put(Utils.TILE_ID_PO_ROCK_ALL, new StaticTileDrawer("placeover/rockall.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_BOTLEFT, new StaticTileDrawer("placeover/rockbotleft.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_BOTRIGHT, new StaticTileDrawer("placeover/rockbotright.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_DOWN, new StaticTileDrawer("placeover/rockbot.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_LEFT, new StaticTileDrawer("placeover/rockleft.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_NOTDOWN, new StaticTileDrawer("placeover/rocknotbot.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_NOTLEFT, new StaticTileDrawer("placeover/rocknotleft.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_NOTRIGHT, new StaticTileDrawer("placeover/rocknotright.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_NOTTOP, new StaticTileDrawer("placeover/rocknottop.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_RIGHT, new StaticTileDrawer("placeover/rockright.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_TOP, new StaticTileDrawer("placeover/rocktop.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_TOPLEFT, new StaticTileDrawer("placeover/rocktopleft.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_TOPRIGHT, new StaticTileDrawer("placeover/rocktopright.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_TOPBOT, new StaticTileDrawer("placeover/rocktopbot.png"));
      drawers.put(Utils.TILE_ID_PO_ROCK_LEFTRIGHT, new StaticTileDrawer("placeover/rockleftright.png"));
      drawers.put(Utils.TILE_ID_PO_WINDOW, new AlphaMaskDrawer("placeover/window.png"));
      drawers.put(Utils.TILE_ID_SPIKES, new StaticTileDrawer("spikes.png"));
      drawers.put(Utils.TILE_ID_QUICK_BREAK, new StaticTileDrawer("quickbreak.png"));
      drawers.put(Utils.TILE_ID_WOODCRATE, new StaticTileDrawer("woodcrate.png"));
      drawers.put(Utils.TILE_ID_COLUMN, new StaticTileDrawer("column.png"));
      drawers.put(Utils.TILE_ID_BARRIER, new StaticTileDrawer("barrier.png"));
      drawers.put(Utils.TILE_ID_WARNING_SIGN, new StaticTileDrawer("warning.png"));
      drawers.put(Utils.TILE_ID_POWERUP_BLUESTAR, new AnimatedTileDrawer("powerup-bluestar.png", true, 100, 32, 32));
      drawers.put(Utils.TILE_ID_POWERUP_LIGHT, new AnimatedTileDrawer("powerup-lightning.png", true, 100, 16, 32));
      drawers.put(Utils.TILE_ID_FUEL_FILL, new ParticleEmitterTileDrawer("fuelgreen.xml"));
      drawers.put(Utils.TILE_ID_FUEL_WASTE, new ParticleEmitterTileDrawer("fuelred.xml"));
      drawers.put(Utils.TILE_ID_SPIKES_LEFT, new StaticTileDrawer("spikesleft.png"));
      drawers.put(Utils.TILE_ID_SPIKES_RIGHT, new StaticTileDrawer("spikesright.png"));
      drawers.put(Utils.TILE_ID_SPIKES_TOP, new StaticTileDrawer("spikestop.png"));
      drawers.put(Utils.TILE_ID_GOLD_STATUE, new StaticTileDrawer("golditem3.png"));
      gold2Drawer = new AnimatedTileDrawer("golditem2.png", true, 200, 32, 32);
      drawers.put(Utils.TILE_ID_GOLD2, gold2Drawer);
      drawers.put(Utils.TILE_ID_BRICKS, new StaticTileDrawer("bricks.png"));
      drawers.put(Utils.TILE_ID_METAL, new StaticTileDrawer("metal.png"));
      drawers.put(Utils.TILE_ID_GOLD, new StaticTileDrawer("gold.png"));
      drawers.put(Utils.TILE_ID_COLUMN_BOT, new StaticTileDrawer("colbot.png"));
      drawers.put(Utils.TILE_ID_COLUMN_TOP, new StaticTileDrawer("coltop.png"));
      drawers.put(Utils.TILE_ID_COLUMN_MID, new StaticTileDrawer("colmid.png"));
      drawers.put(Utils.TILE_ID_COLUMN_WOOD, new StaticTileDrawer("woodcolumn.png"));
   }

   public void init(GameEnv env) {
      this.env = env;
      env.getDrawableManager().addDrawable(frontDrawer);
      env.getDrawableManager().addDrawable(backDrawer);
      //ENTITIES
      drawers.put(Utils.TILE_ID_PLAYER, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_PLAYER_STANDING).getSubImage(0, 0, 16, 30)));
      drawers.put(Utils.TILE_ID_ROBOT1, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_ROBOT1_WALK).getSubImage(0, 0, 16, 30)));
      drawers.put(Utils.TILE_ID_ROBOT2, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_BOWL).getSubImage(0, 0, 30, 30)));
      drawers.put(Utils.TILE_ID_ROCKET, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_ROCKET).getSubImage(0, 0, 10, 30)));
      drawers.put(Utils.TILE_ID_LADDER_PIMP, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_LADDER_PIMP).getSubImage(0, 0, 24, 24)));
      drawers.put(Utils.TILE_ID_VERT_JUMPER, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_VERTJUMPER).getSubImage(0, 0, 10, 32)));
      drawers.put(Utils.TILE_ID_DIGGER, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_DIGGER).getSubImage(0, 0, 24, 24)));
      drawers.put(Utils.TILE_ID_FIRE_BALL, new StaticTileDrawer(env.getDrawableManager().getImage(Utils.IMAGE_FIREBALL)));
      drawers.put(Utils.TILE_ID_FLYINGJET, new StaticTileDrawer(env.getDrawableManager().getSheet(Utils.SHEET_FLYINGJET).getSubImage(0, 0)));

      try {
         for (TileDrawer drawer : drawers.values()) {
            drawer.init(env);
         }
         mapEditFrontDrawer = new AlphaMaskDrawer("placeover/frontmapedit.png");
         mapEditFrontDrawer.init(env);
      } catch (SlickException ex) {
         Logger.getLogger(MapDrawer.class.getName()).log(Level.SEVERE, null, ex);
      }


      goldDrawer1.getAnimation().setDuration(0, (int) (Math.random() * 30000));
      goldDrawer1.getAnimation().setDuration(1, 50);
      goldDrawer1.getAnimation().setDuration(2, 150);
      goldDrawer1.getAnimation().setPingPong(true);
      goldDrawer1.getAnimation().setAutoUpdate(true);
      goldDrawer1.getAnimation().setLooping(true);
      goldDrawer1.start();
      gold2Drawer.getAnimation().setPingPong(true);

      doorDrawer.getAnimation().setAutoUpdate(false);
      rollDrowerRight.getAnimation().setAutoUpdate(true);
      rollDrowerRight.start();
      rollDrowerLeft.getAnimation().setAutoUpdate(true);
      rollDrowerLeft.start();
      ladderUpDrawer.getAnimation().setAutoUpdate(true);
      ladderUpDrawer.start();
   }

   public TileDrawer getTileDrawer(int tileId) {
      if (tileId == -1) {
         return null;
      }
      return drawers.get(tileId);
   }

   public void triggerDoor() {
      doorDrawer.getAnimation().setAutoUpdate(true);
      doorDrawer.getAnimation().setLooping(false);
      doorDrawer.getAnimation().restart();
   }

   public void resetDoor() {
      doorDrawer.getAnimation().setAutoUpdate(false);
      doorDrawer.getAnimation().setLooping(false);
      doorDrawer.getAnimation().stop();
      doorDrawer.getAnimation().setCurrentFrame(0);
   }

   private void drawBack(Graphics g) {
      GameMap map = env.getMap();
      TileDrawer placeOverDrawer = null;
      for (int z : new int[]{Utils.MAP_Z_BACK, Utils.MAP_Z_NORMAL, Utils.MAP_Z_STARTENTITIES}) {
         if (z == Utils.MAP_Z_STARTENTITIES && !isInEditState) {
            break;
         }
         for (int j = 0; j < map.getHeigth(); j++) {
            for (int i = 0; i < map.getWidth(); i++) {
               int tileId = map.getTileId(i, j, z);
               if (tileId == Utils.TILE_ID_DROP) {
               }
               TileDrawer drawer = getTileDrawer(tileId);
               final int placeoverId = map.getTileId(i, j, Utils.MAP_Z_PLACEOVER_NORMAL);

               if (tileId > 0 && z == Utils.MAP_Z_NORMAL) {
                  placeOverDrawer = getTileDrawer(placeoverId);
                  if (placeOverDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                     placeOverDrawer.draw(g, i, j);
                  }
               }
               if (drawer != null) {
                  drawer.draw(g, i, j);
                  g.setDrawMode(Graphics.MODE_NORMAL);
               }
               if (tileId > 0 && z == Utils.MAP_Z_NORMAL) {
                  placeOverDrawer = getTileDrawer(placeoverId);
                  if (placeOverDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(placeOverDrawer.getClass())) {
                     placeOverDrawer.draw(g, i, j);
                  }
               }
            }
         }
      }
   }

   private void drawFront(Graphics g) {
      GameMap map = env.getMap();
      int z = Utils.MAP_Z_FRONT;
      for (int j = 0; j < map.getHeigth(); j++) {
         for (int i = 0; i < map.getWidth(); i++) {
            int tileId = map.getTileId(i, j, z);
            int poId = map.getTileId(i, j, Utils.MAP_Z_PLACEOVER_FRONT);
            TileDrawer drawer = getTileDrawer(tileId);
            TileDrawer poDrawer = getTileDrawer(poId);
            if (poId == Utils.TILE_ID_PO_FRONT && isInEditState) {
               poDrawer = mapEditFrontDrawer;
            }

            if (tileId > 0 && poDrawer != null && AlphaMaskDrawer.class.isAssignableFrom(poDrawer.getClass())) {
               poDrawer.draw(g, i, j);
            }

            if (drawer != null) {
               drawer.draw(g, i, j);
               g.setDrawMode(Graphics.MODE_NORMAL);
            }

            if (tileId > 0 && poDrawer != null && !AlphaMaskDrawer.class.isAssignableFrom(poDrawer.getClass())) {
               poDrawer.draw(g, i, j);
            }
         }
      }
   }

   public boolean isIsInEditState() {
      return isInEditState;
   }

   public void setIsInEditState(boolean isInEditState) {
      this.isInEditState = isInEditState;
   }

   private class MapBackDrawer implements Drawable {
      @Override
      public void draw(Graphics g) {
         drawBack(g);
      }

      @Override
      public short getDrawablePriority() {
         return Utils.DRAWABLE_PRIORITY_BACKGROUND;
      }
   }

   private class MapFrontDrawer implements Drawable {
      @Override
      public void draw(Graphics g) {
         drawFront(g);
      }

      @Override
      public short getDrawablePriority() {
         return Utils.DRAWABLE_PRIORITY_FRONT;
      }
   }

   public Drawable getBackDrawer() {
      return backDrawer;
   }

   public Drawable getFrontDrawer() {
      return frontDrawer;
   }
}
