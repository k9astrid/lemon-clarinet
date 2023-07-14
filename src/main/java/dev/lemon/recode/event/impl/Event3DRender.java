package dev.lemon.recode.event.impl;

import best.azura.eventbus.core.Event;

public class Event3DRender implements Event {
    private float partialTicks;

    public void Event3DRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

}
