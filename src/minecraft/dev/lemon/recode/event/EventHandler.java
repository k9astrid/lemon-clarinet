package dev.lemon.recode.event;

import dev.lemon.recode.event.impl.ChatEvent;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class EventHandler {
	
	public static void onEvent(Event e) {
		if(e instanceof ChatEvent) {
			ChatEvent event = (ChatEvent) e;
		}
	}
}
