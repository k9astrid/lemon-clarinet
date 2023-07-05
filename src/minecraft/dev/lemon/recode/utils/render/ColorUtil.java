package dev.lemon.recode.utils.render;


import dev.lemon.recode.utils.Util;

public class ColorUtil implements Util {

    public static int fadeBetween(int paramInt1, int paramInt2, float paramFloat) {
        if (paramFloat > 1.0F)
            paramFloat = 1.0F - paramFloat % 1.0F;
        double d = (1.0F - paramFloat);
        int i = (int)((paramInt1 >> 16 & 0xFF) * d + ((paramInt2 >> 16 & 0xFF) * paramFloat));
        int j = (int)((paramInt1 >> 8 & 0xFF) * d + ((paramInt2 >> 8 & 0xFF) * paramFloat));
        int k = (int)((paramInt1 & 0xFF) * d + ((paramInt2 & 0xFF) * paramFloat));
        int m = (int)((paramInt1 >> 24 & 0xFF) * d + ((paramInt2 >> 24 & 0xFF) * paramFloat));
        return (m & 0xFF) << 24 | (i & 0xFF) << 16 | (j & 0xFF) << 8 | k & 0xFF;
    }

}
