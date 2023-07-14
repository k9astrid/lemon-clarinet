package dev.lemon.recode.event.impl;

import dev.lemon.recode.event.CancellableEvent;
import net.minecraft.network.Packet;

public class EventPacket extends CancellableEvent {
    private Packet packet;

    public EventPacket(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
