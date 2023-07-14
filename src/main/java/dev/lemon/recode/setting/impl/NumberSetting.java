package dev.lemon.recode.setting.impl;

import dev.lemon.recode.setting.Setting;
import net.minecraft.util.MathHelper;
import optifine.MathUtils;

import java.util.Arrays;

public class NumberSetting extends Setting {
    private double min;
    private double max;
    private double value;

    public NumberSetting(String name, double defaultValue, double min, double max){
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;

    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = MathHelper.clamp_double(value, this.min, this.max);
    }


}
