package dev.lemon.client.main;

import dev.lemon.api.command.CommandManager;
import dev.lemon.api.module.ModuleManager;
import dev.lemon.api.event.bus.EventBus;
import dev.lemon.api.script.ScriptManager;
import dev.lemon.api.utils.IMethods;
import lombok.Getter;
import microsoft.MicrosoftAuthenticator;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

@Getter
public enum Lemon implements IMethods {
    INSTANCE;

    public final String name = "Lemon";
    public final String version = "0.7";
    public final String authors = "clpz, eternadox, szypko, groszus";
    public final String chatName = "(っ◕‿◕)っ";
    public final ClientEnum clientEnum = ClientEnum.DEVELOPER;

    private EventBus eventBus;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ScriptManager scriptManager;

    private final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator("526b3e37-6aa9-45ef-989f-ed84bfb47f18", "aY78Q~1zman1vukdI.ZzirYvGsWkxY0pjBOLFcEB");

    public void startClient() {
        Display.setTitle(this.name + " " + this.version + "-" + this.clientEnum + " (LWJGL " + Sys.getVersion() + ")");

        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        scriptManager = new ScriptManager();

        moduleManager.initialize();
        commandManager.initialize();
        scriptManager.reload(true);
    }
}
