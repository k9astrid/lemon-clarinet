package dev.lemon.client.main;

import dev.lemon.api.bot.BotManager;
import dev.lemon.api.bot.proxy.Scraper;
import dev.lemon.api.color.ColorManager;
import dev.lemon.api.command.CommandManager;
import dev.lemon.api.config.ConfigManager;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.module.ModuleManager;
import dev.lemon.api.event.bus.EventBus;
import dev.lemon.api.notification.NotificationManager;
import dev.lemon.api.script.ScriptManager;
import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.other.ReflectionUtil;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.other.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import me.chaosdave34.microsoftauthenticator.MicrosoftAuthenticator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

@Getter
public enum Lemon implements IMethods {
    INSTANCE;

    public final String NAME = "Lemon";
    public final String VERSION = "0.8";
    public final String AUTHORS = "clpz, eternadox, szypko, groszus";
    public final ClientEnum CLIENT_ENUM = ClientEnum.BETA;

    private EventBus eventBus;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ScriptManager scriptManager;
    private ConfigManager configManager;
    private NotificationManager notificationManager;
    private ColorManager colorManager;
    private BotManager botManager;
    private Scraper scraper;

    @Setter
    private int deltaTime;

    private final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator("526b3e37-6aa9-45ef-989f-ed84bfb47f18", "aY78Q~1zman1vukdI.ZzirYvGsWkxY0pjBOLFcEB");

    public void startClient() {
        Display.setTitle(this.NAME + " " + this.VERSION + " (" + this.CLIENT_ENUM + ")");
        mc.gameSettings.ofFastRender = false;
        mc.gameSettings.fancyGraphics = false;
        mc.gameSettings.guiScale = 2;

        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        scriptManager = new ScriptManager();
        configManager = new ConfigManager();
        notificationManager = new NotificationManager();
        colorManager = new ColorManager();
        scraper = new Scraper();
        botManager = new BotManager();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.stop();
        }));

        // This can be usefull when we gonna use package obfuscator so we can collect every module from it
        String[] paths = {
            "dev.lemon.client"
        };

        for (String path : paths) {
            if (!ReflectionUtil.exist(path))
                continue;

            Class<?>[] classes = ReflectionUtil.getClassesInPackage(path);

            for (Class<?> clazz : classes) {
                try {
                    if (Module.class.isAssignableFrom(clazz) && clazz != Module.class)
                        this.moduleManager.put(clazz, (Module) clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        scraper.initialize();
        moduleManager.initialize();
        configManager.initialize();
        commandManager.initialize();
        botManager.initialize();
        scriptManager.reload(true);
        eventBus.register(new RotationUtil());
        eventBus.register(this);

        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        if (e.getPacket() instanceof S32PacketConfirmTransaction && mc.currentScreen instanceof GuiContainer)
            ((GuiContainer) mc.currentScreen).onServerTransaction((S32PacketConfirmTransaction) e.getPacket());
    };
}
