package dev.lemon.recode.module.impl.movement;

import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.player.MoveUtil;

@ModuleInfo(name = "Flight", key = org.lwjgl.input.Keyboard.KEY_V, category = Category.MOVEMENT, suffix = "Creative")
public class Flight extends Module {
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("funny");
    }
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.isCreativeMode = false;
        System.out.println("2");
    }

    @EventHandler
    public Listener<EventPreMotion> eventPreMotionListener = e -> {
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.capabilities.isCreativeMode = true;
    };
}
