package mc.clpz.base.event.impl.render;

import mc.clpz.base.event.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class Render2DEvent extends Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;

    public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
