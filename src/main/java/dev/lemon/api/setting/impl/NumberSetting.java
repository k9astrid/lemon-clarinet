package dev.lemon.api.setting.impl;

import dev.lemon.api.setting.Setting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.MathHelper;

import java.util.function.Supplier;

@Getter
@Setter
public class NumberSetting extends Setting {
    private double min;
    private double max;
    private double value;

    public NumberSetting(String name, double defaultValue, double min, double max) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.visible = () -> true;
    }

    public NumberSetting(String name, double defaultValue, double min, double max, Supplier<Boolean> visible) {
        this.name = name;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.visible = visible;
    }

    public void setValue(double value) {
        this.value = MathHelper.clamp_double(value, this.min, this.max);
    }
}
