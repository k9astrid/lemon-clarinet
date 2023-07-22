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
    public ModeSetting mode = new ModeSetting("Mode", "Strafe",
            "Strafe",
            "Intave",
            "MineMenClub",
            "Test",
            "Vulcan",
            "KoksCraft",
            "Debug",
            "Hypixel"
    );
    public ModeSetting cockMode = new ModeSetting("KoksCraft Mode", "Hop", () -> mode.is("KoksCraft"),"Hop", "Low Hop");
    public ModeSetting intaveMode = new ModeSetting("Intave Mode", "Legit Hop", () -> mode.is("Intave"),"Legit Hop", "Fast", "Test", "Test2");
    public ModeSetting vulcanMode = new ModeSetting("Vulcan Mode", "Fast", () -> mode.is("Vulcan"),"Fast", "GroundStrafe", "Strafe");
    public ModeSetting hypixelMode = new ModeSetting("Hypixel Mode", "GroundStrafe", () -> mode.is("Hypixel"), "GroundStrafe", "Strafe", "Fast", "Test");

    public int jumps;
    public double y;
    public int ticks;

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        jumps = 0;
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

            case "KoksCraft":
                switch (cockMode.getMode()) {
                    case "Low Hop":
                        if (mc.player.onGround) {
                            if (mc.player.hurtTime == 0)
                                MoveUtil.setSpeed(MoveUtil.baseSpeed() * .99);

                            mc.player.jump();

                            jumps++;
                        }

                        if (mc.player.offGroundTicks == 1 && mc.player.hurtTime == 0) {
                            double pred = mc.player.motionY;

                            for (int i = 0; i < (jumps % 2 == 0 ? 2 : 4); i++) {
                                pred = (pred - .08) *.98f;
                            }

                            mc.player.motionY = pred;
                        }
                        break;
                }
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
                    case "Test2":
                        if (mc.player.onGround)
                            mc.timer.timerSpeed = 0.1f;
                        else
                            mc.timer.timerSpeed = 2.2f;
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
            case "Debug":
                break;
            case "Hypixel":
                switch (hypixelMode.getMode()) {
                    case "GroundStrafe":
                        if(mc.player.onGround) {
                            mc.timer.timerSpeed = 1;
                        } else {
                            mc.timer.timerSpeed = (float) (1 + Math.random() / 30);
                            if(mc.player.onGround) {
                                ticks = 0;
                                mc.player.jump();
                                MoveUtil.strafe((float) (0.525 - Math.random() / 10));
                            } else {
                                ticks++;
                                mc.player.motionY -= 0.0008;
                                if(ticks == 1) {
                                    mc.player.motionY -= 0.002;
                                }

                                if(ticks == 8) {
                                    mc.player.motionY -= 0.003;
                                }
                            }
                        }
                }
                break;
        }
    };
}
