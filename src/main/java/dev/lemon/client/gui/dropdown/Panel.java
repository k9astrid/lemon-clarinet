package dev.lemon.client.gui.dropdown;

import dev.lemon.api.module.Module;

public class Panel {

    public Module.Category category;
    public float x, y, width;
    public boolean open = true;

    public Panel(Module.Category category, float x, float y, float width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
    }
    
    public void draw(int mouseX, int mouseY) {

    }
}
