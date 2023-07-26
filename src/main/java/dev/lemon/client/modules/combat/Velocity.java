package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.math.TimerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.MovingObjectPosition;

public class Velocity extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Cancel",
            "Cancel",
            "Custom",
            "MineMenClub",
            "C0F",
            "KoksCraft",
            "Legit",
            "Intave",
            "Matrix",
            "Grim"
    );

    public NumberSetting horizontal = new NumberSetting("Horizontal", 0, 0, 100, 1, () -> mode.is("Custom"));
    public NumberSetting vertical = new NumberSetting("Vertical", 0, 0, 100, 1, () -> mode.is("Custom"));

    private int mmcTicks, cancel = 6, reset = 8, grimC, updates;

    public Velocity() {
        super("Velocity", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        mmcTicks = 0;
        grimC = 0;
    }

    @Subscribe
    public final IEventListener<PacketEvent> onPacket = e -> {
        final Packet<?> packet = e.getPacket();

        switch (mode.getMode()) {
            case "KoksCraft":
                if (packet instanceof C0FPacketConfirmTransaction && mc.player.hurtTime > 1) {
                    final C0FPacketConfirmTransaction wrapper = (C0FPacketConfirmTransaction) packet;

                    if (wrapper.getUid() >= -31767 && wrapper.getUid() <= -30769) {
                        e.setCancelled(true);
                    }
                }

                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                    if (wrapper.getEntityID() == mc.player.getEntityId())
                        e.setCancelled(true);
                }
                break;

            case "C0F":
                if (packet instanceof C0FPacketConfirmTransaction && mc.player.hurtTime > 1)
                    e.setCancelled(true);

                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                    if (wrapper.getEntityID() == mc.player.getEntityId())
                        e.setCancelled(true);
                }
                break;

            case "Cancel":
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                    if (wrapper.getEntityID() == mc.player.getEntityId())
                        e.setCancelled(true);
                }
                if (packet instanceof S27PacketExplosion)
                    e.setCancelled(true);
                break;

            case "Grim":
                break;

            case "Custom":
                this.setSuffix(horizontal.getVal() + "% " + vertical.getVal() + "%");

                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                    if (wrapper.getEntityID() == mc.player.getEntityId()) {
                        S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;

                        velocityPacket.setMotionX((int) (velocityPacket.getMotionX() * (horizontal.getVal() / 100)));
                        velocityPacket.setMotionY((int) (velocityPacket.getMotionY() * (vertical.getVal() / 100)));
                        velocityPacket.setMotionZ((int) (velocityPacket.getMotionZ() * (horizontal.getVal() / 100)));
                    }
                }

                if (packet instanceof S27PacketExplosion)
                    e.setCancelled(true);
                break;

            case "MineMenClub":
                if (this.mmcTicks > 20) {
                    if (packet instanceof S12PacketEntityVelocity) {
                        final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;

                        if (wrapper.getEntityID() == mc.player.getEntityId()) {
                            e.setCancelled(true);
                            this.mmcTicks = 0;
                        }
                    } else if (packet instanceof S27PacketExplosion) {
                        e.setCancelled(true);
                        this.mmcTicks = 0;
                    }
                }
                break;
        }
    };

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "Grim":
                updates++;

                if (reset > 0) {
                    if (updates >= 0 || updates >= reset) {
                        updates = 0;

                        if (grimC > 0)
                            grimC--;
                    }
                }
                break;
            case "Intave":
                if (mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY) && mc.player.hurtTime > 0) {
                    mc.player.motionX *= .6;
                    mc.player.motionZ *= .6;
                }
                break;

            case "Matrix":
                if (mc.player.hurtTime > 0) {
                    mc.player.motionX *= .6;
                    mc.player.motionZ *= .6;
                }
                break;

            case "MineMenClub":
                this.mmcTicks++;
                break;

            case "Legit":
                if (mc.player.hurtTime != 9 || !mc.player.onGround)
                    return;

                mc.player.movementInput.jump = true;
                break;
        }
    };
}
