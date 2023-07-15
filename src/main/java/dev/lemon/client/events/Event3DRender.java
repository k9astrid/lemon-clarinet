package dev.lemon.client.events;

public class Event3DRender {
    private float partialTicks;

    public void Event3DRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

}
