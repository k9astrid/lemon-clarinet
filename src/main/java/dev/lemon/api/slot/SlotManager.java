package dev.lemon.api.slot;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.Priority;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.other.SyncCurrentItemEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.entity.player.InventoryPlayer;

public class SlotManager implements IMethods {

    public void initialize() {
        Lemon.INSTANCE.getEventBus().register(this);
    }

    public static void setSlot(final int slot) {
        if (slot < 0 || slot > 8) {
            return;
        }

        mc.player.inventory.alternativeCurrentItem = slot;
        mc.player.inventory.alternativeSlot = true;
    }

    @Subscribe(value = Priority.VERY_HIGH)
    public final IEventListener<SyncCurrentItemEvent> onSyncItem = event -> {
        final InventoryPlayer inventoryPlayer = mc.player.inventory;

        event.setSlot(inventoryPlayer.alternativeSlot ? inventoryPlayer.alternativeCurrentItem : inventoryPlayer.currentItem);
    };


    @Subscribe(value = Priority.VERY_HIGH)
    public final IEventListener<PreUpdateEvent> onPreUpdate = event -> {
        mc.player.inventory.alternativeSlot = false;
    };
}
