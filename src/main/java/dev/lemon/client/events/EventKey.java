package dev.lemon.client.events;

public class EventKey {
    private int keyCode;

    public EventKey(int keyCode){
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
