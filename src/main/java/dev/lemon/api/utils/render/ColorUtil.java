package dev.lemon.api.utils.render;


import java.awt.*;

public class ColorUtil {

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

    public static int fadeLemonColors(int index){
       return fadeColors(0xFF00ff0d, 0xFFffea01, index);
    }

    public static int fadeColors(int color1, int color2, int index){
        return fadeColors(color1, color2, (float) ((System.currentTimeMillis() + index * 100L) % 1000L) / 500.0f);
    }

    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? ColorUtil.interpolateColorHue(start, end, angle / 360f) : ColorUtil.interpolateColorC(start, end, angle / 360f);
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount),
                interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount),
                interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

}
