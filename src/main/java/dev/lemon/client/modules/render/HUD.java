package dev.lemon.client.modules.render;

import dev.lemon.api.color.Colors;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.PostProcessingUtil;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.other.TickEvent;
import dev.lemon.client.main.ClientEnum;
import dev.lemon.client.main.Lemon;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.utils.player.MoveUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class HUD extends Module {

    public ModeSetting color = new ModeSetting("Color", "Warm",
            "Warm", "Flawless", "Violet", "Cosmic", "Watery", "Fiery", "Bloody",
            "Pleasant", "Light Weight");
    public ModeSetting watermarkStyle = new ModeSetting("Watermark Style", "Default",
            "Default", "Gigabyte");
    public NumberSetting offset = new NumberSetting("Offset", 3, 0, 10, 0.1);
    public static BooleanSetting watermark = new BooleanSetting("Watermark", false);
    public static BooleanSetting toggleNotifications = new BooleanSetting("Toggle Notifications", false);
    public static BooleanSetting optimizeVisuals = new BooleanSetting("Optimize Visuals", false);
    public static BooleanSetting newStyle = new BooleanSetting("New Style", false);

    public HUD() {
        super("HUD", Category.RENDER);
    }

    @Subscribe
    private final IEventListener<TickEvent> onTick = e -> {
        for (Colors colors : Colors.values())
            if (colors.getColorName().equals(color.getMode()))
                Lemon.INSTANCE.getColorManager().setColor(colors);
    };

    @Subscribe
    public final IEventListener<Render2DEvent> onRender2D = e -> {
        if (watermark.isToggled()) {
            switch (watermarkStyle.getMode()) {
                case "Default":
                    if (!newStyle.isToggled())
                        RenderUtil.drawGradientRound(4, 4, 6 + Fonts.SF_16.getStringWidth("Lemon | " + Minecraft.getDebugFPS() + " FPS | v" + Lemon.INSTANCE.getVERSION()), Fonts.SF_16.getHeight() + 7, 4.5f,
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());

                    if (!optimizeVisuals.isToggled())
                        PostProcessingUtil.drawBloom(() -> RenderUtil.drawGradientRound(4, 4, 6 + Fonts.SF_16.getStringWidth("Lemon | " + Minecraft.getDebugFPS() + " FPS | v" + Lemon.INSTANCE.getVERSION()), Fonts.SF_16.getHeight() + 7, 4.5f,
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                                Lemon.INSTANCE.getColorManager().getColor().getGradientColor4()));

                    if (!newStyle.isToggled())
                        RenderUtil.drawRound(5, 5, 6 + Fonts.SF_16.getStringWidth("Lemon | " + Minecraft.getDebugFPS() + " FPS | v" + Lemon.INSTANCE.getVERSION()) - 2, Fonts.SF_16.getHeight() + 7 - 2, 3.5f, new Color(0, 0, 0, 160));
                    else
                        RenderUtil.drawRound(4, 4, 6 + Fonts.SF_16.getStringWidth("Lemon | " + Minecraft.getDebugFPS() + " FPS | v" + Lemon.INSTANCE.getVERSION()), Fonts.SF_16.getHeight() + 7, 3.5f, new Color(0, 0, 0, 160));
                    Fonts.SF_16.drawString("Lemon | " + Minecraft.getDebugFPS() + " FPS | v" + Lemon.INSTANCE.getVERSION(), 7, 8, -1);
                    break;
                case "Gigabyte":
                    if (!optimizeVisuals.isToggled())
                        PostProcessingUtil.drawBloom(() -> {
                            RenderUtil.drawImage(new ResourceLocation("lemon/images/gigabytelogo.png"), 2, 2, 100, 30);
                        });
                    RenderUtil.drawImage(new ResourceLocation("lemon/images/gigabytelogo.png"), 2, 2, 100, 30);
                    break;
            }
        }

        ArrayList<Module> modules = new ArrayList<>();

        for (Module m : Lemon.INSTANCE.getModuleManager().getModulesMap().values()) {
            if (m.isToggled())
                modules.add(m);
        }
        modules.sort(Comparator.<Module>comparingDouble(m -> Fonts.BOLD_18.getStringWidth(m.getDisplayName())).reversed());

        int offsetY = (int) this.offset.getVal() + 1, spacing = 4, offsetX = (int) this.offset.getVal() + 2;

        for (Module m : modules) {
            Gui.drawRect2(e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) - offsetX - 1, offsetY - 2,
                    Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 5, Fonts.BOLD_18.getHeight() + spacing,
                    new Color(0, 0, 0, 120).getRGB());

            Fonts.BOLD_18.drawStringWithShadow(m.getDisplayName(), e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 1 - offsetX, offsetY + 2,
                    Lemon.INSTANCE.getColorManager().getColor().getColor(new Vector2d(e.getWidth() - Fonts.BOLD_18.getStringWidth(m.getDisplayName()) + 1 - offsetX, offsetY)).getRGB());


            offsetY += Fonts.BOLD_18.getHeight() + spacing;
        }
    };

    private void drawBar() {
        String bps = new DecimalFormat("#.##").format(MoveUtil.speed());
        String text = Lemon.INSTANCE.getNAME() + " " + Lemon.INSTANCE.getVERSION() + " | " + "FPS: " + Minecraft.getDebugFPS() + " | " + "BPS: " + bps;

        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, Fonts.BOLD_18.getHeight() + 9, 0x40000000);
        Gui.drawRect(3, 2, Fonts.BOLD_18.getStringWidth(text) + 10, 4,
                Lemon.INSTANCE.getColorManager().getColor().getColor(new Vector2d(3, 2)).getRGB());

        Fonts.BOLD_18.drawString(text, 6, 10, 0xffFFFFFF);
    }
}