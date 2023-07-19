package dev.lemon.client.events.input;

import lombok.*;

@Getter @Setter @AllArgsConstructor
public class MoveInputEvent {
    private float forward, strafe;
    private boolean jump, sneak;
}
