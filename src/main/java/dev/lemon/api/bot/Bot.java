package dev.lemon.api.bot;

import dev.lemon.api.bot.entity.BotController;
import dev.lemon.api.bot.entity.BotPlayer;
import dev.lemon.api.bot.network.BotNetwork;
import dev.lemon.api.bot.network.BotPlayClient;
import dev.lemon.api.bot.world.BotWorld;
import dev.lemon.api.utils.math.TimerUtil;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import org.lwjgl.Sys;

import java.util.Set;

// Made by Luca ;3
public class Bot {
    public TimerUtil timerUtil = new TimerUtil();

    public long systemTime = System.currentTimeMillis();

    public static Set<Bot> bots = new ConcurrentSet<>();

    @Getter
    private final BotController controller;

    @Getter
    private final BotNetwork network;

    @Getter
    private final BotPlayClient connection;

    @Getter
    private final BotPlayer player;

    @Getter
    private final BotWorld world;

    public Bot(BotNetwork network, BotPlayClient playClient, BotController controller, BotPlayer player, BotWorld world) {
        this.network = network;
        this.connection = playClient;
        this.controller = controller;
        this.player = player;
        this.world = world;
    }

}
