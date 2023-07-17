package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import net.minecraft.client.settings.KeyBinding;

import javax.vecmath.Vector2f;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Subscribe
    public final IEventListener<PreMotionEvent> eventPreMotionListener = e -> {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    };
}
