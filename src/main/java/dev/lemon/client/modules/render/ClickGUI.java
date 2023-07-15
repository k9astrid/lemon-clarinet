package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("Click Gui", Category.RENDER, Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        //TODO: add dropdown from solar tomorrow
    }
}