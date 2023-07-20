package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.motion.StrafeEvent;
import dev.lemon.client.events.other.PacketEvent;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;

import javax.vecmath.Vector2f;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "Intave", "MineMenClub", "Test", "Vulcan");
    public ModeSetting intaveMode = new ModeSetting("Intave Mode", "Legit Hop", () -> mode.is("Intave"),"Legit Hop", "Fast", "FastFall", "Test");
    public ModeSetting vulcanMode = new ModeSetting("Vulcan Mode", "Fast", () -> mode.is("Vulcan"),"Fast", "GroundStrafe", "Strafe");

    public double y;

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "Strafe":
                if (mc.player.onGround)
                    mc.player.jump();

                MoveUtil.strafe();
                break;

            case "MineMenClub":
                if (mc.player.onGround)
                    mc.player.jump();
                break;

            case "Intave":
                switch (intaveMode.getMode()) {
                    case "Legit Hop":
                        if (!mc.player.onGround)
                            e.setYaw(mc.player.rotationYaw);

                        mc.player.jumpTicks = 0;
                        mc.timer.timerSpeed = 1.004f;
                        break;

                    case "Test":
                        if (mc.player.onGround)
                            mc.timer.timerSpeed = 1.2f;
                        else
                            mc.timer.timerSpeed = 1.15f;
                        break;
        
                    case "Fast":
                        if (mc.player.onGround)
                            e.setYaw(mc.player.rotationYaw);

                        mc.player.jumpTicks = 0;
                        mc.timer.timerSpeed = 1.15f;
                        break;
                    case "SlowFall":
                        if (!mc.player.onGround)
                            e.setYaw(mc.player.rotationYaw);

                        if (Math.abs(mc.player.posY) < 0.05) {
                            mc.timer.timerSpeed = 0.3f;
                        }
                        mc.player.jumpTicks = 0;
                        mc.timer.timerSpeed = 3.214f;
                        break;
                }
                break;
            case "Vulcan":
                switch (vulcanMode.getMode()) {
                    case "GroundStrafe":
                        if (mc.player.onGround) {
                            mc.player.jump();
                            MoveUtil.strafe(0.4175f);
                        }
                        break;
                }
                break;
        }
    };

    @Subscribe
    public final IEventListener<StrafeEvent> onStrafe = e -> {
        switch (mode.getMode()) {
            case "MineMenClub":
                if (mc.player.hurtTime >= 6)
                    MoveUtil.strafe();
                break;

            case "Intave":
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
    };
}
