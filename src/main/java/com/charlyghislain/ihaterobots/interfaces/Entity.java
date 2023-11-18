/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.interfaces;

import com.charlyghislain.ihaterobots.game.env.GameEnv;

/**
 *
 * @author charly
 */
public interface Entity {
   public int getEntityId();

   public float getPosX();

   public float getPosY();

   public int getWidth();

   public int getHeigth();

   public void register(GameEnv env);

   public void unregister();

   public boolean isStuck();
   
   public void doorLockChanged();
}
