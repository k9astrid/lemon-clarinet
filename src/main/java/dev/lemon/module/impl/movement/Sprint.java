package dev.lemon.module.impl.movement;

import dev.lemon.module.Module;
import dev.lemon.event.IEventListener;
import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventPreMotion;
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
