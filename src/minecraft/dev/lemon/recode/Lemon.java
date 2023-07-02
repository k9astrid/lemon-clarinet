package dev.lemon.recode;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventKey;
import dev.lemon.recode.managers.ModuleManager;
import dev.lemon.recode.module.Module;
import org.lwjgl.opengl.Display;

public enum Lemon {
    INSTANCE;

    private final String name = "Lemon Recode", version = "2.0", authors = "clpz & eternadox";

    private final EventBus eventBus = new EventBus();
    private final ModuleManager moduleManager = new ModuleManager();

    public void startClient(){
        Display.setTitle(this.name +" version "+this.version+" by "+this.authors);
        eventBus.subscribe(this);
        System.out.println("Subscribed to event bus!");
        moduleManager.initialize();
        System.out.println("Initialized module manager!");

    }

    @EventHandler
    public Listener<EventKey> eventKeyListener = e -> {
        for (Module m : moduleManager.getModules()){
            if (m.getKey() == e.getKeyCode()){
                m.toggle();
            }
        }
    };

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

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

}
