package dev.lemon.api.setting.impl;

import dev.lemon.api.setting.Setting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.MathHelper;

import java.util.function.Supplier;

@Getter
@Setter
public class NumberSetting extends Setting {
    public double val, min, max, inc;

    public NumberSetting(String name, double val, double min, double max, double inc) {
        this.name = name;
        this.val = val;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.visible = () -> true;
        this.save = () -> true;
    }

    public NumberSetting(String name, double val, double min, double max, double inc, Supplier<Boolean> visible) {
        this.name = name;
        this.val = val;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.visible = visible;
        this.save = () -> true;
    }

    public void setValue(double value) {
        double prec = 1 / inc;
        this.val = Math.round(Math.max(min, Math.min(max, value)) * prec) / prec;
    }
}
