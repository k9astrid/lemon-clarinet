package dev.lemon.client.modules.misc;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.client.events.motion.PreUpdateEvent;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("No Jump Delay", Category.MISC);
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        mc.player.jumpTicks = 0;
    };
}
