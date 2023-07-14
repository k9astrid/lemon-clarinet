package dev.lemon.recode.setting;

public abstract class Setting {
    private String name;
    private boolean visible;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }
}
