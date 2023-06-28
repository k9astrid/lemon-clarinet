package dev.lemon.recode.setting.impl;

import dev.lemon.recode.module.Module;
import dev.lemon.recode.setting.Setting;

public class BooleanSetting extends Setting {

	public boolean enabled;
	public Module parent;

	public float animState = 0;

	public BooleanSetting(String name, boolean enabled, Module parent) {
		this.name = name;
		this.enabled = enabled;
		this.parent = parent;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void toggle() {
		enabled = !enabled;
	}

	public boolean isOn() {
		return enabled;
	}
	
	public String getName() {
		return name;
	}
	
}
