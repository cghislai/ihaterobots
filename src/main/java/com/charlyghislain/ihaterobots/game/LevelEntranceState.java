/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game;

import com.charlyghislain.ihaterobots.game.env.GameEnv;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class LevelEntranceState extends BasicGameState {
   private final int time_one = 1000;
   private final GameEnv env;
   private Image box;
   private Image door;
   private Rectangle titleRect;
   private Rectangle authorRect;
   private Rectangle seeThroughRect;
   private int boxPosX;
   private int boxPosY;
   private int timer = 0;
   private float doorSpeed = 0.05f;
   private int leftDoorPosX;
   private int rightDoorPosX;
   private String levelName;
   private String authorName;
   private int levelNumber;
   private String levelString = "Entering level ";
   private String authorString = "By ";
   private UnicodeFont font;
   private boolean doorOpen;
   private GameContainer container;

   public LevelEntranceState(GameEnv env) {
      this.env = env;
   }

   @Override
   public int getID() {
      return Utils.STATE_ID_LEVEL_ENTRANCE;
   }

   @Override
   public void init(GameContainer container, StateBasedGame game) throws SlickException {
      this.container = container;
   }

   public void initStuff() {
      box = env.getDrawableManager().getImage(Utils.IMAGE_ENTERING_LEVEL_BOX);
      boxPosX = (container.getWidth() - box.getWidth()) / 2;
      boxPosY = (container.getHeight() - box.getHeight()) / 2;
      door = env.getDrawableManager().getImage(Utils.IMAGE_ENTERING_LEVEL_DOOR);
      titleRect = new Rectangle(boxPosX + 15, boxPosY + 10, 415, 75);
      authorRect = new Rectangle(boxPosX + 15, boxPosY + 415, 415, 25);
      seeThroughRect = new Rectangle(boxPosX + 20, boxPosY + 100, 408, 300);
      try {
         font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
         font.addAsciiGlyphs();
         font.addGlyphs(400, 600);
         font.loadGlyphs();
      } catch (SlickException ex) {
         Logger.getLogger(LevelEntranceState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      g.setClip(seeThroughRect);
      // draw scaled map
      g.pushTransform();
      g.translate(seeThroughRect.getX() + 4, seeThroughRect.getY());
      g.pushTransform();
      g.scale(.5f, .5f);
//      env.getMap().getMapDrawer().getBackDrawer().draw(g);
      env.getDrawableManager().draw(g);
//      env.getMap().getMapDrawer().getFrontDrawer().draw(g);
      g.popTransform();
      g.popTransform();

      // draw doors
      g.pushTransform();
      g.translate(seeThroughRect.getX() + leftDoorPosX, seeThroughRect.getY());
      g.drawImage(door, 0, 0);
      g.popTransform();

      g.pushTransform();
      g.translate(seeThroughRect.getX() + rightDoorPosX, seeThroughRect.getY());
      g.pushTransform();
      g.translate(door.getWidth() / 2, 0);
      g.pushTransform();
      g.scale(-1f, 1f);
      g.pushTransform();
      g.translate(-door.getWidth() / 2, 0);
      g.drawImage(door, 0, 0);
      g.popTransform();
      g.popTransform();
      g.popTransform();
      g.popTransform();
      g.clearClip();
      // draw box
      g.drawImage(box, boxPosX, boxPosY);

      // draw texts
      g.setFont(font);
      g.setColor(new Color(1f, .5f, 0f));
      g.setClip(titleRect);
      int titleWidth = font.getWidth(levelString + " " + levelNumber);
      int titleHeight = font.getHeight(levelString + " " + levelNumber);
      int nameWidth = font.getWidth(levelName);
      int nameHeight = font.getHeight(levelName);

      g.pushTransform();
      g.translate(titleRect.getX() + (titleRect.getWidth() - titleWidth) / 2, titleRect.getY());
      g.drawString(levelString + " " + levelNumber, 0, 0);
      g.popTransform();

      g.clearClip();

      g.pushTransform();
      g.translate(titleRect.getX() + (titleRect.getWidth() - 2 * nameWidth) / 2,
                  titleRect.getY() + titleHeight / 2 + (titleRect.getHeight() - 2 * nameHeight) / 2);
      g.pushTransform();
      g.scale(2f, 2f);
      g.drawString(levelName, 0, 0);
      g.popTransform();
      g.popTransform();

      g.setClip(authorRect);
      g.pushTransform();
      g.translate(authorRect.getX(), authorRect.getY());
      g.drawString(authorString + authorName, 0, 0);
      g.popTransform();
      g.clearClip();
   }

   @Override
   public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      if (timer > 0) {
         timer -= delta;
      }
      if (timer <= 0) {
         if (!doorOpen) {
            // 1 second elapsed, open door
            env.getSoundManager().startEntranceDoor();
            leftDoorPosX -= (doorSpeed * (float) delta);
            rightDoorPosX += (doorSpeed * delta);
            if (leftDoorPosX < -200 || rightDoorPosX > 400) {
               leftDoorPosX = -200;
               rightDoorPosX = 400;
               doorOpen = true;
               timer = time_one;
               env.getSoundManager().stopEntranceDoor();
            }
         } else {
            // 1 second elapsed after open door, go to next level
            enterGame();
         }
      }
   }

   @Override
   public void enter(GameContainer container, StateBasedGame game) throws SlickException {
      timer = time_one;
      leftDoorPosX = 0;
      rightDoorPosX = 200;
      doorOpen = false;
      levelName = env.getGameManager().getMapData().getName();
      authorName = env.getGameManager().getMapData().getAuthor();
      levelNumber = env.getGameManager().getCurrentLevel();
//      env.getPlayer().register(env); //so that pointer are set up
      env.getPlayer().setFrozen(true);
      env.getSoundManager().pauseMusic(); //TODO: door opening
   }

   @Override
   public void leave(GameContainer container, StateBasedGame game) throws SlickException {
//      env.getPlayer().unregister();
   }

   private void enterGame() {
      InGameState inGameState = (InGameState) env.getGame().getState(Utils.STATE_ID_INGAME);
      inGameState.setFromStateId(Utils.STATE_ID_MAINMENU);
      env.getGame().enterState(Utils.STATE_ID_WAITING_INGAME);
   }

   @Override
   public void keyPressed(int key, char c) {
      if (key == Input.KEY_H && Utils.EASY_SKIP) {
         enterGame();
      }
   }
}
