package dev.lemon.client.modules.combat;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.other.TickEvent;
import java.util.Objects;

public class TickBase extends Module {
    public TickBase(){
        super("Tick Base", Category.COMBAT);
    }

    public NumberSetting tickCap = new NumberSetting("Tick Cap", 5, 1, 20, 1);
    private int ticks;
    @Subscribe
    public IEventListener<TickEvent> onTick = e -> {
            if (Objects.isNull(mc.player)) return;

            if (MoveUtil.moving() && ticks < tickCap.getVal())
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
                mc.runTick();

            }

        } catch (Exception eepy){
            System.out.println("no eepy tonight");
        }
        ChatUtil.send("UBER CRAZY INTAVE FORCEOP 10/10 IM HAZE RATING TICK MANIPULATION COMPLETE!");
        mc.timer.timerSpeed = 1f;

    }
    private boolean shouldTickBase(){

        return KillAura.target.getDistanceToEntity(mc.player) > 3.0D;
    }

}
