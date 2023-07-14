package dev.lemon.recode.module.impl.movement;

import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Module;

@Module.Info(name = "Flight", category = Module.Category.MOVEMENT)
public class Flight extends Module {
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
