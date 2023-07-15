package dev.lemon.client.gui.dropdown.elements;

import dev.lemon.api.utils.IMethods;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public abstract class Element implements IMethods {

    public double x, y, width, height;
    public double addHeight;
    protected final List<Element> elements = new ArrayList<>();

    public double getHeight() {
        if (!isShown()) {
            return 0;
        }
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean collided(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    public boolean collided(final int mouseX, final int mouseY, double posX, double posY, float width, float height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }

    protected boolean isHovered(double mouseX, double mouseY) {
        double x;
        double y;
        return (mouseX >= (x = getX()) && mouseY >= (y = getY()) && mouseX < x + getWidth() && mouseY < y + getHeight());
    }

    public void handleMouseInput() { }

    public abstract void draw(int mouseX, int mouseY);

    public void mouseClicked(int x, int y, int button) { }

    public boolean isShown() {
        return true;
    }

    public void mouseReleased(int x, int y, int button) { }

    public void keyPressed(char typedChar, int code) { }

}