package dev.lemon.client.modules.player;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.other.PacketEvent;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vulcan", "Vulcan");

    public boolean vulcan1 = false, vulcan2 = false, nextSpoof = false, doSpoof = false;

    public NoFall() {
        super("No Fall", Category.PLAYER);
    }

    @Override
    protected void onEnable() {
        vulcan1 = vulcan2 = nextSpoof = doSpoof = false;
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onUpdate = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "Vulcan":
                if (!vulcan1 && mc.player.fallDistance > 3.25)
                    vulcan1 = true;

                if (nextSpoof) {
                    mc.player.motionY = -0.1;
                    mc.player.fallDistance = -.1f;
                    MoveUtil.strafe(0.3);
                    nextSpoof = false;
                }

                if (mc.player.fallDistance > 2.8f) {
                    mc.player.fallDistance = 0;
                    doSpoof = true;
                    nextSpoof = true;
                }

                break;
        }
    };

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        switch (mode.getMode()) {
            case "Vulcan":
                if (e.getPacket() instanceof C03PacketPlayer) {
                    if (doSpoof) {
                        ((C03PacketPlayer) e.getPacket()).onGround = true;
                        doSpoof = false;
                        ((C03PacketPlayer) e.getPacket()).y = Math.round(mc.player.posY * 2) / 2;
                        mc.player.setPosition(mc.player.posX, ((C03PacketPlayer) e.getPacket()).y, mc.player.posZ);
                    }
                }
                break;
        }
    };
}
