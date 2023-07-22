package dev.lemon.client.modules.movement;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreMotionEvent;
import net.minecraft.client.settings.KeyBinding;

public class NoSlowdown extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla");
    public NoSlowdown() {
        super("No Slowdown", Category.MOVEMENT);
    }
}
