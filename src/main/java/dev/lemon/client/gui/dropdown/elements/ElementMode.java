package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.utils.font.Fonts;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ElementMode extends Element {

    public ElementModule parent;
    public ModeSetting value;

    public boolean expanded;

    public ElementMode(ElementModule e, ModeSetting value) {
        this.parent = e;
        this.value = value;
        setHeight(16);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (expanded)
            setHeight((16 + value.modes.size() * 16));
        Fonts.BOLD_15.drawString(value.name, x + 4, y + 5, -1);
        Fonts.BOLD_15.drawString(value.getMode(), x + width - Fonts.BOLD_15.getStringWidth(value.getMode()) - 7, y + 5, new Color(215, 215, 21).getRGB());
        if (expanded) {
            Gui.drawVerticalGradient(x + 2, y + 14, parent.width - 4, 2, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 0).getRGB());
            int offset = 0;
            for (String s : value.getModes()) {
                Fonts.BOLD_15.drawString(s, (float) (x + 5), (float) (y + 21 + offset), value.currentMode.equalsIgnoreCase(s) ? new Color(215, 215, 21).getRGB() : -1);
                offset += 16;
            }
            Gui.drawVerticalGradient(x + 2, y + 14 + offset, parent.width - 4, 2, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 50).getRGB());
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (collided(x, y, getX(), getY(), (float) getWidth(), 16))
            expanded = !expanded;

        if (expanded) {
            int offset = 0;

            for (String s : value.getModes()) {
                if (collided(x, y, getX(), getY() + 16 + offset, (float) getWidth(), 16) && button == 0)
                    value.setListMode(s);
                offset += 16;
            }
        }
    }

    @Override
    public boolean isShown() {
        return value.isVisible();
    }
}