package dev.lemon.utils.math;

public class TimerUtil {
	private long lastMillis;

	public TimerUtil(){
		this.reset();
	}

	public void reset(){
		this.lastMillis = System.currentTimeMillis();
	}

	public long getElapsedTime(){
		return System.currentTimeMillis() - this.lastMillis;
	}

	public boolean hasTimeElapsed(long millis){
		return getElapsedTime() >= millis;
	}

	public long getLastMillis(){
		return lastMillis;
	}
}
