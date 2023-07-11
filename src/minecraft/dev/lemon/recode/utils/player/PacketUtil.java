package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.Util;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public class PacketUtil implements Util {

    public static void sendPacket(Packet<?> packet, boolean silent) {
        if (mc.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }
    public static void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
    public static void sendPacketNoEvent(Packet packet) {
        PacketUtil.sendPacket(packet, true);
    }
}
