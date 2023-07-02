package mc.clpz.base.event.impl.game;

import mc.clpz.base.event.cancelable.CancelableEvent;

public class ChatEvent extends CancelableEvent {
	private String msg;

	public ChatEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
