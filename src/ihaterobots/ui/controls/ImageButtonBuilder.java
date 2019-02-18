/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihaterobots.ui.controls;

import de.lessvoid.nifty.builder.ControlBuilder;

/**
 *
 * @author charly
 */
public class ImageButtonBuilder extends ControlBuilder {

   public ImageButtonBuilder(final String id) {
      super(id, "imageButton");
   }

   public ImageButtonBuilder(final String id, final String buttonLabel) {
      super(id, "imageButton");
      label(buttonLabel);
   }

   public ImageButtonBuilder(final String id, final String buttonLabel, final String imageFilename) {
      super(id, "imageButton");
      label(buttonLabel);
      image(imageFilename);
   }

   public void image(final String imageFileName) {
      set("imageFilename", imageFileName);
   }

   public void label(final String label) {
      set("label", label);
   }
}
