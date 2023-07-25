package dev.lemon.client.modules.movement;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreUpdateEvent;

public class Spider extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vulcan", "Vulcan");

    public int ticks;

    public Spider() {
        super("Spider", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        ticks = 0;
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        this.setSuffix(mode.getMode());

        if (!mc.player.isCollidedHorizontally)
            return;

        switch (mode.getMode()) {
            case "Vulcan":
                if (mc.player.onGround) {
                    ticks = 0;
                    mc.player.jump();
                }

                if (ticks >= 3) {
                    ticks = 0;
                }

                ticks++;

                switch (ticks) {
                    case 2:
                    case 3:
                        mc.player.jump();
                        mc.player.motionX = mc.player.motionZ = 0;
                        break;
                }
                break;
        }
    };
}
