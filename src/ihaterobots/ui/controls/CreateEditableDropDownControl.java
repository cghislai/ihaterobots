package ihaterobots.ui.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateEditableDropDownControl extends ControlAttributes {
   public CreateEditableDropDownControl() {
      setAutoId(NiftyIdCreator.generate());
      setName("editableDropDown");
   }

   public CreateEditableDropDownControl(final String id) {
      setId(id);
      setName("editableDropDown");
   }

   @SuppressWarnings("rawtypes")
   public EditableDropDown create(
           final Nifty nifty,
           final Screen screen,
           final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
      nifty.addControlsWithoutStartScreen();
      return parent.findNiftyControl(attributes.get("id"), EditableDropDown.class);
   }

   @Override
   public ElementType createType() {
      return new ControlType(attributes);
   }
}
