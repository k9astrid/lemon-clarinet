package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.EventPreMotion;

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
