package dev.lemon.recode.module.impl.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;
import dev.lemon.recode.utils.math.MathUtils;
import dev.lemon.recode.utils.player.MoveUtil;
import dev.lemon.recode.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "HUD", key = Keyboard.KEY_P, category = Category.RENDER, suffix = "")
public class HUD extends Module {
    float funny;
    float bps;

    @EventHandler
    public Listener<Event2DRender> event2DRenderListener = e -> {
        ScaledResolution sr = new ScaledResolution(mc);

        drawLemon();//draws hot thing

        int color = ColorUtil.fadeLemonColors(0);
        int offsetY = 4;
        int spacing = 2;
        int index = 0;
        for (Module m : lemon.getModuleManager().getEnabledSortedModules()) {
            color = ColorUtil.fadeLemonColors(index);
            mc.fontRendererObj.drawStringWithShadow(m.getName() + (m.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + m.getSuffix(), e.getWidth() - mc.fontRendererObj.getStringWidth(m.getName() + (m.getSuffix().isEmpty() ? "" : " ") + EnumChatFormatting.WHITE + m.getSuffix()) - 5, offsetY, color);
            offsetY += mc.fontRendererObj.FONT_HEIGHT + spacing;
            index++;
        }
    };
    private void drawLemon() {

        bps = (float) MathUtils.round(MoveUtil.getSpeed(), 0.01);

        String text = lemon.getName() + " " + lemon.getVersion() + " | " + "FPS: "+mc.getDebugFPS() + " | " + "BPS: " + bps;
        for(int i = 0; i < 2; i++) {
            Gui.drawRect(3, 2, mc.fontRendererObj.getStringWidth(text) + 10, 18, 0x40000000);
        }
        Gui.drawRect(3, 2, mc.fontRendererObj.getStringWidth(text) + 10, 5, ColorUtil.fadeLemonColors(0));
        mc.fontRendererObj.drawString(text, 6, 8, 0xffFFFFFF);
    }
}