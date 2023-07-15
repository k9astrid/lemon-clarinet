package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.Setting;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.client.gui.dropdown.Panel;
import lombok.*;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Getter @Setter
public class ElementModule extends Element {
    private Module module;
    private Panel panel;

    public boolean binding = false;

    public ElementModule(Module module, Panel panel) {
        this.panel = panel;
        this.module = module;
        setHeight(16);

        for (Setting v : module.getSettings()) {
            if (v instanceof BooleanSetting)
                elements.add(new ElementBoolean(this, (BooleanSetting) v));
            if (v instanceof ModeSetting)
                elements.add(new ElementMode(this, (ModeSetting) v));
            if (v instanceof NumberSetting)
                elements.add(new ElementSlider(this, (NumberSetting) v));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Fonts.BOLD_18.drawCenteredString(binding ? "Binding..." : module.getName(), x + width / 2, y + 5, module.isToggled() ? new Color(215, 215, 21).getRGB() : -1);

        int offset = 0;

        if (module.isExpanded()) {
            if (elements.size() > 0)
                Gui.drawVerticalGradient(x, y + 14, panel.width, 5, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 0).getRGB());

            for (Element e : elements) {
                if (e.isShown()) {
                    e.x = this.x;
                    e.y = this.y + 15.5 + offset;
                    e.width = this.width;
                    e.height = 16;
                    e.draw(mouseX, mouseY);
                    offset += e.getHeight();
                }
            }

            if (elements.size() > 0)
                Gui.drawVerticalGradient(x, y + 8 + offset, panel.width, 5, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 70).getRGB());
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (isHovered(x, y) && button == 1)
            module.setExpanded(!module.isExpanded());

        if (isHovered(x, y) && ((button == 2) || (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && button == 0)))
            binding = true;

        if (isHovered(x, y) && button == 0)
            module.toggle();

        if (module.isExpanded()) {
            for (Element e : elements) {
                if (!e.isShown()) continue;
                e.mouseClicked(x, y, button);
            }
        }
    }

    @Override
    public void keyPressed(char typedChar, int code) {
        super.keyPressed(typedChar, code);
        elements.forEach(e -> e.keyPressed(typedChar, code));

        if (binding) {
            if (code == Keyboard.KEY_ESCAPE) {
                module.setKey(0);
                binding = false;
                return;
            }
            module.setKey(code);
            binding = false;
        }
    }
}
