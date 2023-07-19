package dev.lemon.api.bot.entity;

import dev.lemon.api.bot.network.BotPlayClient;
import net.minecraft.world.WorldSettings;

public class BotController {
    private int blockHitDelay, currrentPlayItem;

    private final BotPlayClient connection;

    private WorldSettings.GameType gameType;

    public BotController(BotPlayClient botPlayClient) {
        this.gameType = WorldSettings.GameType.SURVIVAL;
        this.connection = botPlayClient;
    }

    private void syncCurrentPlayItem() {

    }
}
