package dev.lemon.api.script.binding;

import net.minecraft.client.gui.Gui;

public class GuiBinding {

    public void rect(double x, double y, double width, double height, int color) {
        Gui.drawRect2(x, y, width, height, color);
    }

}
