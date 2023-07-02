package dev.lemon.recode.event.impl;

import best.azura.eventbus.core.Event;

public class EventKey implements Event {
    private int keyCode;

    public EventKey(int keyCode){
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
