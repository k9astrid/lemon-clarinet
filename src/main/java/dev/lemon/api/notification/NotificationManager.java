package dev.lemon.api.notification;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.AnimationUtil;
import dev.lemon.api.utils.render.PostProcessingUtil;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.render.HUD;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements IMethods {
    public List<Notification> notifications = new ArrayList<>();

    public void call(String text, NotificationType type) {
        ScaledResolution sr = new ScaledResolution(mc);
        Notification notification = new Notification(text, type);
        notification.animationUtil = new AnimationUtil(calc(sr.getScaledWidth()) - Fonts.GREYCLIFF_BOLD_22.getStringWidth(notification.text) - 45, calc(sr.getScaledHeight()) + 15);
        this.notifications.add(notification);
    }

    public void draw() {
        notifications.removeIf(n -> System.currentTimeMillis() - n.startTime > 3200L);
        ScaledResolution sr = new ScaledResolution(mc);

        int offset = mc.currentScreen instanceof GuiChat ? 35 : 30;
        for (Notification notification : this.notifications) {
            if (System.currentTimeMillis() - notification.startTime > 3200L)
                continue;

            if (System.currentTimeMillis() - notification.startTime > 3000L) {
                notification.animationUtil.interpolate(calc(sr.getScaledWidth()) - Fonts.GREYCLIFF_BOLD_22.getStringWidth(notification.text) - 45, calc(sr.getScaledHeight()) + 15, (((mc.getDebugFPS() > 0.0F) ? (1.0F / mc.getDebugFPS()) : 1.0F) * 10));
            } else {
                notification.animationUtil.interpolate(calc(sr.getScaledWidth()) - Fonts.GREYCLIFF_BOLD_22.getStringWidth(notification.text) - 45, calc(sr.getScaledHeight()) - offset, (((mc.getDebugFPS() > 0.0F) ? (1.0F / mc.getDebugFPS()) : 1.0F) * 10));
            }

            RenderUtil.drawRound(Math.round(notification.animationUtil.getX()), Math.round(notification.animationUtil.getY()), Fonts.GREYCLIFF_BOLD_22.getStringWidth(notification.text) + 35, 17, 4,
                    new Color(25, 25, 25, 80));

            if (!HUD.optimizeVisuals.isToggled())
                PostProcessingUtil.drawBloom(() -> RenderUtil.drawGradientRound(Math.round(notification.animationUtil.getX()), Math.round(notification.animationUtil.getY()), Fonts.GREYCLIFF_BOLD_22.getStringWidth(notification.text) + 35, 17, 4,
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor4()));

            Fonts.ICON_35.drawString(notification.type.getIcon(), Math.round(notification.animationUtil.getX()) + 5, (Math.round(notification.animationUtil.getY()) + 8) - Fonts.GREYCLIFF_BOLD_22.getHeight() / 2f - 1, Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB());
            Fonts.GREYCLIFF_BOLD_22.drawString(notification.text, Math.round(notification.animationUtil.getX()) + 10 + Fonts.ICON_35.getStringWidth(notification.type.getIcon()), (Math.round(notification.animationUtil.getY()) + 8) - Fonts.GREYCLIFF_BOLD_22.getHeight() / 2f + 3, -1);
            offset += 26;
        }
    }

    public static int calc(int value) {
        ScaledResolution rs = new ScaledResolution(mc);
        return (value * ScaledResolution.getScaleFactor()) / 2;
    }
}
