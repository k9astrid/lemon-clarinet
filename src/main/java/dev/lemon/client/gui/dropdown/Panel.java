package dev.lemon.client.gui.dropdown;

import dev.lemon.api.module.Module;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.gui.dropdown.elements.Element;
import dev.lemon.client.gui.dropdown.elements.ElementModule;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel {

    public Module.Category category;

    public float x, y, width;

    public boolean open = true;

    public List<ElementModule> elements = new ArrayList<>();

    public Panel(Module.Category category, float x, float y, float width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;

        for (Module m : Lemon.INSTANCE.getModuleManager().getModulesFromCategory(category))
            elements.add(new ElementModule(m, this));
    }

    public float animated = 0;

    public void draw(int mouseX, int mouseY) {
        float guiOffset = 0;
        if (open)
            for (ElementModule e : elements) {

                if (e.getModule().isExpanded())
                    for (Element element : e.getElements())
                        guiOffset += element.getHeight();

                guiOffset += e.getHeight();
            }

        float finalOffset = Math.min((Minecraft.getMinecraft().displayHeight * .5f), guiOffset);
        animated = (float) RenderUtil.linearAnimation(animated, finalOffset, .9f);
        Gui.drawRect2(x, y, width, 18 + animated, new Color(23, 23, 23, 255).getRGB());

        Fonts.BOLD_18.drawCenteredString(category.getName(), x + width / 2, y + 7, -1);

        Scissoring.push();
        Scissoring.setFromComponentCoordinates(Math.round(x), Math.round(y + 15), Math.round(width), Math.round(animated));
        Gui.drawVerticalGradient(x, y + 16, width, 3, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());
        for (ElementModule e : elements) {
            e.draw(mouseX, mouseY);
        }
        Scissoring.unset();
        Scissoring.pop();
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (open) {
            for (ElementModule e : elements) {
                e.keyPressed(typedChar, keyCode);
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        float offset = animated;
        if (open)
            for (ElementModule e : elements) {
                if (e.getModule().isExpanded())
                    for (Element element : e.getElements())
                        offset += element.getHeight();
                offset += e.getHeight();
            }

        if (isHovering(x, y, width, 16, mouseX, mouseY) && mouseButton == 1)
            open = !open;

        if (open)
            for (ElementModule e : elements)
                if ((offset > Minecraft.getMinecraft().displayHeight * 0.5f && isHovering(x, y + 18, width, Minecraft.getMinecraft().displayHeight * 0.5f, mouseX, mouseY)) || offset <= Minecraft.getMinecraft().displayHeight * 0.5f)
                    e.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (open)
            for (ElementModule e : elements)
                e.mouseReleased(mouseX, mouseY, state);
    }

    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
