package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.input.MouseEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SessionInformation extends Module {
    public NumberSetting posX = new NumberSetting("Pos X",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().width / 2, 1, () -> false);
    public NumberSetting posY = new NumberSetting("Pos Y",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().height / 2, 1, () -> false);

    private boolean dragging;
    private double draggingX, draggingY, width, height;
    private final List<String> linesLeft = Arrays.asList("Server", "Name", "Play time");

    public SessionInformation() {
        super("Session Information", Category.RENDER);
    }

    @Subscribe
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        ScaledResolution sr = new ScaledResolution(mc);

        if (this.dragging) {
            if (!(mc.currentScreen instanceof GuiChat)) {
                this.dragging = false;
            } else {
                this.posX.setValue(this.draggingX + ((double) (Mouse.getX() * sr.getScaledWidth()) / mc.displayWidth));
                this.posY.setValue(this.draggingY + (sr.getScaledHeight() - (double) (Mouse.getY() *
                        sr.getScaledHeight()) / mc.displayHeight - 1));
            }
        }

        this.posX.setValue(Math.min(this.posX.getVal(), sr.getScaledWidth() - this.width - 1));
        this.posY.setValue(Math.min(this.posY.getVal(), sr.getScaledHeight() - this.height - 1));
        this.posX.setValue(Math.max(this.posX.getVal(), 1));
        this.posY.setValue(Math.max(this.posY.getVal(), 0.5));

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.posX.getVal(), this.posY.getVal(), 0);
        this.height = linesLeft.size() * (Fonts.GREYCLIFF_BOLD_18.getHeight() + 6) + 13;
        this.width = 138;

        if (!HUD.newStyle.isToggled())
            RenderUtil.drawGradientRound(0, 0, (float) this.width, (float) this.height, 6,
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());

        RenderUtil.drawRound(1, 1, (float) width - 2, (float) height - 2, 5, new Color(0, 0, 0, 160));
        Gui.drawRect2(5, 16, width - 10, .5f, new Color(155, 155, 155).getRGB());

        Fonts.GREYCLIFF_BOLD_222.drawStringWithShadow(this.getName(), 6, 4, -1);

        for (int i = 0; i < linesLeft.size(); i++) {
            int offset = i * (Fonts.SF_16.getHeight() + 4);
            Fonts.SF_16.drawStringWithShadow(linesLeft.get(i), 5, offset + 21, -1);
        }

        Fonts.SF_16.drawString(mc.getCurrentServerData() == null ? "singleplayer" : mc.getCurrentServerData().serverIP, width -
                Fonts.SF_16.getStringWidth(mc.getCurrentServerData() == null ? "singleplayer" : mc.getCurrentServerData().serverIP) - 5, 21, -1);

        Fonts.SF_16.drawString(mc.player.getName(), width -
                Fonts.SF_16.getStringWidth(mc.player.getName()) - 5, 21 + (Fonts.SF_16.getHeight() + 4), -1);

        Fonts.SF_16.drawString(getPlayTime()[0] + "h " + getPlayTime()[1] + "m " + getPlayTime()[2] + "s", width -
                Fonts.SF_16.getStringWidth(getPlayTime()[0] + "h " + getPlayTime()[1] + "m " + getPlayTime()[2] + "s") - 5, 21 + (Fonts.SF_16.getHeight() + 4) * 2, -1);

        GlStateManager.popMatrix();
    };

    @Subscribe
    private final IEventListener<MouseEvent> onMouse = e -> {
        switch (e.getType()){
            case CLICK:
                if(e.getMouseButton() == 0){
                    if(isMouseInBounds(e.getMouseX(), e.getMouseY(),
                            this.posX.getVal(), this.posY.getVal(),
                            this.posX.getVal() + this.width,
                            this.posY.getVal() + this.height)){
                        this.dragging = true;
                        this.draggingX = this.posX.getVal() - e.getMouseX();
                        this.draggingY = this.posY.getVal() - e.getMouseY();
                    }
                }
                break;
            case RELEASED:
                this.dragging = false;
                break;
        }
    };

    public boolean isMouseInBounds(double mouseX, double mouseY, double x, double y, double x1, double y1) {
        return mouseX >= x && mouseX <= x1 && mouseY >= y && mouseY <= y1;
    }

    public static long startTime = System.currentTimeMillis(), endTime = -1;
    public static int[] getPlayTime() {
        long diff = (endTime == -1 ? System.currentTimeMillis() : endTime) - startTime;
        long diffSeconds = 0, diffMinutes = 0, diffHours = 0;
        if (diff > 0) {
            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
        }
        return new int[]{(int) diffHours, (int) diffMinutes, (int) diffSeconds};
    }
}
