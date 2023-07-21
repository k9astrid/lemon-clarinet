package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.other.TickEvent;

public class Brightness extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Gamma", "Gamma");

    public Brightness() {
        super("Brightness", Category.RENDER);
    }

    private float oldGamma;

    @Override
    protected void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000f;
    }

    @Subscribe
    private final IEventListener<TickEvent> onTick = e -> this.setSuffix(mode.getMode());

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
