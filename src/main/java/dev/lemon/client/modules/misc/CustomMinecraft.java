package dev.lemon.client.modules.misc;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;

public class CustomMinecraft extends Module {
    public BooleanSetting smoothChat = new BooleanSetting("Smooth chat", false);

    public CustomMinecraft() {
        super("Custom Minecraft", Category.MISC);
    }
}
