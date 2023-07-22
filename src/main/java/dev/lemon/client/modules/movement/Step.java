package dev.lemon.client.modules.movement;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.other.StepEvent;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vulcan", "Vulcan");

    public Step() {
        super("Step", Category.MOVEMENT);
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> this.setSuffix(mode.getMode());

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        switch (mode.getMode()) {
            case "Vulcan":
                if (mc.player.ticksSinceJump > 11)
                    mc.player.stepHeight = 1;
                else
                    mc.player.stepHeight = 0.6f;
                break;
        }
    };

    @Subscribe
    private final IEventListener<StepEvent> onStep = e -> {
        switch (mode.getMode()) {
            case "Vulcan":
                if (e.getHeight() > 0.6) {
                    mc.timer.timerSpeed = 0.5f;
                    mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY + .5f, mc.player.posZ, true));
                }
                break;
        }
    };

    @Override
    protected void onDisable() {
        if (mc.player != null)
            mc.player.stepHeight = 0.6f;
    }
}
