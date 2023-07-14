package dev.lemon.recode.module.impl.render;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.event.impl.Event2DRender;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.utils.player.MoveUtil;
import dev.lemon.recode.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;

@Module.Info(name = "HUD", category = Module.Category.RENDER, autoEnabled = true)
public class HUD extends Module {

    @Subscribe
    public final IEventListener<Event2DRender> event2DRenderListener = e -> {
        ScaledResolution sr = new ScaledResolution(mc);

        drawLemon();//draws hot thing

        int color = ColorUtil.fadeLemonColors(0);
        int offsetY = 4;
        int spacing = 2;
        int index = 0;
        for (Module m : Lemon.INSTANCE.getModuleManager().getEnabledSortedModules()) {
            color = ColorUtil.fadeLemonColors(index);
            mc.fontRendererObj.drawStringWithShadow(m.getDisplayName(), e.getWidth() - mc.fontRendererObj.getStringWidth(m.getDisplayName()) - 5, offsetY, color);
            offsetY += mc.fontRendererObj.FONT_HEIGHT + spacing;
            index++;
        }
    };
    private void drawLemon() {

        String bps = new DecimalFormat("#.##").format(MoveUtil.getSpeed());

        String text = Lemon.INSTANCE.getName() + " " + Lemon.INSTANCE.getVersion() + " | " + "FPS: "+ mc.getDebugFPS() + " | " + "BPS: " + bps;
        Gui.drawRect(3, 2, mc.fontRendererObj.getStringWidth(text) + 10, 18, 0x40000000);

        Gui.drawRect(3, 2, mc.fontRendererObj.getStringWidth(text) + 10, 4, ColorUtil.fadeLemonColors(0));
        mc.fontRendererObj.drawString(text, 6, 8, 0xffFFFFFF);
    }
}