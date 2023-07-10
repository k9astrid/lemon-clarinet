package dev.lemon.recode.utils.render;

import java.awt.Color;

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


}
