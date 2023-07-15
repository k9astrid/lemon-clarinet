package dev.lemon.client.gui.dropdown;

import dev.lemon.api.module.Module;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickScreen extends GuiScreen {

    public List<Panel> panels = new ArrayList<>();

    public ClickScreen() {
        int x = 0;

        for (Module.Category category : Module.Category.values()) {
            panels.add(new Panel(category, 20 + x, 10, 100));
            x += 115;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        panels.forEach(panel -> panel.draw(mouseX, mouseY));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }
}
