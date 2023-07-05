package dev.lemon.recode.module.impl.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.player.MoveUtil;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Speed", key = Keyboard.KEY_B, category = Category.MOVEMENT, suffix = "Strafe")
public class Speed extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
    }

    @EventHandler
    public Listener<EventPreMotion> eventPreMotionListener = e -> {
        MoveUtil.setSpeed(0.3F);
        if (mc.thePlayer.onGround)
        mc.thePlayer.jump();
    };
}
