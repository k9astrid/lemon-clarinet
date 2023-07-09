package dev.lemon.recode.utils.render;

import java.awt.Color;

public class ColorUtil {

    public static int getRGB(float seconds, float saturation, float brightness, long index) {
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
    private static final int[] HEALTH_COLOURS = {
            0xFF00FF59, // Green
            0xFFFFFF00, // Yellow
            0xFFFF8000, // Orange
            0xFFFF0000, // Red
            0xFF800000 // Dark-red
    };
    public static int clientColour = 0xFFCDFA00;

    public static int getClientColour() {
        return clientColour;
    }

    public static void setClientColour(final int colour) {
        clientColour = colour;
    }

    public static int secondaryColour = 0xFF00E4FF;

    public static int getSecondaryColour() {
        return secondaryColour;
    }

    public static void setSecondaryColour(final int secondColour) {
        secondaryColour = secondColour;
    }

    public static int darker(final int colour, final double factor) {
        final int r = (int) ((colour >> 16 & 0xFF) * factor);
        final int g = (int) ((colour >> 8 & 0xFF) * factor);
        final int b = (int) ((colour & 0xFF) * factor);
        final int a = colour >> 24 & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }

    public static float calculateAverageChannel(final int rgb) {
        final int red = rgb >> 16 & 0xFF;
        final int green = rgb >> 8 & 0xFF;
        final int blue = rgb & 0xFF;
        return Math.max(red, Math.max(green, blue)) / 255.f;
    }

    public static int removeAlphaComponent(final int colour) {
        final int red = colour >> 16 & 0xFF;
        final int green = colour >> 8 & 0xFF;
        final int blue = colour & 0xFF;

        return ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                (blue & 0xFF);
    }

    public static int overwriteAlphaComponent(final int colour, final int alphaComponent) {
        final int red = colour >> 16 & 0xFF;
        final int green = colour >> 8 & 0xFF;
        final int blue = colour & 0xFF;

        return ((alphaComponent & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                (blue & 0xFF);
    }
    public static int astolfoColors(int yOffset, int yTotal) {
        float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 7);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.64F, 1F);
    }

    public static int lemonColors(float seconds, long index) {
        index += 40;
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }
        return getGradientOffset(new Color(0, 255, 13), new Color(255, 234, 1), hue2).getRGB();
    }

    public static int fadeColors(int color1, int color2, float time ){
            if (time > 1.0F)
                time = 1.0F - time % 1.0F;
            double d = (1.0F - time);
            int i = (int)((color1 >> 16 & 0xFF) * d + ((color2 >> 16 & 0xFF) * time));
            int j = (int)((color1 >> 8 & 0xFF) * d + ((color2 >> 8 & 0xFF) * time));
            int k = (int)((color1 & 0xFF) * d + ((color2 & 0xFF) * time));
            int m = (int)((color1 >> 24 & 0xFF) * d + ((color2 >> 24 & 0xFF) * time));
            return (m & 0xFF) << 24 | (i & 0xFF) << 16 | (j & 0xFF) << 8 | k & 0xFF;

    }

    public static int fadeLemonColors(float v){
       return fadeColors(0xFF00ff0d, 0xFFffea01, index);

    }

    public static int fadeColors(int color1, int color2, int index){
        return fadeColors(color1, color2, (float) ((System.currentTimeMillis() + index * 100L) % 1000L) / 500.0f);

    }

    public static int lemonGreenColors(float seconds, long index) {
        index += 40;
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }
        return getGradientOffset(new Color(144, 255, 48, 190), new Color(19, 128, 22), hue2).getRGB();
    }

    public static int lemonColors2(float seconds, long index) {
        index += 40;
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }
        return getGradientOffset(new Color(144, 253, 106), new Color(255, 215, 110), hue2).getRGB();
    }

    public static int customColors(Color c1, Color c2, float seconds, long index) {
        index += 40;
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }

        return getGradientOffset(c1, c2, hue2).getRGB();
    }

}
