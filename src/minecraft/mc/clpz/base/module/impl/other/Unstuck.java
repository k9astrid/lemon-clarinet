package mc.clpz.base.module.impl.other;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.awt.*;

import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.bus.Priority;
import mc.clpz.base.event.impl.game.PacketEvent;
import mc.clpz.base.event.impl.player.MotionEvent;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.TimerUtil;
import mc.clpz.base.utils.value.impl.BooleanValue;
import mc.clpz.base.utils.value.impl.NumberValue;

public class Unstuck extends Module {
    private final NumberValue<Long> delay = new NumberValue<>("Delay", 500L, 50L, 1000L, 50L);
    private final BooleanValue automated = new BooleanValue("Automated", true);
    private int setbackCount;
    private TimerUtil timer = new TimerUtil();
    public Unstuck() {
        super("Unstuck", Category.OTHER, new Color(125, 125, 215).getRGB());
    }

    @Handler
    public void packetEvent(PacketEvent e) {
        if (e.isSending()) {
            if (e.getPacket() instanceof C03PacketPlayer) {
                if (isStuck() || !automated.isEnabled()) {
                    e.setCanceled(true);
                }
            }
        } else {

            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                if (!timer.reach(delay.getValue())) {
                    setbackCount++;
                } else {
                    setbackCount = 1;
                }
                timer.reset();
            }
        }
    }

    @Handler(value = Priority.HIGHEST)
    public void moveEvent(MotionEvent e) {
        if (isStuck() || !automated.isEnabled()) {
            e.setX(getMc().thePlayer.motionX = 0);
            e.setY(getMc().thePlayer.motionY = 0);
            e.setZ(getMc().thePlayer.motionZ = 0);
        }
    }

    private boolean isStuck() {
        return setbackCount > 5 && !timer.reach(delay.getValue());
    }
}
