package dev.lemon.api.bot.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.util.IChatComponent;

public class BotLoginClient implements INetHandlerLoginClient {
    private final BotNetwork network;

    public BotLoginClient(BotNetwork network) {
        this.network = network;
    }

    @Override
    public void onDisconnect(IChatComponent reason) { }

    @Override
    public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn) { }

    @Override
    public void handleLoginSuccess(S02PacketLoginSuccess packetIn) {
        this.network.setConnectionState(EnumConnectionState.PLAY);
        this.network.setNetHandler(new BotPlayClient(this.network, packetIn.getProfile()));
    }

    @Override
    public void handleDisconnect(S00PacketDisconnect packetIn) { }

    @Override
    public void handleEnableCompression(S03PacketEnableCompression packetIn) {
        if (!this.network.isLocalChannel())
            this.network.setCompressionThreshold(packetIn.getCompressionTreshold());
    }
}
