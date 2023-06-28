package dev.lemon.recode.event.impl;

import dev.lemon.recode.event.Event;

public class ChatEvent extends Event {
	
	private String message;
	
	public ChatEvent(String message) {
		super(false);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
