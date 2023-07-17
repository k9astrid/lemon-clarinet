package dev.lemon.client.events.render;

public class Render3DEvent {
    private float partialTicks;

    public void Event3DRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

}
