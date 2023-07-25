package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import org.lwjgl.input.Keyboard;

public class BlockAnimation extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "1.7", "1.7");

    public BlockAnimation() {
        super("Block Animations", Category.RENDER, Keyboard.KEY_NONE);
    }
}