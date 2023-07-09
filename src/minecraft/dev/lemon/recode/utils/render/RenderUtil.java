package dev.lemon.recode.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

/*
 *   I took(stole) some code from stackoverflow :sunglasses: - clpz
 */

public class RenderUtil extends GuiScreen {
    public static RenderUtil instance = new RenderUtil();
    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();
    private static final FloatBuffer WND_POS_BUFFER = GLAllocation.createDirectFloatBuffer(4);
    private static final IntBuffer VIEWPORT_BUFFER = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODEL_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final IntBuffer SCISSOR_BUFFER = GLAllocation.createDirectIntBuffer(16);

    public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        setColor(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        int i;
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void glDrawRoundedRect(final double x,
                                         final double y,
                                         final double width,
                                         final double height,
                                         final RoundingMode roundingMode,
                                         final float rounding,
                                         final float scaleFactor,
                                         final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }
    }

        public static double animateProgress(final double current, final double target, final double speed) {
        if (current < target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (target - current < inc) {
                return target;
            } else {
                return current + inc;
            }
        } else if (current > target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (current - target < inc) {
                return target;
            } else {
                return current - inc;
            }
        }

        return current;
    }

    public static double bezierBlendAnimation(double t) {
        return t * t * (3.0 - 2.0 * t);
    }
    public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float borderSize, int borderC, int insideC) {
        drawRoundedRect(x, y, x1, y1, borderSize, borderC);
        drawRoundedRect((x + 0.5F), (y + 0.5F), (x1 - 0.5F), (y1 - 0.5F), borderSize, insideC);
    }
    public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float radius, float borderSize, int borderC, int insideC) {
        drawRoundedRect(x, y, x1, y1, radius, borderC);
        drawRoundedRect((x + borderSize), (y + borderSize), (x1 - borderSize), (y1 - borderSize), radius, insideC);
    }
    public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }
    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
    public static void glColour(final int color) {
        glColor4ub((byte) (color >> 16 & 0xFF),
                (byte) (color >> 8 & 0xFF),
                (byte) (color & 0xFF),
                (byte) (color >> 24 & 0xFF));
    }

    public static void glDrawPlayerFace(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final ResourceLocation skinLocation) {
        // Bind skin texture
        Minecraft.getMinecraft().getTextureManager().bindTexture(skinLocation);
        // Colour solid
        glColor4f(1, 1, 1, 1);
        final float eightPixelOff = 1.0F / 8;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(eightPixelOff, eightPixelOff);
            glVertex2d(x, y);

            glTexCoord2f(eightPixelOff, eightPixelOff * 2);
            glVertex2d(x, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff * 2);
            glVertex2d(x + width, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff);
            glVertex2d(x + width, y);
        }
        glEnd();
    }
    public enum RoundingMode {
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        FULL
    }
}
