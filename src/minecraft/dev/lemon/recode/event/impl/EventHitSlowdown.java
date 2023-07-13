package dev.lemon.recode.event.impl;

import best.azura.eventbus.events.CancellableEvent;

public final class EventHitSlowdown extends CancellableEvent {
    public double slowDown;
    public boolean sprint;
}
