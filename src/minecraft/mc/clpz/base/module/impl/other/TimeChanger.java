package mc.clpz.base.module.impl.other;

import com.google.common.eventbus.Subscribe;

import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.impl.game.PacketEvent;
import mc.clpz.base.event.impl.game.TickEvent;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.value.impl.NumberValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.awt.*;

/**
 * made by oHare for oHareWare
 *
 * @since 7/19/2019
 **/
public class TimeChanger extends Module {
    public NumberValue<Long> time = new NumberValue<>("Time", 18400L, 0L, 24000L, 100L);
    public TimeChanger() {
        super("TimeChanger", Category.OTHER, new Color(0x8D9D3C).getRGB());
        setRenderLabel("Time Changer");
        setDescription("Change client-side time.");
    }
}
