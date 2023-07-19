package dev.lemon.client.events.motion;

import dev.lemon.api.event.CancellableEvent;
import lombok.*;

@Getter @Setter @AllArgsConstructor
public class JumpEvent extends CancellableEvent {
    private float jumpMotion, yaw;
}
