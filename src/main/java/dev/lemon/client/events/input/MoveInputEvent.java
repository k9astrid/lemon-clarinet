package dev.lemon.client.events.input;

import lombok.*;

@Getter @Setter @AllArgsConstructor
public class MoveInputEvent {
    private float forward, strafe, sneakSpeed;
    private boolean jump, sneak;
}
