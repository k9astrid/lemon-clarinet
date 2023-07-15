package dev.lemon.client.events;

import dev.lemon.api.event.CancellableEvent;

public class EventChat extends CancellableEvent {
    private String message;

    public EventChat(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
