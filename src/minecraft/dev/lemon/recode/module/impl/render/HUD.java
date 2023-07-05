package dev.lemon.recode.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.render.ColorUtil;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUD", key = Keyboard.KEY_O, category = Category.RENDER, suffix = "")
public class HUD extends Module {
    int color = ColorUtil.fadeBetween(0xffffff00, 0xff00ffff, (float) ((System.currentTimeMillis()) % 1000L) / 500.0f);
    @EventHandler
    public Listener<Event2DRender> event2DRenderListener = e -> {
        mc.fontRendererObj.drawStringWithShadow(lemon.getName().substring(0, 1)+ EnumChatFormatting.WHITE+lemon.getName().substring(1), 5, 5, color);
        int moduleY = 4;
        int spacing = 2; // customizable
        int index = 0;
        for (Module m : lemon.getModuleManager().getEnabledSortedModules()){
            color = ColorUtil.fadeBetween(0xffffff00, 0xff00ffff, (float) ((System.currentTimeMillis() + index * 100L) % 1000L) / 500.0f);
            mc.fontRendererObj.drawStringWithShadow(m.getName()+(m.getSuffix().isEmpty() ? "" : " ")+EnumChatFormatting.WHITE+m.getSuffix(),e.getWidth() - mc.fontRendererObj.getStringWidth(m.getName()+(m.getSuffix().isEmpty() ? "" : " ")+EnumChatFormatting.WHITE+m.getSuffix()) - 5, moduleY, color);
            moduleY += mc.fontRendererObj.FONT_HEIGHT+spacing;
            index++;
        }
    };
}
