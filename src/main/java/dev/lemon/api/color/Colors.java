package dev.lemon.api.color;

import dev.lemon.api.utils.render.ColorUtil;
import dev.lemon.client.main.Lemon;
import net.minecraft.util.EnumChatFormatting;

import javax.vecmath.Vector2d;
import java.awt.*;

public enum Colors {

    WARM("Warm", new Color(228, 131, 71), new Color(104, 86, 204), EnumChatFormatting.LIGHT_PURPLE),
    FLAWLESS("Flawless", new Color(244, 221, 87), new Color(253, 16, 241), EnumChatFormatting.YELLOW),
    VIOLET("Violet", new Color(190, 187, 238), new Color(107, 61, 157), EnumChatFormatting.DARK_PURPLE),
    COSMIC("Cosmic", new Color(2, 205, 253), new Color(17, 15, 249), EnumChatFormatting.AQUA),
    WATERY("Watery", new Color(108, 167, 242), new Color(11, 59, 212), EnumChatFormatting.BLUE),
    FIERY("Fiery", new Color(244, 208, 60), new Color(253, 15, 89), EnumChatFormatting.RED),
    BLOODY("Bloody", new Color(197, 56, 112), new Color(87, 21, 12), EnumChatFormatting.DARK_RED),
    PLEASANT("Pleasant", new Color(15, 185, 242), new Color(236, 14, 206), EnumChatFormatting.LIGHT_PURPLE),
    LIGHTWEIGHT("Light Weight", new Color(1, 231, 99), new Color(1, 36, 239), EnumChatFormatting.GREEN);

    private final String colorName;
    private Color firstColor, secondColor;
    private final EnumChatFormatting accent;

    Colors(String name, Color first, Color second, EnumChatFormatting chatFormatting) {
        this.colorName = name;
        this.firstColor = first;
        this.secondColor = second;
        this.accent = chatFormatting;
    }

    public Color getColor(Vector2d coords) {
        return ColorUtil.mixColors(getFirstColor(), getSecondColor(), getBlendFactor(coords));
    }

    public double getBlendFactor(Vector2d screenCoordinates) {
        return Math.sin(System.currentTimeMillis() / 600.0D
                + screenCoordinates.getX() * 0.005D
                + screenCoordinates.getY() * 0.06D
        ) * 0.5D + 0.5D;
    }

    public EnumChatFormatting getAccent() {
        return accent;
    }

    public Color getGradientColor1() {
        return ColorUtil.applyOpacity(ColorUtil.interpolateColorsBackAndForth(5, 270, getFirstColor(), getSecondColor(), false), .85f);
    }

    public Color getGradientColor2() {
        return ColorUtil.interpolateColorsBackAndForth(5, 0, getFirstColor(), getSecondColor(), false);
    }

    public Color getGradientColor3() {
        return ColorUtil.interpolateColorsBackAndForth(5, 180, getFirstColor(), getSecondColor(), false);
    }

    public Color getGradientColor4() {
        return ColorUtil.interpolateColorsBackAndForth(5, 90, getFirstColor(), getSecondColor(), false);
    }

    public Color getFirstColor() {
        return firstColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public String getColorName() {
        return colorName;
    }
}