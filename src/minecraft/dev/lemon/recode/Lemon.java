package dev.lemon.recode;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventKey;
import dev.lemon.recode.managers.CommandManager;
import dev.lemon.recode.managers.ModuleManager;
import dev.lemon.recode.module.Module;
import org.lwjgl.opengl.Display;

public enum Lemon {
    INSTANCE;

    private final String name = "Lemon";
    private final String version = "0.7";
    private final String authors = "clpz, eternadox";
    private final String chatName = "(っ◕‿◕)っ";
    private final ClientEnum clientEnum = ClientEnum.DEVELOPER;
    private final EventBus eventBus = new EventBus();
    private final ModuleManager moduleManager = new ModuleManager();
    private final CommandManager commandManager = new CommandManager();

    public void startClient(){
        Display.setTitle(this.name+ " " + this.version + "-" + this.clientEnum);

        eventBus.subscribe(this);
        System.out.println("Subscribed to event bus!");
        moduleManager.initialize();
        System.out.println("Initialized module manager!");
        commandManager.initialize();
        System.out.println("Initialized command manager!");
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
    public String getChatName() {
        return chatName;
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
