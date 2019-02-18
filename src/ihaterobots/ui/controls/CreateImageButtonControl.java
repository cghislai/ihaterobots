/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.ui.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.button.builder.CreateButtonControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author charly
 */
public class CreateImageButtonControl extends CreateButtonControl {

   @Override
   public ImageButton create(
           final Nifty nifty,
           final Screen screen,
           final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
      nifty.addControlsWithoutStartScreen();
      return parent.findNiftyControl(attributes.get("id"), ImageButton.class);
   }
}
