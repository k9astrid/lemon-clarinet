package dev.lemon.api.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancellableEvent implements Event {
    private boolean cancelled;
}