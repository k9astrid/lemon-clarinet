package mc.clpz.base.event.impl.input;

import mc.clpz.base.event.Event;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class KeyInputEvent extends Event {
    private int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
