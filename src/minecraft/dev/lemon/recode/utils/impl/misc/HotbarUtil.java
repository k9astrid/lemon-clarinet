package dev.lemon.recode.utils.impl.misc;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import dev.lemon.recode.utils.Util;
import dev.lemon.recode.utils.impl.packet.PacketUtils;

public class HotbarUtil implements Util {
	
	public static int getAirSlot() {
		for(int i = 0; i < 9; i++) {
			if(mc.thePlayer.inventory.mainInventory[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
}