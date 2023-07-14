package dev.lemon.recode.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancellableEvent implements Event {
    private boolean cancelled;
}