package dev.lemon.api.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum NotificationType {
    SUCCESS("s"), ERROR("e"), INFO("i");

    private final String icon;
}
