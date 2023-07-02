package dev.lemon.recode;

import best.azura.eventbus.core.EventBus;

public enum Lemon {
    INSTANCE;

    private final String name = "Lemon Recode", version = "2.0";

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    private final EventBus eventBus = new EventBus();

}
