package dev.lemon.event.impl;

public class EventMouse {

    private int buttonID;
    private boolean mouseDown;

    public EventMouse(int buttonID, boolean mouseDown) {
        this.buttonID = buttonID;
    }

    public int getButtonID() {
        return buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }

    public boolean isMotionEvent() {
        return buttonID == -1;
    }
}