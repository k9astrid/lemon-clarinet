package dev.lemon.api.setting;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class Setting<T> {
    public String name;
    protected Supplier<Boolean> visible;
}
