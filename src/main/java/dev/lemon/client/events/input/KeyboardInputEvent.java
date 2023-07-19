package dev.lemon.client.events.input;

public class KeyboardInputEvent {
    private int keyCode;

    public KeyboardInputEvent(int keyCode){
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
