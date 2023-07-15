package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.client.events.EventPreMotion;

public class Flight extends Module {

    public ModeSetting mode = new ModeSetting("Test Mode", "Test 1", "Test 1", "Test 2", "Test 3");
    public BooleanSetting booleanSetting = new BooleanSetting("Test Boolean", false);
    public NumberSetting numberSetting = new NumberSetting("Test Number", 0, 0, 5, 1);

    public Flight() {
        super("Flight", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.isCreativeMode = false;
    }

    @Subscribe
    public final IEventListener<EventPreMotion> onPreMotion = e -> {
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.capabilities.isCreativeMode = true;
    };

}
