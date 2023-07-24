package dev.lemon.api.setting.impl;

import dev.lemon.api.setting.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Setter
@Getter
public class TextSetting extends Setting {
    public String text;

    public TextSetting(String name, String text) {
        this.name = name;
        this.text = text;
        this.visible = () -> true;
    }

    public TextSetting(String name, String text, Supplier<Boolean> visible) {
        this.name = name;
        this.text = text;
        this.visible = visible;
    }

}
