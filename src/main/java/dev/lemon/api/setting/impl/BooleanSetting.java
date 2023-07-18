package dev.lemon.api.setting.impl;

import dev.lemon.api.setting.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {
    private boolean toggled;

    public BooleanSetting(String name, boolean val) {
        this.name = name;
        this.toggled = val;
        this.visible = () -> true;
    }

    public BooleanSetting(String name, boolean val, Supplier<Boolean> visible) {
        this.name = name;
        this.toggled = val;
        this.visible = visible;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
}
