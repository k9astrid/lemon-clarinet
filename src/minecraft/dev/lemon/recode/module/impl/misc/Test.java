package dev.lemon.recode.module.impl.misc;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPacket;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.player.ChatUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "Test", key= org.lwjgl.input.Keyboard.KEY_Y, category= Category.MISC, suffix = "")
public class Test extends Module {

    @EventHandler
    public Listener<EventPacket> eventPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook){
            ChatUtil.addMessage("when the s08 packet player pos look");
            S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)e.getPacket();
            if (mc.thePlayer.posY > s08.getY()) {
                s08.setY(mc.thePlayer.posY);
                s08.setX(mc.thePlayer.posX);
                s08.setZ(mc.thePlayer.posZ);
            }

        }
    };


}
