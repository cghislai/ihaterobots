/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui.controls;

import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.render.NiftyImage;

/**
 *
 * @author charly
 */
public interface ImageButton extends Button {

   NiftyImage getImage();

   void setImage(NiftyImage image);
}
