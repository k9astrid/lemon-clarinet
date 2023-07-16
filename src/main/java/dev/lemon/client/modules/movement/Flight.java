package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.EventPreMotion;

public class Flight extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Creative", "Creative", "Vanilla");
    public NumberSetting vanillaSpeed = new NumberSetting("Vanilla Speed", 1, 0, 5, 0.1);

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
        switch (mode.getMode()){
            case "Creative":
                mc.thePlayer.capabilities.isFlying = true;
                mc.thePlayer.capabilities.isCreativeMode = true;
                break;
            case "Vanilla":
                mc.thePlayer.motionY = 0;
                MoveUtil.setSpeed(vanillaSpeed.getVal());
                break;
        }

    };

}
