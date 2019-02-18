/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.game.env;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author cghislai
 */
public class SoundManager implements MusicListener {
   private GameEnv env;
   private Sound breakSnd;
   private Sound coin;
   private Sound doorClose;
   private Sound doorOpen;
   private Sound drop;
   private Sound firing;
   private Sound flying;
   private Sound gwing;
   private Sound levelcleared;
   private Sound refill;
   private Sound walk;
   private Sound deathmarch;
   private Sound powerupLoop;
   private Sound teleport;
   private Sound robotStuck;
   private Sound jump;
   private Sound ladder;
   private Sound newlife;
   private Music music;
   private Sound boxbreak;
   private Sound fuelwaste;
   private Sound fuelfill;
   private Sound quickbreakclose;
   private Sound intro;
   private Sound levelendloop;
   private Sound powerupendloop;
   private Sound quickbreakopen;
   private Sound die;
   private Sound explode;
   private Sound entrancedoor;
   private String[] musicFiles;
   private int musicIdx;
   private float fxVolume;
   private float musicVolume;

   public void init(GameEnv env) {
      fxVolume = 1f;
      musicVolume = .3f;
      this.env = env;
      String path = "assets/snd/";
      musicIdx = 0;
      musicFiles = new String[]{
         "01 - Printed circuit song.ogg",
         "02 - Interstellar supermarket.ogg",
         "03 - Outrun.ogg",
         "04 - Jailbreak.ogg",
         "05 - Schleife.ogg",
         "06 - Rouge XIII.ogg",
         "07 - Who that bot.ogg"
      };
      try {
         music = new Music(path + "music/01 - Printed circuit song.ogg", true);
         music.addListener(this);
         breakSnd = new Sound(path + "break.wav");
         coin = new Sound(path + "coin.wav");
         doorOpen = new Sound(path + "dooropen.wav");
         doorClose = new Sound(path + "doorclose.wav");
         drop = new Sound(path + "drop.wav");
         firing = new Sound(path + "firing.wav");
         flying = new Sound(path + "flying.wav");
         gwing = new Sound(path + "gwing.wav");
         levelcleared = new Sound(path + "levelcleared.wav");
         refill = new Sound(path + "refill.wav");
         walk = new Sound(path + "walk.wav");
         deathmarch = new Sound(path + "deathmarch.wav");
         powerupLoop = new Sound(path + "poweruploop.wav");
         teleport = new Sound(path + "teleport.wav");
         robotStuck = new Sound(path + "robotstuck.wav");
         jump = new Sound(path + "jump.wav");
         ladder = new Sound(path + "ladder.wav");
         newlife = new Sound(path + "yeah.wav");
         boxbreak = new Sound(path + "boxbreak.wav");
         fuelwaste = new Sound(path + "fuelless.wav");
         fuelfill = new Sound(path + "fuelmore.wav");
         intro = new Sound(path + "intro.wav");
         levelendloop = new Sound(path + "levelendloop.wav");
         powerupendloop = new Sound(path + "powerupendloop.wav");
         quickbreakopen = new Sound(path + "quickbreakopen.wav");
         quickbreakclose = new Sound(path + "quickbreakclose.wav");
         die = new Sound(path + "die.wav");
         explode = new Sound(path + "explode.wav");
         entrancedoor = new Sound(path + "entrancedoor.wav");
      } catch (SlickException ex) {
         Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
         Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void nextMusic() throws Exception {
      String path = "assets/snd/music/";
      int newIndx = musicIdx;
      while (newIndx == musicIdx) {
         newIndx = (int) Math.floor(Math.random() * musicFiles.length);
      }
      musicIdx = newIndx;
      music = null;
      music = new Music(path + musicFiles[musicIdx], true);
      music.addListener(this);
      startMusic();
      env.getPlayer().newSong(musicFiles[musicIdx]);
   }

   public void stopAllLoops() {
      stopFiring();
      stopFlying();
      stopFuelFill();
      stopFuelWaste();
      stopIntro();
      stopLadder();
      stopLevelEndLoop();
      stopPowerupEndLoop();
      stopPowerupLoop();
      stopSteps();
      stopEntranceDoor();
   }

   public void playBreak() {
      breakSnd.play(getSpeed(1f), getFxVolume(.7f));
   }

   public void playRefill() {
      refill.play(getSpeed(1f), getFxVolume(.7f));
   }

   public void startSteps() {
      if (!walk.playing()) {
         walk.loop(getSpeed(.8f), getFxVolume(1f));
      }
   }

   public void stopSteps() {
      walk.stop();
   }

   public void playCoin() {
      coin.play(getSpeed(1f), getFxVolume(1f));
   }

   public void playDoorOpen() {
      doorOpen.play(getSpeed(1f), getFxVolume(.3f));
   }

   public void playDoorClose() {
      doorClose.play(getSpeed(1f), getFxVolume(.3f));
   }

   public void playDrop() {
      drop.play(getSpeed(1f), getFxVolume(1f));
   }

   public void startFiring() {
      if (!firing.playing()) {
         firing.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopFiring() {
      firing.stop();
   }

   public void startFlying() {
      if (!flying.playing()) {
         flying.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopFlying() {
      flying.stop();
   }

   public void playGoing() {
      gwing.play(getSpeed(1f), getFxVolume(.8f));
   }

   public void playLevelCleared() {
      levelcleared.play(2f, getFxVolume(1f));
   }

   public void playDeathMarch() {
      deathmarch.play(getSpeed(1.2f), getFxVolume(.3f));
   }

   public void startPOwerupLoop() {
      if (!powerupLoop.playing()) {
         powerupLoop.loop(getSpeed(1f), getFxVolume(.3f));
      }
   }

   public void stopPowerupLoop() {
      powerupLoop.stop();
   }

   public void playTeleport() {
      teleport.play(getSpeed(1f), getFxVolume(.3f));
   }

   public void startMusic() {
      if (!music.playing()) {
         music.play(getSpeed(1f), musicVolume);
      } else {
         music.stop();
         music.play(getSpeed(1f), musicVolume);
      }
   }

   public void pauseMusic() {
//      music.pause();
      music.setVolume(musicVolume / 4f);
   }

   public void resumeMusic() {
      if (!music.playing()) {
         music.resume();
      }
      music.setVolume(musicVolume);
   }

   public void playRobotStuck() {
      robotStuck.play(getSpeed(1f), getFxVolume(1f));
   }

   public void playJump() {
      jump.play(getSpeed(1f), getFxVolume(1f));
   }

   public void startLadder() {
      if (!ladder.playing()) {
         ladder.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopLadder() {
      ladder.stop();
   }

   public void playNewLife() {
      newlife.play(getSpeed(1f), getFxVolume(1f));
   }

   public void playBoxBreak() {
      boxbreak.play(getSpeed(1f), getFxVolume(.9f));
   }

   public void startFuelWaste() {
      if (!fuelwaste.playing()) {
         fuelwaste.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopFuelWaste() {
      fuelwaste.stop();
   }

   public void startFuelFill() {
      if (!fuelfill.playing()) {
         fuelfill.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopFuelFill() {
      fuelfill.stop();
   }

   public void playIntro() {
      intro.play(1f, getFxVolume(.4f));
   }

   public void stopIntro() {
      intro.stop();
   }

   public void startPowerUpEndLoop() {
      if (!powerupendloop.playing()) {
         powerupendloop.loop(getSpeed(1f), getFxVolume(1f));
      }
   }

   public void stopPowerupEndLoop() {
      powerupendloop.stop();
   }

   public void playQuickBreakOpen() {
      quickbreakopen.play(getSpeed(1f), getFxVolume(.7f));
   }

   public void playQuickBreakClose() {
      quickbreakclose.play(getSpeed(1f), getFxVolume(.7f));
   }

   public void startLevelEndLoop() {
      if (!levelendloop.playing()) {
         levelendloop.loop(1f, getFxVolume(1f));
      }
   }

   public void stopLevelEndLoop() {
      levelendloop.stop();
   }

   public void playDie() {
      die.play(getSpeed(1f), getFxVolume());
   }

   public void playExplode() {
      explode.play(getSpeed(1f), getFxVolume());
   }

   public void startEntranceDoor() {
      if (!entrancedoor.playing()) {
         entrancedoor.loop();
      }
   }

   public void stopEntranceDoor() {
      entrancedoor.stop();
   }

   @Override
   public void musicEnded(Music music) {
      try {
         nextMusic();
      } catch (Exception ex) {
         Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void musicSwapped(Music music, Music music1) {
   }

   public float getFxVolume() {
      return fxVolume;
   }

   public void setFxVolume(float fxVolume) {
      this.fxVolume = fxVolume;
   }

   public float getMusicVolume() {
      return musicVolume;
   }

   public void setMusicVolume(float musicVolume) {
      this.musicVolume = musicVolume;
      music.setVolume(musicVolume);
   }

   private float getFxVolume(float baseVol) {
      return baseVol * fxVolume;
   }

   private float getSpeed(float speed) {
      return .9f * speed + .1f * env.getGameManager().getGameOptions().getSpeed();
   }
}
