package dev.lemon.client.modules.render.targethud;

import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.render.RenderUtil;

import java.awt.*;

public class Particle {
    public double x, y, deltaX, deltaY, size, opacity;
    public Color color;

    public void render() {
        RenderUtil.polygon(x, y, size, 360, true, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) opacity));
    }

    public void update() {
        x += deltaX * 2;
        y += deltaY * 2;

        deltaX *= .95;
        deltaY *= .95;

        opacity -= 2f;

        if (opacity < 1)
            opacity = 1;
    }

    public void set(final double x, final double y, final double deltaX, final double deltaY, final double size, final Color color){
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.size = size;
        this.opacity = 254;
        this.color = color;
    }
}
