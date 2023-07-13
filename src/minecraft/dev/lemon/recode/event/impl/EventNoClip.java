package dev.lemon.recode.event.impl;

import best.azura.eventbus.core.Event;
import net.minecraft.block.BlockOldLeaf;

public class EventNoClip implements Event {

    public boolean noClip;

    public EventNoClip(boolean noClip) {
        this.noClip = noClip;
    }
}
