package dev.lemon.recode.module.impl.movement;

import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Module;
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
