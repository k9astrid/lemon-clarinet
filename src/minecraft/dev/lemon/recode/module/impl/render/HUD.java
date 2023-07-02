package dev.lemon.recode.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUD", key = Keyboard.KEY_O, category = Category.RENDER, toggled = true)
public class HUD extends Module {

    @EventHandler
    public Listener<Event2DRender> event2DRenderListener = e -> {
        mc.fontRendererObj.drawStringWithShadow(lemon.getName(), 5, 5, -1);

    };
}
