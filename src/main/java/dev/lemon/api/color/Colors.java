package dev.lemon.api.color;

import dev.lemon.api.utils.render.ColorUtil;
import dev.lemon.client.main.Lemon;
import net.minecraft.util.EnumChatFormatting;

import javax.vecmath.Vector2d;
import java.awt.*;

public enum Colors {

    VENOMOUS("Venomous", new Color(58, 255, 156), new Color(68, 191, 255), EnumChatFormatting.AQUA),
    PEACHY("Peachy", new Color(234, 95, 113), new Color(250, 217, 179), EnumChatFormatting.LIGHT_PURPLE),
    SAND_DUNE("Sand Dune", new Color(118, 167, 211), new Color(231, 208, 178), EnumChatFormatting.DARK_BLUE),
    ORANGE_CORAL("Orange Coral", new Color(253, 143, 103), new Color(246, 107, 103), EnumChatFormatting.YELLOW),
    PLUM_PLATE("Plum Plate", new Color(74, 45, 85), new Color(109, 72, 105), EnumChatFormatting.DARK_PURPLE),
    TOXIC("Toxic", new Color(187, 235, 162), new Color(122, 215, 240), EnumChatFormatting.GREEN),
    ORBITAL("Orbital", new Color(85, 114, 251), new Color(139, 222, 249), EnumChatFormatting.BLUE),
    CELESTIAL("Celestial", new Color(178, 55, 99), new Color(47, 40, 109), EnumChatFormatting.DARK_PURPLE),
    MIRROR("Mirror", new Color(156, 170, 208), new Color(221, 229, 229), EnumChatFormatting.GRAY),
    ROCK("Rock", new Color(130, 137, 143), new Color(93, 98, 101), EnumChatFormatting.DARK_GRAY),
    ETERNAL_CONSTANCE("Eternal Constance", new Color(17, 40, 71), new Color(77, 111, 137), EnumChatFormatting.DARK_PURPLE),
    EXOTIC("Exotic", new Color(244, 102, 198), new Color(252, 137, 153), EnumChatFormatting.LIGHT_PURPLE),
    ANTARCTICA("Antarctica", new Color(195, 180, 239), new Color(50, 173, 161), EnumChatFormatting.AQUA),
    PIGLET("Piglet", new Color(234, 160, 173), new Color(251, 213, 217), EnumChatFormatting.LIGHT_PURPLE);

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
        return ColorUtil.interpolateColorsBackAndForth(
                50,
                0,
                Lemon.INSTANCE.getColorManager().getColor().firstColor,
                Lemon.INSTANCE.getColorManager().getColor().secondColor,
                true
        );
    }

    public Color getGradientColor2() {
        return ColorUtil.interpolateColorsBackAndForth(
                50,
                90,
                Lemon.INSTANCE.getColorManager().getColor().firstColor,
                Lemon.INSTANCE.getColorManager().getColor().secondColor,
                true
        );
    }

    public Color getGradientColor3() {
        return ColorUtil.interpolateColorsBackAndForth(
                50,
                180,
                Lemon.INSTANCE.getColorManager().getColor().firstColor,
                Lemon.INSTANCE.getColorManager().getColor().secondColor,
                true
        );
    }

    public Color getGradientColor4() {
        return ColorUtil.interpolateColorsBackAndForth(
                50,
                270,
                Lemon.INSTANCE.getColorManager().getColor().firstColor,
                Lemon.INSTANCE.getColorManager().getColor().secondColor,
                true
        );
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