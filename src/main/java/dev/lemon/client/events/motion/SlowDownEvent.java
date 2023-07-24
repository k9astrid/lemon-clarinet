package dev.lemon.client.events.motion;

import dev.lemon.api.event.CancellableEvent;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class SlowDownEvent extends CancellableEvent {
    private float strafeMultiplier;
    private float forwardMultiplier;
}
