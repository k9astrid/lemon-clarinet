package dev.lemon.client.events;

public class Event2DRender {
    private int width, height;

    public Event2DRender(int width, int height){
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
