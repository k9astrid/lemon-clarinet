package dev.lemon.recode;

import best.azura.eventbus.core.EventBus;
import org.lwjgl.opengl.Display;

public enum Lemon {
    INSTANCE;

    private final String name = "Lemon Recode", version = "2.0", authors = "clpz & eternadox";

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthors() {
        return authors;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    private final EventBus eventBus = new EventBus();

    public void startClient(){
        Display.setTitle(this.name +" version "+this.version+" by "+this.authors);
    }

}
