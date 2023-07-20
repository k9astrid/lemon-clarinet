package dev.lemon.api.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum NotificationType {
    SUCCESS("o"), ERROR("p"), INFO("m");

    private final String icon;
}
