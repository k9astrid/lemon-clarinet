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
        }
    };

    @Subscribe
    private final IEventListener<StepEvent> onStep = e -> {
        switch (mode.getMode()) {
        }
    };

    @Override
    protected void onDisable() {
        if (mc.player != null)
            mc.player.stepHeight = 0.6f;
    }
}
