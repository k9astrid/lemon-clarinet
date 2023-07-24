package dev.lemon.client.modules.combat;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.events.other.TickEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.io.IOException;
import java.util.Objects;

public class TickBase extends Module {
    public TickBase(){
        super("Tick Base", Category.COMBAT);
    }

    private int ticks;
    @Subscribe
    public IEventListener<TickEvent> onTick = e -> {
            if (Objects.isNull(mc.player)) return;

            if (MoveUtil.moving() && ticks < 5)
                ticks++;
            else if (ticks > 0)
                ticks--;

            if (MoveUtil.moving() && KillAura.target != null && shouldTickBase()){
                getSomeSleep();
            }
    };

    private void getSomeSleep(){
        try {
            Thread.sleep(ticks * 50L);
            for (int i = 0; i < ticks; i++){

                ChatUtil.addMessage("tick "+i);
                mc.runTick();

            }

        } catch (Exception eepy){
            System.out.println("no eepy tonight");
        }
        ChatUtil.addMessage("UBER CRAZY INTAVE FORCEOP 10/10 IM HAZE RATING TICK MANIPULATION COMPLETE!");
        mc.timer.timerSpeed = 1f;

    }
    private boolean shouldTickBase(){

        return KillAura.target.getDistanceToEntity(mc.player) > 3.0D && (mc.player.hurtTime > 3);
    }

}
