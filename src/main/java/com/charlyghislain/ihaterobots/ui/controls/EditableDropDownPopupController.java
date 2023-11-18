/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 *
 * @author charly
 */
public class EditableDropDownPopupController<T> extends AbstractController {
   private static Logger log = Logger.getLogger(EditableDropDownController.class.getName());
   private Nifty nifty;
   private Element element;
   private EditableDropDownController<T> dropDownControl;


   @Override
   public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen, @Nonnull Element element, @Nonnull Parameters parameter) {
      super.bind(element);
      this.nifty = nifty;
      this.element = element;
   }

   @Override
   public void init(@Nonnull Parameters parameter) {
      super.init(parameter);
   }

   public boolean inputEvent(final NiftyInputEvent inputEvent) {
      return false;
   }


   public void onStartScreen() {
      dropDownControl.refresh();
   }

   public void setDropDownElement(final EditableDropDownController dropDownControl) {
      this.dropDownControl = dropDownControl;
      Element panel = element.findElementById("#panel");
      panel.setConstraintX(new SizeValue(dropDownControl.getElement().getX() + "px"));
      panel.setConstraintY(new SizeValue(dropDownControl.getElement().getY() + dropDownControl.getHeight() + "px"));
      panel.setConstraintWidth(new SizeValue(dropDownControl.getWidth() + "px"));
      element.layoutElements();
   }

   public void close() {
      dropDownControl.close();
   }
}
