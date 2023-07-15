package dev.lemon.module.impl.movement;

import dev.lemon.module.Module;
import dev.lemon.event.IEventListener;
import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventPreMotion;

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
