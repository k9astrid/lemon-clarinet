package dev.lemon.client.events.render;

public class Render2DEvent {
    private int width, height;

    public Render2DEvent(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
