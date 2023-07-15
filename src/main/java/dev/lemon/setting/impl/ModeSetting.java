package dev.lemon.setting.impl;

import dev.lemon.setting.Setting;
import java.util.Arrays;
import java.util.function.Supplier;

public class ModeSetting extends Setting {
    private String mode;
    private String[] modes;

    public ModeSetting(String name, String defaultValue, String... modes){
        this.name = name;
        this.mode = defaultValue;
        this.modes = modes;
        this.visible = () -> visible;
    }

    public ModeSetting(String name, Supplier<Boolean> visible, String defaultValue, String... modes){
        this.name = name;
        this.mode = defaultValue;
        this.modes = modes;
        this.visible = visible;
    }

    public String getMode() {
        return mode;
    }

    public String[] getModes(){
        return modes;
    }

    public void setMode(String mode){
        if (Arrays.asList(modes).contains(mode)){
            this.mode = mode;
        }
    }

    public void nextMode(){
        if (Arrays.asList(modes).indexOf(this.mode) == modes.length - 1)
            this.mode = modes[0];
        else
            this.mode = modes[Arrays.asList(modes).indexOf(this.mode) + 1];
    }
    public void previousMode(){
         if (Arrays.asList(modes).indexOf(this.mode) == 0)
            this.mode = modes[modes.length - 1];
         else
            this.mode = modes[Arrays.asList(modes).indexOf(this.mode) - 1];
    }
}
