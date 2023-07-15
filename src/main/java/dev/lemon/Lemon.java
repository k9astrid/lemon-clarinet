package dev.lemon;

import dev.lemon.managers.CommandManager;
import dev.lemon.managers.ModuleManager;
import dev.lemon.event.bus.EventBus;
import dev.lemon.utils.IMethods;
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

    private final EventBus eventBus = new EventBus();
    private final ModuleManager moduleManager = new ModuleManager();
    private final CommandManager commandManager = new CommandManager();

    private final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator("526b3e37-6aa9-45ef-989f-ed84bfb47f18", "aY78Q~1zman1vukdI.ZzirYvGsWkxY0pjBOLFcEB");

    public void startClient() {
        Display.setTitle(this.name + " " + this.version + "-" + this.clientEnum + " (LWJGL " + Sys.getVersion() + ")");

        moduleManager.initialize();
        commandManager.initialize();
        authenticator.login();
    }
}
