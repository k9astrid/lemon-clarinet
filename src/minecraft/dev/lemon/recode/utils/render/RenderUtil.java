package dev.lemon.recode.utils.render;

import dev.lemon.recode.utils.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class RenderUtil implements Util {

    public static void drawImage(ResourceLocation location, float x, float y, int width, int height) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableTexture2D();

        GlStateManager.color(1, 1, 1, 1);

        mc.getTextureManager().bindTexture(location);

        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, width, height, width, height);

        GlStateManager.popMatrix();
    }
}
