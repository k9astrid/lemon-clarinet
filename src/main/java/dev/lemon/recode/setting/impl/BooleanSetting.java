package dev.lemon.recode.setting.impl;

import dev.lemon.recode.setting.Setting;

public class BooleanSetting extends Setting {
    private boolean toggled;

    public BooleanSetting(String name, boolean defaultValue){
        super(name);
        this.toggled = defaultValue;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !toggled;
    }
}
