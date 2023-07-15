package dev.lemon.module.impl.combat;

import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventPacket;
import dev.lemon.module.Module;
import dev.lemon.event.IEventListener;

import dev.lemon.utils.IMethods;
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
