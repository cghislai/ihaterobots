/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.entities.controller;

import com.charlyghislain.ihaterobots.interfaces.Controllable;

/**
 *
 * @author charly
 */
public abstract class Controller {
   public enum Action {
      UP, DOWN, LEFT, RIGHT, FIRE,
      ESC, ENTER, DELETE, CTRL, MAJ, SPACE;
   }
   protected final Controllable controllable;

   public Controller(Controllable controllable) {
      this.controllable = controllable;
   }
}
