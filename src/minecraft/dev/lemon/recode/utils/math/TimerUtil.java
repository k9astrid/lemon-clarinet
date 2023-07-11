package dev.lemon.recode.utils.math;

public class TimerUtil {
	private long lastTime = System.currentTimeMillis();
	private long lastMS = 0L;

	public boolean hasReached(long time) {
		if(System.currentTimeMillis() - lastTime >= time) {
			return true;
		}
		return false;
	}
	
	public long getTimeElapsed() {
		return System.currentTimeMillis() - lastTime;
	}
	public void reset() {
		this.lastMS = this.getCurrentMS();
	}
	public long getCurrentMS() {
		return System.currentTimeMillis();
	}
	public int convertToMS(int perSecond) {
		return 1000 / perSecond;
	}
	public void setCurrentDifference(int difference) {
		this.lastMS = System.currentTimeMillis() - (long)difference;
	}
	public boolean hasTimePassed(long delay) {
		return System.currentTimeMillis() >= this.lastMS + delay;
	}
	public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}
	public boolean hasTimeElapsed(long time, boolean reset) {
		if (this.lastMS > System.currentTimeMillis()) {
			this.lastMS = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - this.lastMS > time) {
			if (reset) {
				this.reset();
			}
			return true;
		}
		return false;
	}
}
