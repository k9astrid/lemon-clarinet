package dev.lemon.recode.module.impl.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.setting.impl.ModeSetting;
import dev.lemon.recode.utils.player.MoveUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Speed", key = Keyboard.KEY_F, category = Category.MOVEMENT, suffix = "")
public class Speed extends Module {
    private ModeSetting mode = new ModeSetting("Mode", "Test", "Strafe", "Test");
    @Override
    public void onDisable(){
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;

    }

    @EventHandler
    public Listener<EventPreMotion> eventPreMotionListener = e -> {
        this.setSuffix(mode.getMode());
        switch (mode.getMode()) {
            case "Strafe":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                MoveUtil.strafe();
                break;
            case "Test":
                if (mc.thePlayer.onGround){
                    mc.timer.timerSpeed = 1.5f;
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY *= 0.8;

                } else {
                    mc.timer.timerSpeed = 1.15f;
                }

                break;
        }
    };
}
