package com.charlyghislain.ihaterobots.ui.event;

public class MouseEvent {

    public final int mouseX;
    public final int mouseY;
    public final boolean buttonDown;
    public final int button;
    public final int mouseWheel;

    public MouseEvent(int mouseX, int mouseY, boolean buttonDown, int button, int mouseWheel) {

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.buttonDown = buttonDown;
        this.button = button;
        this.mouseWheel = mouseWheel;
    }
}
