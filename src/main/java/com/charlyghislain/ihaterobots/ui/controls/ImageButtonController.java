/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlyghislain.ihaterobots.ui.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;

/**
 * @author charly
 */
public class ImageButtonController extends AbstractController implements ImageButton {

    private Screen screen;
    private FocusHandler focusHandler;
    private Element buttonTextElement;
    private Element buttonImageElement;
    private TextRenderer buttonTextRenderer;
    private ImageRenderer imageRenderer;

    @Override
    public void init(@Nonnull Parameters parameter) {
        super.init(parameter);
        focusHandler = screen.getFocusHandler();
    }

    @Override
    public void bind(@Nonnull Nifty niftyParam, @Nonnull Screen screenParam, @Nonnull Element newElement, @Nonnull Parameters parameter) {
        super.bind(newElement);
        screen = screenParam;
        buttonTextElement = getElement().findElementById("#text");
        buttonTextRenderer = buttonTextElement.getRenderer(TextRenderer.class);
        buttonImageElement = getElement().findElementById("#image");
        imageRenderer = buttonImageElement.getRenderer(ImageRenderer.class);

        String imageFilename = parameter.get("imageFilename");
        NiftyRenderEngine renderEngine = niftyParam.getRenderEngine();
        NiftyImage image = renderEngine.createImage(screen, imageFilename, false);
        setImage(image);

        EventTopicSubscriber<NiftyMousePrimaryClickedEvent> mouseClickedSubscriber = (topic, data) -> niftyParam.publishEvent(topic, new ButtonClickedEvent(ImageButtonController.this));
        niftyParam.subscribe(screen, newElement.getId(), NiftyMousePrimaryClickedEvent.class, mouseClickedSubscriber);
    }

    @Override
    public void onStartScreen() {
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        Element buttonElement = getElement();
        if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
            if (focusHandler != null) {
                focusHandler.getNext(buttonElement).setFocus();
            }
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
            if (focusHandler != null) {
                focusHandler.getPrev(buttonElement).setFocus();
            }
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.Activate) {
            buttonClick();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown) {
            if (focusHandler != null) {
                Element nextElement = focusHandler.getNext(buttonElement);
                if (nextElement.getParent().equals(buttonElement.getParent())) {
                    nextElement.setFocus();
                    return true;
                }
            }
        } else if (inputEvent == NiftyStandardInputEvent.MoveCursorUp) {
            if (focusHandler != null) {
                Element prevElement = focusHandler.getPrev(buttonElement);
                if (prevElement.getParent().equals(buttonElement.getParent())) {
                    prevElement.setFocus();
                    return true;
                }
            }
        }
        return false;
    }

    private void buttonClick() {
        getElement().onClick();
    }

    // Button Implementation
    @Override
    public void activate() {
        buttonClick();
    }

    @Override
    public String getText() {
        return buttonTextRenderer.getOriginalText();
    }

    @Override
    public void setText(final String text) {
        buttonTextRenderer.setText(text);
        if (!buttonTextRenderer.isLineWrapping()) {
            buttonTextElement.setConstraintWidth(new SizeValue(buttonTextRenderer.getTextWidth() + "px"));
        }
    }

    @Override
    public NiftyImage getImage() {
        return imageRenderer.getImage();
    }

    @Override
    public void setImage(NiftyImage image) {
        imageRenderer.setImage(image);
        buttonImageElement.setConstraintHeight(new SizeValue(image.getHeight() + "px"));
        buttonImageElement.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
    }

    @Override
    public int getTextWidth() {
        return buttonTextRenderer.getTextWidth();
    }

    @Override
    public int getTextHeight() {
        return buttonTextRenderer.getTextHeight();
    }

    @Override
    public RenderFont getFont() {
        return buttonTextRenderer.getFont();
    }

    @Override
    public void setFont(final RenderFont fontParam) {
        buttonTextRenderer.setFont(fontParam);
    }

    @Override
    public VerticalAlign getTextVAlign() {
        return buttonTextRenderer.getTextVAlign();
    }

    @Override
    public void setTextVAlign(final VerticalAlign newTextVAlign) {
        buttonTextRenderer.setTextVAlign(newTextVAlign);
    }

    @Override
    public HorizontalAlign getTextHAlign() {
        return buttonTextRenderer.getTextHAlign();
    }

    @Override
    public void setTextHAlign(final HorizontalAlign newTextHAlign) {
        buttonTextRenderer.setTextHAlign(newTextHAlign);
    }

    @Override
    public Color getTextColor() {
        return buttonTextRenderer.getColor();
    }

    @Override
    public void setTextColor(final Color newColor) {
        buttonTextRenderer.setColor(newColor);
    }
}
