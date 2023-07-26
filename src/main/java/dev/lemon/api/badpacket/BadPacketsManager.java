package dev.lemon.api.badpacket;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public class BadPacketsManager implements IMethods {

    private static boolean slot, attack, swing, block, inventory;

    public void initialize() {
        Lemon.INSTANCE.getEventBus().register(this);
    }

    public static boolean badPacket(final boolean slot, final boolean attack, final boolean swing, final boolean block, final boolean inventory) {
        return (BadPacketsManager.slot && slot) || (BadPacketsManager.attack && attack) || (BadPacketsManager.swing && swing) || (BadPacketsManager.block && block) || (BadPacketsManager.inventory && inventory);
    }

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        final Packet<?> p = e.getPacket();

        if (p instanceof C09PacketHeldItemChange)
            slot = true;
        else if (p instanceof C0APacketAnimation)
            swing = true;
        else if (p instanceof C02PacketUseEntity)
            attack = true;
        else if (p instanceof C08PacketPlayerBlockPlacement || p instanceof C07PacketPlayerDigging)
            block = true;
        else if (p instanceof C0EPacketClickWindow ||
                (p instanceof C16PacketClientStatus && ((C16PacketClientStatus) p).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) ||
                p instanceof C07PacketPlayerDigging)
            inventory = true;
        else if (p instanceof C03PacketPlayer)
            slot = swing = attack = block = inventory = false;
    };

}
