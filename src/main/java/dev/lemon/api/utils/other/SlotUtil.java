package dev.lemon.api.utils.other;

import dev.lemon.api.utils.IMethods;
import lombok.experimental.UtilityClass;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class SlotUtil implements IMethods {

    public final List<Block> blacklist = Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest,
            Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch,
            Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser,
            Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock,
            Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner, Blocks.redstone_torch);

    public int findBlock() {
        for (int i = 36; i < 45; i++) {
            final ItemStack item = mc.player.inventoryContainer.getSlot(i).getStack();

            if (item != null && item.getItem() instanceof ItemBlock && item.stackSize > 0) {
                final Block block = ((ItemBlock) item.getItem()).getBlock();

                if ((block.isFullBlock() || block instanceof BlockGlass || block instanceof BlockStainedGlass || block instanceof BlockTNT) && !blacklist.contains(block)) {
                    return i - 36;
                }
            }
        }

        return -1;
    }


}
