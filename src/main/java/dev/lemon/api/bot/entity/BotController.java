package dev.lemon.api.bot.entity;

import dev.lemon.api.bot.network.BotPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
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
        int slot = (this.connection.getBot()).inventory.currentItem;

        if (slot != this.currrentPlayItem) {
            this.currrentPlayItem = slot;
            this.connection.sendPacket(new C09PacketHeldItemChange(this.currrentPlayItem));
        }
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.gameType = gameType;
        this.gameType.configurePlayerCapabilities((this.connection.getBot()).capabilities);
    }

    private void updateController() {
        syncCurrentPlayItem();

        if (this.connection.getNetwork().isChannelOpen())
            this.connection.getNetwork().tick();
    }

    public boolean isSpectator() {
        return (this.gameType == WorldSettings.GameType.SPECTATOR);
    }

    public void flipPlayer(EntityPlayer player) {
        player.rotationYaw = -180;
    }
}
