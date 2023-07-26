package dev.lemon.client.events.other;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class SyncCurrentItemEvent {
    private int slot;
}