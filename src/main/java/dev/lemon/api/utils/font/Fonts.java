package dev.lemon.api.utils.font;

import java.awt.Font;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

//Just a little credit i took this custom font from my client ;3 ~ Luca
public abstract class Fonts {

    public static final CFontRenderer BOLD_28 = new CFontRenderer(Fonts.getFonts("bold.ttf", 28), true, true);
    public static final CFontRenderer BOLD_20 = new CFontRenderer(Fonts.getFonts("bold.ttf", 20), true, true);
    public static final CFontRenderer BOLD_18 = new CFontRenderer(Fonts.getFonts("bold.ttf", 18), true, true);
    public static final CFontRenderer BOLD_17 = new CFontRenderer(Fonts.getFonts("bold.ttf", 17), true, true);
    public static final CFontRenderer BOLD_15 = new CFontRenderer(Fonts.getFonts("bold.ttf", 15), true, true);

    private static Font getFonts(String fontName, int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Lemon/Fonts/" + fontName)).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading " + fontName);
            font = new Font("default", 0, size);
        }
        return font;
    }
}