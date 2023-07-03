package dev.lemon.recode.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUD", key = Keyboard.KEY_O, category = Category.RENDER, suffix = "")
public class HUD extends Module {

    @EventHandler
    public Listener<Event2DRender> event2DRenderListener = e -> {

        mc.fontRendererObj.drawStringWithShadow(lemon.getName().substring(0, 1)+ EnumChatFormatting.WHITE+lemon.getName().substring(1), 5, 5, 0xFFFFFF00);
        int moduleY = 5;
        int spacing = 2;
        int index = 0;
        for (Module m : lemon.getModuleManager().getEnabledSortedModules()){
            mc.fontRendererObj.drawStringWithShadow(m.getName()+(m.getSuffix().isEmpty() ? "" : " ")+EnumChatFormatting.WHITE+m.getSuffix(),e.getWidth() - mc.fontRendererObj.getStringWidth(m.getName()+(m.getSuffix().isEmpty() ? "" : " ")+EnumChatFormatting.WHITE+m.getSuffix()) - 5, moduleY, -1);
            moduleY += mc.fontRendererObj.FONT_HEIGHT+spacing;
            index++;
        }

    };
}
