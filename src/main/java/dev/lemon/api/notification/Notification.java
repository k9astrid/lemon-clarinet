package dev.lemon.api.notification;

import dev.lemon.api.utils.render.AnimationUtil;

public class Notification {
    public String text;

    public NotificationType type;

    public long startTime;

    public AnimationUtil animationUtil = new AnimationUtil(0, 0);

    public Notification(String text, NotificationType type) {
        this.text = text;
        this.type = type;
        this.startTime = System.currentTimeMillis();
    }
}
