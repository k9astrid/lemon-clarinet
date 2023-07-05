package dev.lemon.recode.module.impl.movement;

import best.azura.eventbus.core.Event;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;

@ModuleInfo(name = "Flight", key = org.lwjgl.input.Keyboard.KEY_V, category = Category.MOVEMENT, suffix = "Fly")
public class Flight extends Module {
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("funny");
    }
    public void onEvent(Event event) {
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.capabilities.isCreativeMode = true;
    }
}
