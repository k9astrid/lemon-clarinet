package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;

public class KeepSprint extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Kokscraft", "Kokscraft");
    public KeepSprint() {
        super("KeepSprint", Category.MOVEMENT);
    }
}
