/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.game.serializables;

import java.io.Serializable;
import org.newdawn.slick.Input;

/**
 *
 * @author charly
 */
public class GameOptions implements Serializable {
   public float fxVol;
   public float musicVol;
   public float speed;
   public int keyLeft;
   public int keyRight;
   public int keyUp;
   public int keyDown;
   public int keyFire;
   public int keyDie;

   public GameOptions() {
      fxVol = 1f;
      musicVol = .3f;
      speed = 1.2f;
      keyLeft = Input.KEY_LEFT;
      keyRight = Input.KEY_RIGHT;
      keyUp = Input.KEY_UP;
      keyDown = Input.KEY_DOWN;
      keyDie = Input.KEY_K;
   }

   public float getFxVol() {
      return fxVol;
   }

   public void setFxVol(float fxVol) {
      this.fxVol = fxVol;
   }

   public int getKeyDie() {
      return keyDie;
   }

   public void setKeyDie(int keyDie) {
      this.keyDie = keyDie;
   }

   public int getKeyDown() {
      return keyDown;
   }

   public void setKeyDown(int keyDown) {
      this.keyDown = keyDown;
   }

   public int getKeyFire() {
      return keyFire;
   }

   public void setKeyFire(int keyFire) {
      this.keyFire = keyFire;
   }

   public int getKeyLeft() {
      return keyLeft;
   }

   public void setKeyLeft(int keyLeft) {
      this.keyLeft = keyLeft;
   }

   public int getKeyRight() {
      return keyRight;
   }

   public void setKeyRight(int keyRight) {
      this.keyRight = keyRight;
   }

   public int getKeyUp() {
      return keyUp;
   }

   public void setKeyUp(int keyUp) {
      this.keyUp = keyUp;
   }

   public float getMusicVol() {
      return musicVol;
   }

   public void setMusicVol(float musicVol) {
      this.musicVol = musicVol;
   }

   public float getSpeed() {
      return speed;
   }

   public void setSpeed(float speed) {
      this.speed = speed;
   }
}
