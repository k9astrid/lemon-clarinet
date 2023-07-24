package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.setting.impl.TextSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ElementText extends Element {

    public ElementModule parent;
    public TextSetting value;
    public boolean typing;

    public ElementText(ElementModule e, TextSetting value) {
        this.parent = e;
        this.value = value;
        setHeight(16);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        setHeight(16 + 8);
        Fonts.BOLD_16.drawString(value.name, x + 4, y + 3, -1);
        RenderUtil.drawRound((float) (x + 2), (float) (y + 9), (float) (width - 4), 10, 1, new Color(10, 10, 10, 255));
        Fonts.BOLD_12.drawString(Fonts.BOLD_12.trimStringToWidth(value.text, (int) (width - 15), true), x + 4, y + 14, -1);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);

        if (isHovered(x, y))
            typing = !typing;
    }

    @Override
    public void keyPressed(char typedChar, int code) {
        super.keyPressed(typedChar, code);

        if (typing) {
            Keyboard.enableRepeatEvents(true);

            System.out.println(typedChar);
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
                value.text += typedChar;

            if (code == Keyboard.KEY_BACK) {
                if (value.text.length() > 0) {
                    value.text = value.text.substring(0, value.text.length() - 1);
                }
            }
        }

        if (code == Keyboard.KEY_RETURN)
            typing = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
            value.text += GuiScreen.getClipboardString();
        }
    }

    @Override
    public boolean isShown() {
        return value.isVisible();
    }
}