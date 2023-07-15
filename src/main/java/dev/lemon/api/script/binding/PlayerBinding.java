package dev.lemon.api.script.binding;

import dev.lemon.api.utils.IMethods;

public class PlayerBinding implements IMethods {

    public void rightClick() {
        mc.rightClickMouse();
    }

}
