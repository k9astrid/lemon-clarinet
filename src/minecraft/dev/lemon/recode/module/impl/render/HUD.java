package dev.lemon.recode.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.render.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUD", key = Keyboard.KEY_P, category = Category.RENDER, suffix = "")
public class HUD extends Module {

    @EventHandler
    public Listener<Event2DRender> event2DRenderListener = e -> {
        ScaledResolution sr = new ScaledResolution(mc);
        int color = ColorUtil.lemonColors2(1.5F, 6 * -10);
        mc.fontRendererObj.drawStringWithShadow(lemon.getName().substring(0, 1) + EnumChatFormatting.WHITE + lemon.getName().substring(1), 5, 5, color);
        int offsetY = 4;
        int spacing = 2;
        int index = 0;
        for (Module m : lemon.getModuleManager().getEnabledSortedModules()) {
            color = ColorUtil.lemonColors2(1.5F, 6 * -10);
            mc.fontRendererObj.drawStringWithShadow(m.getName() + (m.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + m.getSuffix(), e.getWidth() - mc.fontRendererObj.getStringWidth(m.getName() + (m.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + m.getSuffix()) - 5, offsetY, color);
            offsetY += mc.fontRendererObj.FONT_HEIGHT + spacing;
            index++;
            };
    };
}