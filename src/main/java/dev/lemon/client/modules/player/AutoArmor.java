package dev.lemon.client.modules.player;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.utils.math.RandomUtil;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.events.other.TickEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AutoArmor extends Module {
    Map<Integer, Integer> armorSlotMap = new HashMap<>();

    public AutoArmor(){
        super("Auto Armor", Category.PLAYER);
        armorSlotMap.put(0, 3); // slot, armor type (boots)
        armorSlotMap.put(1, 2); // (leggings)
        armorSlotMap.put(2, 1); // (chestplate)
        armorSlotMap.put(3, 0); // (helmet?)

    }




    @Subscribe
    public final IEventListener<TickEvent> onTick = e -> {
        if (mc.player == null)
            return;

        for (int i = 0; i < 4; i++) {
            ItemStack armorPiece = mc.player.getCurrentArmor(i);
            if (armorPiece == null) {
                for (Slot slot : mc.player.inventoryContainer.inventorySlots) {
                    if (slot.getHasStack() && slot.slotNumber > 8) {
                        if (slot.getStack().getItem() instanceof ItemArmor) {
                            ItemArmor armor = (ItemArmor) slot.getStack().getItem();

                            if (armor.armorType == armorSlotMap.get(i)) {
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot.slotNumber, 0, 1, mc.player);
                            }
                        }
                    }
                }
            } else {
                // TODO: Replace armor pieces with less damage reduction
            }
        }
    };
}
