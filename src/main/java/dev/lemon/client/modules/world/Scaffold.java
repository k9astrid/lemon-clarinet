package dev.lemon.client.modules.world;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.client.events.motion.PreMotionEvent;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Category.WORLD);
    }

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> { };
}
