package dev.lemon.client.events.other;

import dev.lemon.api.event.CancellableEvent;

public class ChatEvent extends CancellableEvent {
    private String message;

    public ChatEvent(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
