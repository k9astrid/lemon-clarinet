package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.EventPreMotion;
import net.minecraft.client.settings.KeyBinding;

@Module.Info(name = "Sprint", category = Module.Category.MOVEMENT)
public class Sprint extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Subscribe
    public final IEventListener<EventPreMotion> eventPreMotionListener = e -> {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    };
}
