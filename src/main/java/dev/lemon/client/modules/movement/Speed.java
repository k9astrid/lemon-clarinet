package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.utils.player.MoveUtil;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "Intave Legit");


    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Subscribe
    public final IEventListener<PreMotionEvent> eventPreMotionListener = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "Strafe":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                MoveUtil.strafe();
                break;

            case "Intave Legit":

                break;
        }
    };
}
