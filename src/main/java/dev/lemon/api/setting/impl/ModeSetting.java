package dev.lemon.api.setting.impl;

import dev.lemon.api.setting.Setting;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting {
    public int index;
    public List<String> modes;
    public String currentMode;

    public ModeSetting(String name, String current, String... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(current);
        this.currentMode = this.modes.get(index);
        this.visible = () -> true;
    }

    public ModeSetting(String name, String current, Supplier<Boolean> visible, String... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(current);
        this.currentMode = this.modes.get(index);
        this.visible = visible;
    }

    public void setMode(String mode) {
        this.currentMode = mode;
        this.index = this.modes.indexOf(mode);
    }

    public String getMode() {
        try {
            return modes.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return modes.get(0);
        }
    }

    public boolean is(String mode) {
        return index == modes.indexOf(mode);
    }

    public void setListMode(String selected) {
        this.currentMode = selected;
        this.index = this.modes.indexOf(selected);
    }

    public List<String> getModes() {
        return modes;
    }

    public void positiveCycle() {
        if (this.index < this.modes.size() - 1) {
            this.index++;
        } else {
            this.index = 0;
        }
    }

    public void negativeCycle() {
        if (this.index <= 0) {
            this.index = this.modes.size() - 1;
        } else {
            this.index--;
        }
    }
}
