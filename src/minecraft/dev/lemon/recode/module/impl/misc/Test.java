package dev.lemon.recode.module.impl.misc;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPacket;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.player.ChatUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.*;

@ModuleInfo(name = "Test", key= org.lwjgl.input.Keyboard.KEY_Y, category= Category.MISC, suffix = "")
public class Test extends Module {

    @EventHandler
    public Listener<EventPacket> eventPacketListener = e -> {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.allowFlying = false;

        if (e.getPacket() instanceof C18PacketSpectate || e.getPacket() instanceof S39PacketPlayerAbilities || e.getPacket() instanceof C13PacketPlayerAbilities && mc.getNetHandler().doneLoadingTerrain){
            ChatUtil.addMessage("cancelled "+e.getPacket().getClass().getSimpleName());

            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S08PacketPlayerPosLook && mc.getNetHandler().doneLoadingTerrain){
            ChatUtil.addMessage("when the s08 packet player pos loo k");
            S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)e.getPacket();
            if (mc.thePlayer.getDistance(s08.getX(), s08.getY(), s08.getZ()) > 2) {
                s08.setX(mc.thePlayer.posX);
                s08.setZ(mc.thePlayer.posZ);
            }

        }
        if (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook){
            e.setCancelled(true);
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY-0.E25, mc.thePlayer.posZ, false));
        }
    };


}
