package dev.lemon.client.modules.render;

import dev.lemon.api.utils.font.Fonts;
import dev.lemon.client.main.Lemon;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.api.utils.render.ColorUtil;
import dev.lemon.api.utils.IMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.text.DecimalFormat;

public class HUD extends Module {

    public HUD() {
        super("HUD", Category.RENDER);
        this.setAutoEnabled(true);
    }

    @Subscribe
    public final IEventListener<Render2DEvent> onRender2D = e -> {
        ScaledResolution sr = new ScaledResolution(mc);

        drawLemon();//draws hot thing

        int color, offsetY = 4, spacing = 2, index = 0;
        for (Module m : Lemon.INSTANCE.getModuleManager().getModulesMap().values()) {
            if (!m.isToggled())
                continue;

            color = ColorUtil.fadeLemonColors(index);
            Fonts.BOLD_18.drawStringWithShadow(m.getDisplayName(), e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) - 5, offsetY, color);
            offsetY += Fonts.BOLD_18.getHeight() + spacing;
            index++;
        }
    };

    private void drawLemon() {
        String bps = new DecimalFormat("#.##").format(MoveUtil.getSpeed());
        String text = Lemon.INSTANCE.getName() + " " + Lemon.INSTANCE.getVersion() + " | " + "FPS: " + Minecraft.getDebugFPS() + " | " + "BPS: " + bps;

        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, Fonts.BOLD_18.getHeight() + 8, 0x40000000);
        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, 4, ColorUtil.fadeLemonColors(0));

        Fonts.BOLD_18.drawString(text, 6, 8, 0xffFFFFFF);
    }
}