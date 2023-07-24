package dev.lemon.client.modules.movement;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PostMotionEvent;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.SlowDownEvent;
import dev.lemon.client.events.other.TeleportEvent;
import dev.lemon.client.modules.combat.KillAura;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla",
            "Vanilla",
            "NCP",
            "New NCP");

    private int ticks;

    public NoSlowdown() {
        super("No Slowdown", Category.MOVEMENT);
    }

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {
            case "NCP":
                if (mc.player.isUsingItem())
                    mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                break;

            case "New NCP":
                this.ticks++;

                if (mc.player.isUsingItem() && this.ticks > 10 && KillAura.target == null) {
                    mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.player.inventory.currentItem % 8 + 1));
                    mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.player.inventory.currentItem));
                }
                break;
        }
    };

    @Subscribe
    private final IEventListener<PostMotionEvent> onPostMotion = e -> {
        switch (mode.getMode()) {
            case "NCP":
                if (mc.player.isUsingItem())
                    mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.player.inventoryContainer.getSlot(mc.player.inventory.currentItem + 36).getStack()));
                break;
        }
    };

    @Subscribe
    private final IEventListener<TeleportEvent> onTeleport = e -> {
        this.ticks = 0;
    };

    @Subscribe
    private final IEventListener<SlowDownEvent> onSlowDown = e -> {
        e.setCancelled(mode.is("Vanilla") || mode.is("NCP") || mode.is("New NCP") && KillAura.target == null);
    };
}
