/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author charly
 */
public class EditableDropDownController<T> extends AbstractController implements EditableDropDown<T> {
    private static Logger log = Logger.getLogger(EditableDropDownController.class.getName());
    private Nifty nifty;
    private boolean alreadyOpen = false;
    private FocusHandler focusHandler;
    private Screen screen;
    private Element popup;
    private ListBox<T> listBox;
    private TextField textField;
    //   private NiftyControl dropDownButton;
    private DropDownViewConverter<T> viewConverter;


    @Override
    public void bind(@Nonnull Nifty niftyParam, @Nonnull Screen screenParam,
                     @Nonnull Element newElement, @Nonnull Parameters properties) {
        super.bind(newElement);
        nifty = niftyParam;
        screen = screenParam;
        focusHandler = screen.getFocusHandler();

        final String elementId = getElement().getId();
        if (elementId == null) {
            log.warning("The EditableDropDownControl requires an id but this one is missing it.");
            return;
        }
        popup = nifty.createPopupWithStyle(
                "editableDropDownBoxSelectPopup",
                getElement().getElementType().getAttributes().get("style"));

        popup.getControl(EditableDropDownPopupController.class).setDropDownElement(this);
        textField = newElement.findNiftyControl("#textField", TextField.class);
//      dropDownButton = newElement.findNiftyControl("#panel-icon", NiftyControl.class);
        listBox = popup.findNiftyControl("#listBox", ListBox.class);
        nifty.subscribe(screen, listBox.getId(), ListBoxSelectionChangedEvent.class, new EventTopicSubscriber<ListBoxSelectionChangedEvent>() {
            @Override
            public void onEvent(final String topic, final ListBoxSelectionChangedEvent data) {
                Object selectedItem = getSelectedItem(data.getSelection());

                ListBoxControl listBoxControl = (ListBoxControl) listBox;
                listBoxControl.getViewConverter().display(getElement().findElementById("#textField").findElementById("#text"), selectedItem);
                textField.setText((String) selectedItem);
//            listBoxControl.getViewConverter().display(getElement().findElementByName("#text"), selectedItem);

                int selectedItemIndex = -1;
                List<Integer> selectionIndices = data.getSelectionIndices();
                if (!selectionIndices.isEmpty()) {
                    selectedItemIndex = selectionIndices.get(0);
                }
                nifty.publishEvent(elementId, new DropDownSelectionChangedEvent(EditableDropDownController.this, selectedItem, selectedItemIndex));
                close();
            }

            private Object getSelectedItem(final List selection) {
                if (selection.isEmpty()) {
                    return null;
                }
                return selection.get(0);
            }
        });
    }

    public void onStartScreen() {
        updateEnabled();
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
            focusHandler.getNext(getElement()).setFocus();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
            focusHandler.getPrev(getElement()).setFocus();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.Activate) {
            dropDownClicked();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
            listBox.selectPrevious();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
            listBox.selectNext();
            return true;
        }
        return false;
    }

    public void dropDownClicked() {
        if (popup == null) {
            return;
        }
        if (alreadyOpen) {
            return;
        }

        alreadyOpen = true;
        nifty.showPopup(nifty.getCurrentScreen(), popup.getId(), null);
    }

    public void close() {
        alreadyOpen = false;
        nifty.closePopup(popup.getId());
    }

    public void refresh() {
    }

    private void updateEnabled() {
        setEnabled(!listBox.getItems().isEmpty());
        textField.setEnabled(true);
    }

    // DropDown implementation that forwards to the internal ListBox
    @Override
    public void setViewConverter(final DropDownViewConverter<T> viewConverter) {
        this.viewConverter = viewConverter;
        listBox.setListBoxViewConverter(new ListBoxViewConverter<T>() {
            @Override
            public void display(final Element listBoxItem, final T item) {
                viewConverter.display(listBoxItem, item);
            }

            @Override
            public int getWidth(final Element element, final T item) {
                return viewConverter.getWidth(element, item);
            }
        });
    }

    @Override
    public void addItem(final T newItem) {
        listBox.addItem(newItem);
        updateEnabled();
    }

    @Override
    public void insertItem(final T item, final int index) {
        listBox.insertItem(item, index);
        updateEnabled();
    }

    @Override
    public int itemCount() {
        return listBox.itemCount();
    }

    @Override
    public void clear() {
        listBox.clear();
        updateEnabled();
    }

    @Override
    public void selectItemByIndex(final int selectionIndex) {
        listBox.selectItemByIndex(selectionIndex);
    }

    @Override
    public void selectItem(final T item) {
        listBox.selectItem(item);
    }

    @Override
    public T getSelection() {
        List<T> selection = listBox.getSelection();
        if (selection.isEmpty()) {
            return null;
        }
        return selection.get(0);
    }

    @Override
    public int getSelectedIndex() {
        List<Integer> selection = listBox.getSelectedIndices();
        if (selection.isEmpty()) {
            return -1;
        }
        return selection.get(0);
    }

    @Override
    public void removeItemByIndex(final int itemIndex) {
        listBox.removeItemByIndex(itemIndex);
        updateEnabled();
    }

    @Override
    public void removeItem(final T item) {
        listBox.removeItem(item);
        updateEnabled();
    }

    @Override
    public List<T> getItems() {
        return listBox.getItems();
    }

    @Override
    public void addAllItems(@Nonnull Collection<T> collection) {
        listBox.addAllItems(collection);
        updateEnabled();
    }

    @Override
    public void removeAllItems(@Nonnull Collection<T> collection) {
        listBox.removeAllItems(collection);
        updateEnabled();
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public void setText(@Nonnull CharSequence charSequence) {
        textField.setText(charSequence);
    }

    @Override
    public void setMaxLength(int maxLength) {
        textField.setMaxLength(maxLength);
    }

    @Override
    public void setCursorPosition(int position) {
        textField.setCursorPosition(position);
    }

    @Override
    public void enableInputFilter(@Nullable TextFieldInputFilter textFieldInputFilter) {

    }

    @Override
    public void enableInputFilter(@Nullable TextFieldInputCharFilter textFieldInputCharFilter) {

    }

    @Override
    public void enableInputFilter(@Nullable TextFieldInputCharSequenceFilter textFieldInputCharSequenceFilter) {

    }

    @Override
    public void disableInputFilter() {

    }

    @Override
    public void enableDeleteFilter(@Nullable TextFieldDeleteFilter textFieldDeleteFilter) {

    }

    @Override
    public void disableDeleteFilter() {

    }

    @Override
    public void setFormat(@Nullable TextFieldDisplayFormat textFieldDisplayFormat) {

    }

    @Override
    public void enablePasswordChar(char passwordChar) {
        textField.enablePasswordChar(passwordChar);
    }

    @Nonnull
    @Override
    public String getDisplayedText() {
        return null;
    }

    @Nonnull
    @Override
    public String getRealText() {
        return null;
    }

    @Override
    public void disablePasswordChar() {
        textField.disablePasswordChar();
    }

    @Override
    public boolean isPasswordCharEnabled() {
        return textField.isPasswordCharEnabled();
    }
}
