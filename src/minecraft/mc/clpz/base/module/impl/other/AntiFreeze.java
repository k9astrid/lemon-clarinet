package mc.clpz.base.module.impl.other;

import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.awt.*;

import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.impl.game.PacketEvent;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.Printer;
import mc.clpz.base.utils.value.impl.BooleanValue;

public class AntiFreeze extends Module {
    private BooleanValue safetp = new BooleanValue("SafeTP",true);
    public AntiFreeze() {
        super("AntiFreeze", Category.OTHER, new Color(0xA25B41).getRGB());
        setDescription("Anti freeze screen");
        setRenderLabel("Anti Freeze");
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2DPacketOpenWindow) {
            S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) event.getPacket();
            if (packetOpenWindow.getWindowTitle().getUnformattedText().toLowerCase().contains("frozen")) {
                event.setCanceled(true);
            }
        }

        if (event.getPacket() instanceof S02PacketChat && safetp.isEnabled()) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getFormattedText().contains("is currently frozen, you may not attack.")) {
                getMc().thePlayer.setPositionAndUpdate(0, -999, 0);
                Printer.print("The person you tried to attack was frozen, teleported you to spawn.");
            }
        }
    }
}
