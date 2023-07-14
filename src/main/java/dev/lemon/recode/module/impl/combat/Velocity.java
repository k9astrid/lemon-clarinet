package dev.lemon.recode.module.impl.combat;

import best.azura.eventbus.handler.EventHandler;
import dev.lemon.recode.event.impl.EventPacket;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import best.azura.eventbus.handler.Listener;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Velocity", category = Category.COMBAT)
public class Velocity extends Module {
    @EventHandler
    public Listener<EventPacket> eventPacketListener = e -> {
        if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()){
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S27PacketExplosion){
            e.setCancelled(true);
        }
    };
}
