package dev.lemon.recode.utils.impl.player;

import dev.lemon.recode.utils.Util;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;

public class PlayerUtils implements Util {
	
	public static boolean isUsingItem() {
		return mc.thePlayer.isUsingItem();
	}
	
	public static boolean isUsingSword() {
		return mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
	}
	
}