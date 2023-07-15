package dev.lemon.api.utils.render;

import dev.lemon.api.utils.IMethods;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RenderUtil implements IMethods {

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

}
