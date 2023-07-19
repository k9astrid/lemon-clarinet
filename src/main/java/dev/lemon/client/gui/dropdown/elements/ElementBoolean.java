package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;

import java.awt.*;

public class ElementBoolean extends Element {

    public ElementModule parent;
    public BooleanSetting value;

    public float lerp;

    public ElementBoolean(ElementModule e, BooleanSetting value) {
        this.parent = e;
        this.value = value;
        setHeight(16);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Fonts.BOLD_15.drawString(value.name, x + 4, y + 4, -1);
        lerp = (float) RenderUtil.linearAnimation(lerp, value.isToggled() ? 6 : 0, 0.9f);
        RenderUtil.drawRound((float) (x + parent.width - 16), (float) (y + 2), 12, 6, 3, value.isToggled() ? new Color(0, 0, 0, 50) :  new Color(0, 0, 0, 100));
        //RoundUtil.drawRoundCircle((float) (x + parent.width - 13 + lerp), (float) (y + 5), 3, value.isToggled() ? Color.black : Color.white);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (isHovered(x, y))
            value.toggle();
    }

    @Override
    public boolean isShown() {
        return value.isVisible();
    }
}