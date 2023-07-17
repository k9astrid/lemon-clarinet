package dev.lemon.client.events.motion;

import dev.lemon.api.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class StrafeEvent extends CancellableEvent {
    private float forward, strafe, friction, yaw;
}
