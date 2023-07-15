package dev.lemon.client.modules.render;

import dev.lemon.client.main.Lemon;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.Event2DRender;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.api.utils.render.ColorUtil;
import dev.lemon.api.utils.IMethods;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.text.DecimalFormat;

@Module.Info(name = "HUD", category = Module.Category.RENDER, autoEnabled = true)
public class HUD extends Module {

    @Subscribe
    public final IEventListener<Event2DRender> event2DRenderListener = e -> {
        ScaledResolution sr = new ScaledResolution(IMethods.mc);

        drawLemon();//draws hot thing

        int color = ColorUtil.fadeLemonColors(0);
        int offsetY = 4;
        int spacing = 2;
        int index = 0;
        for (Module m : Lemon.INSTANCE.getModuleManager().getEnabledSortedModules()) {
            color = ColorUtil.fadeLemonColors(index);
            IMethods.mc.fontRendererObj.drawStringWithShadow(m.getDisplayName(), e.getWidth() - IMethods.mc.fontRendererObj.getStringWidth(m.getDisplayName()) - 5, offsetY, color);
            offsetY += IMethods.mc.fontRendererObj.FONT_HEIGHT + spacing;
            index++;
        }
    };
    private void drawLemon() {

        String bps = new DecimalFormat("#.##").format(MoveUtil.getSpeed());

        String text = Lemon.INSTANCE.getName() + " " + Lemon.INSTANCE.getVersion() + " | " + "FPS: "+ IMethods.mc.getDebugFPS() + " | " + "BPS: " + bps;
        Gui.drawRect(3, 2, IMethods.mc.fontRendererObj.getStringWidth(text) + 10, 18, 0x40000000);

        Gui.drawRect(3, 2, IMethods.mc.fontRendererObj.getStringWidth(text) + 10, 4, ColorUtil.fadeLemonColors(0));
        IMethods.mc.fontRendererObj.drawString(text, 6, 8, 0xffFFFFFF);
    }
}