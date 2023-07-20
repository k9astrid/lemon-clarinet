package dev.lemon.api.utils.render;

import lombok.Getter;

@Getter
public class AnimationUtil {
    private double x, y;

    public AnimationUtil(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void interpolate(double targetX, double targetY, double smoothing) {
        this.x = animate(targetX, this.x, smoothing);
        this.y = animate(targetY, this.y, smoothing);
    }

    public void animate(double newX, double newY) {
        this.x = animate(this.x, newX, 1.0D);
        this.y = animate(this.y, newY, 1.0D);
    }

    public double animate(double target, double current, double speed) {
        boolean larger = (target > current);
        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1D)
            factor = 0.1D;
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }
}
