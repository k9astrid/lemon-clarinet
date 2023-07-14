package dev.lemon.recode.event.impl;

import best.azura.eventbus.core.Event;

public class Event2DRender implements Event {
    private int width, height;

    public Event2DRender(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
