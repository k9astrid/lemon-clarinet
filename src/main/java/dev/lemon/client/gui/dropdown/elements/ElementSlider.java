package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ElementSlider extends Element {

    public ElementModule parent;
    public NumberSetting value;

    public float lerp;

    public ElementSlider(ElementModule e, NumberSetting value) {
        this.parent = e;
        this.value = value;
        setHeight(16);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Fonts.BOLD_15.drawString(value.name, x + 2, y + 2, -1);

        float amount = (float) (((float) (value.getVal() - value.getMin()) / (float) (value.getMax() - value.getMin())) / 1.005f);

        lerp = (float) RenderUtil.linearAnimation(lerp, amount, 0.9f);

        if (isHovered(mouseX, mouseY)) {
            if (Mouse.isButtonDown(0))
                value.setValue(round((mouseX - 2 - x) / (parent.width - 4) * (value.getMax() - value.getMin()) + value.getMin(), value.getInc()));
        }

        Gui.drawRect(x + 2, y + 8, x + 2 + parent.width - 4, y + 8 + 1, new Color(15, 15, 15).getRGB());
        Gui.drawRect(x + 2, y + 8, x + 2 + (parent.width - 4) * lerp, y + 8 + 1, new Color(215, 215, 21).getRGB());
        Gui.drawRect(x + 2 + (parent.width - 4) * lerp - 1, y + 7.5f, x + 2 + (parent.width - 4) * lerp - 1 + 2, y + 7.5f + 2, new Color(215, 215, 21).getRGB());

        Fonts.BOLD_15.drawString(String.valueOf(value.getVal()), x + parent.width - Fonts.BOLD_18.getStringWidth(String.valueOf(value.getVal())) - 2, y + 2, -1);
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public boolean isShown() {
        return value.isVisible();
    }
}