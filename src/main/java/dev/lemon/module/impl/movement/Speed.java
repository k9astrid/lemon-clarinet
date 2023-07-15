package dev.lemon.module.impl.movement;

import dev.lemon.module.Module;
import dev.lemon.event.IEventListener;
import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventPreMotion;
import dev.lemon.utils.player.MoveUtil;

@Module.Info(name = "Speed", category = Module.Category.MOVEMENT)
public class Speed extends Module {
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
