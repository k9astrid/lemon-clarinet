package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.StrafeEvent;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.modules.combat.KillAura;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe",
            "Strafe",
            "Intave",
            "MineMenClub",
            "Test",
            "Vulcan",
            "KoksCraft",
            "Incognito"
    );
    public ModeSetting cockMode = new ModeSetting("KoksCraft Mode", "Hop", () -> mode.is("KoksCraft"),"Hop", "Low Hop", "Ground", "Ground2");
    public ModeSetting intaveMode = new ModeSetting("Intave Mode", "Legit Hop", () -> mode.is("Intave"),"Legit Hop", "Fast", "Test", "Test2");
    public ModeSetting vulcanMode = new ModeSetting("Vulcan Mode", "Fast", () -> mode.is("Vulcan"),"Fast", "GroundStrafe", "Strafe");

    public int jumps;
    public double y;

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        jumps = 0;
        y = 0;
        mc.gameSettings.keyBindJump.pressed = false;
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

            case "Incognito":
                mc.gameSettings.keyBindJump.pressed = MoveUtil.moving();

                if (mc.player.onGround)
                    MoveUtil.strafe(0.36 + Math.random() / 70);
                else
                    MoveUtil.strafe((MoveUtil.speed()  - (float) (Math.random() - 0.5F) / 70F));

                if (MoveUtil.speed() < 0.25F)
                    MoveUtil.strafe((float) (MoveUtil.speed() + 0.02));
                break;

            case "KoksCraft":
                switch (cockMode.getMode()) {
                    case "Ground2":
                    case "Ground":
                        if (mc.player.isCollidedHorizontally) {
                            return;
                        }

                        if (KillAura.target != null) {
                            y = 0;
                            return;
                        }

                        if (mc.player.onGround) {
                            if (cockMode.is("Ground2"))
                                mc.timer.timerSpeed = 1.11f;

                            y = 0.01;

                            mc.player.motionY = 0.01;
                            MoveUtil.strafe(.418);
                        } else {
                            if (cockMode.is("Ground2"))
                                mc.timer.timerSpeed = .94f;

                            if (y == .01) {
                                MoveUtil.strafe(MoveUtil.baseSpeed() * 1.04f);
                                y = 0;
                            }
                        }
                        break;
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
    public final IEventListener<PacketEvent> onPacket = e -> {
        if (mc.player == null)
            return;

        switch (mode.getMode()) {
            case "KoksCraft":
                if (mc.player.isCollidedHorizontally)
                    return;

                if (cockMode.is("Ground") || cockMode.is("Ground2")) {
                    if(e.getPacket() instanceof C03PacketPlayer) {
                        ((C03PacketPlayer) e.getPacket()).y = mc.player.posY + y;
                    }
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
        }
    };
}
