package dev.lemon.recode.utils.impl.server;

import dev.lemon.recode.utils.Util;

public class ServerUtils implements Util {
	
	public static boolean isOnHypixel() {
		if(mc.getCurrentServerData() == null || mc.thePlayer == null)
			return false;
		return mc.getCurrentServerData().serverIP.contains("hypixel.net");
	}
}