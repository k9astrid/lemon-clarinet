package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.IMethods;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Cancel", "Cancel", "Custom");
    public NumberSetting horizontal = new NumberSetting("Horizontal", 0, 0, 100, 1);
    public NumberSetting vertical = new NumberSetting("Vertical", 0, 0, 100, 1);

    public Velocity() {
        super("Velocity", Category.COMBAT);
    }

    @Subscribe
    public final IEventListener<PacketEvent> eventPacketListener = e -> {
        switch (mode.getMode()) {
            case "Cancel":
                if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == IMethods.mc.player.getEntityId()) {
                    e.setCancelled(true);
                }
                if (e.getPacket() instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                }
            break;
            case "Custom":
                if (e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == IMethods.mc.player.getEntityId()) {
                    S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) e.getPacket();
                    velocityPacket.setMotionX((int) (velocityPacket.getMotionX() * (horizontal.getVal() / 100)));
                    velocityPacket.setMotionY((int) (velocityPacket.getMotionY() * (vertical.getVal() / 100)));
                    velocityPacket.setMotionZ((int) (velocityPacket.getMotionZ() * (horizontal.getVal() / 100)));
                }
                if (e.getPacket() instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                }
                break;
        }
    };
}
