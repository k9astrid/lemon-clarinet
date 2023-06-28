package dev.lemon.recode.event;

import dev.lemon.recode.utils.Util;

public class Event {
	
	private boolean cancelled;
	private boolean canCallOutOfGame;
	
	public Event(boolean canCallOutOfGame) {
		this.canCallOutOfGame = canCallOutOfGame;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public boolean canCallOutOfGame() {
		return canCallOutOfGame;
	}
	
}
