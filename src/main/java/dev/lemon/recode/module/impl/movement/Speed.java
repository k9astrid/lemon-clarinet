package dev.lemon.recode.module.impl.movement;

import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.utils.player.MoveUtil;

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
