package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.EventPacket;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.IMethods;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@Module.Info(name = "Velocity", category = Module.Category.COMBAT)
public class Velocity extends Module {

    @Subscribe
    public final IEventListener<EventPacket> eventPacketListener = e -> {
        if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == IMethods.mc.thePlayer.getEntityId()){
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S27PacketExplosion){
            e.setCancelled(true);
        }
    };
}
