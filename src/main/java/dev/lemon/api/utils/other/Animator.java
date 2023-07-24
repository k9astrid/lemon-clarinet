package dev.lemon.api.utils.other;

import dev.lemon.client.main.Lemon;
import net.minecraft.client.Minecraft;

public class Animator {
    private float value, min, max, speed, time, alpha;
    private boolean reversed;
    private Easing ease;

    public Animator() {
        this.ease = Easing.LINEAR;
        this.value = 0;
        this.min = 0;
        this.max = 1;
        this.speed = 50;
        this.alpha = 0;
        this.reversed = false;
    }

    public Easing getEase() {
        return ease;
    }

    public void reset() {
        time = min;
    }

    public void resetMax() {
        time = max;
    }

    public float getAlpha() {
        return alpha;
    }

    public Animator update() {
        if (reversed) {
            if (time > min) time -= (Lemon.INSTANCE.getDeltaTime() * .001F * speed);
        } else {
            if (time < max) time += (Lemon.INSTANCE.getDeltaTime() * .001F * speed);
        }
        time = clamp(time, min, max);
        this.value = getEase().ease(time, min, max, max);
        alpha = clamp(time, 0, 255);
        this.alpha = getEase().ease(time, 0, 255, 255);
        return this;
    }

    public float getValue() {
        return value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getSpeed() {
        return speed;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Animator setValue(float value) {
        this.value = value;
        return this;
    }

    public Animator setMin(float min) {
        this.min = min;
        return this;
    }

    public Animator setMax(float max) {
        this.max = max;
        return this;
    }

    public Animator setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Animator setReversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    public Animator setEase(Easing ease) {
        this.ease = ease;
        return this;
    }

    private float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }
}
