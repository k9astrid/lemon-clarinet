package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.EventPreMotion;
import dev.lemon.api.utils.player.MoveUtil;

public class Speed extends Module {
    public Speed() {
        super("Speed", Category.RENDER);
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Subscribe
    public final IEventListener<EventPreMotion> eventPreMotionListener = e -> {
        if(mc.thePlayer.onGround){
            mc.thePlayer.jump();
        }
        MoveUtil.strafe();
    };
}
