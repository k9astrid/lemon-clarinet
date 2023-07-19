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
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;

import javax.vecmath.Vector2f;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "Intave");
    public ModeSetting intaveMode = new ModeSetting("Intave Mode", "Legit Hop", () -> mode.is("Intave"),"Test", "Legit Hop");

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

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

            case "Intave":
                switch (intaveMode.getMode()) {
                    case "Legit Hop":
                        if (!mc.player.onGround)
                            e.setYaw(mc.player.rotationYaw + 45);

                        mc.player.jumpTicks = 0;
                        mc.timer.timerSpeed = 1.004f;
                        break;

                    case "Test":
                        if (mc.player.onGround)
                            mc.timer.timerSpeed = 1.2f;
                        else
                            mc.timer.timerSpeed = 1.15f;
                        break;
                }
                break;
        }
    };

    @Subscribe
    public final IEventListener<StrafeEvent> onStrafe = e -> {
        switch (mode.getMode()) {
            case "Intave":
                switch (intaveMode.getMode()) {
                    case "Legit Hop":
                        if (mc.player.onGround)
                            mc.player.jump();
                        break;

                    case "Test":
                        if (mc.player.onGround) {
                            mc.player.triggerAchievement(StatList.jumpStat);
                            mc.player.motionY = 0.42f * .78;
                            MoveUtil.strafe(MoveUtil.baseSpeed());
                        }
                        break;
                }
                break;
        }
    };
}
