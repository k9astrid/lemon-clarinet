package dev.lemon.recode.event.impl;

import dev.lemon.recode.event.Event;

public class JumpEvent extends Event {
	
	private double motionY;
	private float yaw;
	
	public JumpEvent(double motionY, float yaw) {
		super(false);
		this.motionY = motionY;
		this.yaw = yaw;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotion(double motionY) {
		this.motionY = motionY;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
}

