package dev.lemon.client.modules.render;

import dev.lemon.api.color.Colors;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.client.events.other.TickEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.api.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class HUD extends Module {

    //If u have a better way to do it then go ahead im stupid sorry
    public ModeSetting color = new ModeSetting("Color", "Venomous", "Venomous", "Peachy", "Sand Dune",
            "Orange Coral", "Plum Plate", "Toxic", "Orbital", "Celestial", "Mirror", "Rock", "Eternal Constance",
            "Exotic", "Antarctica", "Piglet");
    public NumberSetting offset = new NumberSetting("Offset", 3, 0, 10, 0.1);
    public static BooleanSetting toggleNotifications = new BooleanSetting("Toggle Notifications", false);
    public static BooleanSetting optimizeVisuals = new BooleanSetting("Optimize Visuals", false);

    public HUD() {
        super("HUD", Category.RENDER);
        this.setAutoEnabled(true);
    }

    @Subscribe
    private final IEventListener<TickEvent> onTick = e -> {
        for (Colors colors : Colors.values()) {
            if (colors.getColorName().equals(color.getMode())) {
                Lemon.INSTANCE.getColorManager().setColor(colors);
            }
        }
    };

    @Subscribe
    public final IEventListener<Render2DEvent> onRender2D = e -> {
        drawLemon();

        ArrayList<Module> modules = new ArrayList<>();

        for (Module m : Lemon.INSTANCE.getModuleManager().getModulesMap().values())
            if (m.isToggled())
                modules.add(m);

        modules.sort(Comparator.<Module>comparingDouble(m -> Fonts.BOLD_18.getStringWidth(m.getDisplayName())).reversed());

        int offsetY = (int) this.offset.getVal() + 1, spacing = 3, offsetX = (int) this.offset.getVal() + 2;

        for (Module m : modules) {
            Gui.drawRect2(e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) - offsetX, offsetY - 2, Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 3, Fonts.BOLD_18.getHeight() + spacing, new Color(0,0,0, 80).getRGB());
            Fonts.BOLD_18.drawStringWithShadow(m.getDisplayName(), e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 1 - offsetX, offsetY,
                    Lemon.INSTANCE.getColorManager().getColor().getColor(new Vector2d(e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 1 - offsetX, offsetY)).getRGB());
            offsetY += Fonts.BOLD_18.getHeight() + spacing;
        }
    };

    private void drawLemon() {
        String bps = new DecimalFormat("#.##").format(MoveUtil.speed());
        String text = Lemon.INSTANCE.getName() + " " + Lemon.INSTANCE.getVersion() + " | " + "FPS: " + Minecraft.getDebugFPS() + " | " + "BPS: " + bps;

        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, Fonts.BOLD_18.getHeight() + 9, 0x40000000);
        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, 4,
                Lemon.INSTANCE.getColorManager().getColor().getColor(new Vector2d(3, 2)).getRGB());

        Fonts.BOLD_18.drawString(text, 6, 7, 0xffFFFFFF);
    }
}