package mc.clpz.base.module.impl.player;


import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.awt.*;

import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.impl.player.UpdateEvent;
import mc.clpz.base.module.Module;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Category.PLAYER, new Color(223, 233, 233).getRGB());
        setDescription("Flags you back when falling into the void");
        setRenderLabel("Anti Void");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if(event.isPre()) {
            if (getMc().thePlayer.fallDistance > 2.5) {
                if (getMc().thePlayer.posY < 0) {
                    event.setY(event.getY() + 4.42f);
                } else {
                    for (int i = (int) Math.ceil(getMc().thePlayer.posY); i >= 0; i--) {
                        if (getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ)).getBlock() != Blocks.air) {
                            return;
                        }
                    }
                    event.setY(event.getY() + 4.42f);
                }
            }
        }
    }
}
