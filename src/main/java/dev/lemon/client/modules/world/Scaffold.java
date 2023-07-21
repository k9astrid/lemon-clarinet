package dev.lemon.client.modules.world;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.utils.player.MoveUtil;
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
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {

        if (mc.world.getBlockState(new BlockPos(mc.player).add(0, -1, 0)).getBlock() instanceof BlockAir) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());

            mc.playerController.onPlayerRightClick(mc.player, mc.world,
                    mc.player.getCurrentEquippedItem(),
                    new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ),
                    EnumFacing.UP, mc.objectMouseOver.hitVec);
        }
    };
}
