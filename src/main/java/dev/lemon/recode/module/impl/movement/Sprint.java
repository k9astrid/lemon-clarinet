package dev.lemon.recode.module.impl.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Sprint", category = Category.MOVEMENT, suffix = "")
public class Sprint extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
    }

    @EventHandler
    public Listener<EventPreMotion> eventPreMotionListener = e -> {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    };
}
