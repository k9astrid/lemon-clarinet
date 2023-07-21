package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;

public class ChestESP extends Module {

    public static ModeSetting mode = new ModeSetting("Mode", "Outline", "Outline");

    public ChestESP() {
        super("Chest ESP", Category.RENDER);
    }
}
