package dev.lemon.client.modules.misc;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;

public class MovementCorrection extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Silent", "Silent", "Legit");

    public MovementCorrection() {
        super("Movement Correction", Category.MISC);
    }
}
