package dev.lemon.api.utils.render;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.shader.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class RenderUtil implements IMethods {
    public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
    public static ShaderUtil roundedOutlineShader = new ShaderUtil("roundRectOutline");

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

    public static ByteBuffer[] loadIcon(String filepath)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(filepath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = loadIconInstance(image, 32);
        buffers[1] = loadIconInstance(image, 16);
        return buffers;
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension)
    {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio = 1;
        if(image.getWidth() > scaledIcon.getWidth())
        {
            ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        }
        else
        {
            ratio = scaledIcon.getWidth() / image.getWidth();
        }
        if(image.getHeight() > scaledIcon.getHeight())
        {
            double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
            if(r2 < ratio)
            {
                ratio = r2;
            }
        }
        else
        {
            double r2 = scaledIcon.getHeight() / image.getHeight();
            if(r2 < ratio)
            {
                ratio = r2;
            }
        }
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) (width), (int) (height), null);
        g.dispose();

        byte[] imageBuffer = new byte[dimension*dimension*4];
        int counter = 0;
        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter] =(byte)((colorSpace << 8) >> 24 );
                imageBuffer[counter + 1] =(byte)((colorSpace << 16) >> 24 );
                imageBuffer[counter + 2] =(byte)((colorSpace << 24) >> 24 );
                imageBuffer[counter + 3] =(byte)(colorSpace >> 24 );
                counter += 4;
            }
        }
        return ByteBuffer.wrap(imageBuffer);
    }

    public static double linearAnimation(double now, double desired, double speed) {
        double dif = Math.abs(now - desired);

        final int fps = Minecraft.getDebugFPS();

        if (dif > 0) {
            double animationSpeed = roundToDecimalPlace(Math.min(
                    10.0D, Math.max(0.05D, (144.0D / fps) * (dif / 10) * speed)), 0.05D);

            if (dif != 0 && dif < animationSpeed)
                animationSpeed = dif;

            if (now < desired)
                return now + animationSpeed;
            else if (now > desired)
                return now - animationSpeed;
        }

        return now;
    }

    public static double roundToDecimalPlace(double value, double inc) {
        final double halfOfInc = inc / 2.0D;
        final double floored = StrictMath.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return new BigDecimal(StrictMath.ceil(value / inc) * inc, MathContext.DECIMAL64)
                    .stripTrailingZeros()
                    .doubleValue();
        else
            return new BigDecimal(floored, MathContext.DECIMAL64)
                    .stripTrailingZeros()
                    .doubleValue();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedShader.init();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformf("blur", 0);
        roundedShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedOutlineShader.init();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);

        ShaderUtil.drawQuads(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness * 2), height + (4 + outlineThickness * 2));
        roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawRoundCircle(float x, float y, float radius, Color color) {
        drawRound(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, color);
    }
}
