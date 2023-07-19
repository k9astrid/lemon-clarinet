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
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "Intave", "MineMenClub");
    public ModeSetting intaveMode = new ModeSetting("Intave Mode", "Legit Hop", () -> mode.is("Intave"),"Test", "Legit Hop");

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

                    case "Legit Fast":
                        if (!mc.player.onGround)
                            e.setYaw(mc.player.rotationYaw + 45);

                        mc.player.jumpTicks = 0;
                        mc.timer.timerSpeed = 1.20438672868002447955033248163757547256375754576812877471296728680024479550332481637575472563757545768128774712672868002447955033248163757547256375754576812877471220438672868002447955033248163757547256375754576812877471296728680024479550332481637575472563757545768128774712672868002447955033248163757547256375754576812877471220438672868002447955033248163757547256375754576812877471296728680024479550332481637575472563757545768128774712672868002447955033248163757547256375754576812877471220438672868002447955033248163757547256375754576812877471296728680024479550332481637575472563757545768128774712672868002447955033248163757547256375754576812877471220438672868002447955033248163757547256375754576812877471296728680024479550332481637575472563757545768128774712672868002447955033248163757547256375754576812877471214159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196442881097566593344612847564823378678316527120190914564856692346034861045432664821339360726024914127372457006606315588174881520920962829254091715364367892590360011330530548820466521384146951941511609433057270365759591953092186117381932611793105118548074462379962749567351885752724891227938183011949129833673362440656643086021394946395224737190702179860943f;
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
