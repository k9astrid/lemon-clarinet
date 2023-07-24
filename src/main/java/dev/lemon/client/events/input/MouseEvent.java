package dev.lemon.client.events.input;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MouseEvent {
    private int mouseX, mouseY, mouseButton;
    private Type type;

    public enum Type {
        CLICK,
        CLICK_MOVE,
        RELEASED,
        NO_SCREEN
    }
}