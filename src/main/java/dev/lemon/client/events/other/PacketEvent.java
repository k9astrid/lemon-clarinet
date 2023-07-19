package dev.lemon.client.events.other;

import dev.lemon.api.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

@AllArgsConstructor @Getter
public class PacketEvent extends CancellableEvent {
    private Packet packet;
    private Type type;
    private INetHandler iNetHandler;
    private EnumPacketDirection direction;

    public enum Type {
        SENT, RECEIVE
    }
}
