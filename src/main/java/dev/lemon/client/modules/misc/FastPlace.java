package dev.lemon.client.modules.misc;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.client.events.motion.PreMotionEvent;

public class FastPlace extends Module {
    public NumberSetting delay = new NumberSetting("Delay", 3, 0, 3, 1);

    public FastPlace() {
        super("Fast Place", Category.MISC);
    }

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        if (mc.player == null)
            return;

        mc.rightClickDelayTimer = (int) delay.getVal();
    };
}
