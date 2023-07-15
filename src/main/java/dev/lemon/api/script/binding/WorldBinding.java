package dev.lemon.api.script.binding;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.player.MoveUtil;

public class WorldBinding implements IMethods {

    public void setTimer(float speed) {
        mc.timer.timerSpeed = speed;
    }

    public boolean isSinglePlayer() {
        return mc.isSingleplayer();
    }

    public float timer() {
        return mc.timer.timerSpeed;
    }

}