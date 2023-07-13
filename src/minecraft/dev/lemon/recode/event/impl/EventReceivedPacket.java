package dev.lemon.recode.event.impl;

import best.azura.eventbus.events.CancellableEvent;
import net.minecraft.network.Packet;

public class EventReceivedPacket extends CancellableEvent {
    public static EventReceivedPacket INSTANCE;
    private Packet packet;

    public EventReceivedPacket(Packet packet) {
        INSTANCE = this;
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {

        this.packet = packet;

    }

}
