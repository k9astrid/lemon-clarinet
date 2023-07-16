package dev.lemon.client.events;

import dev.lemon.api.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.Packet;

@AllArgsConstructor @Getter
public class EventPacket extends CancellableEvent {
    private Packet packet;
    private Type type;

    public enum Type {
        SENT, RECEIVE
    }
}
