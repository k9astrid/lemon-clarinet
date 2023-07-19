package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.motion.StrafeEvent;

import javax.vecmath.Vector2f;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "Intave Legit");

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() { }

    @Subscribe
    public final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        switch (mode.getMode()) {
            case "Intave Legit":
                break;
        }
    };

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "Strafe":
                if (mc.player.onGround) {
                    mc.player.jump();
                }
                MoveUtil.strafe();
                break;

            case "Intave Legit":
                mc.player.jumpTicks = 0;
                mc.timer.timerSpeed = 1.004f;
                break;
        }
    };

    @Subscribe
    public final IEventListener<StrafeEvent> onStrafe = e -> {
        switch (mode.getMode()) {
            case "Intave Legit":
                if (mc.player.onGround)
                    mc.player.jump();
                break;
        }
    };
}
