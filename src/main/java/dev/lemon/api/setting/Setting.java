package dev.lemon.api.setting;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class Setting {
    public String name;
    protected Supplier<Boolean> visible;

    public boolean isVisible() {
        return visible.get();
    }
}
