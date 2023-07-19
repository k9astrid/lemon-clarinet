package dev.lemon.api.bot.network;

import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.util.IChatComponent;

public class BotLoginClient implements INetHandlerLoginClient {

    @Override
    public void onDisconnect(IChatComponent reason) { }

    @Override
    public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn) { }

    @Override
    public void handleLoginSuccess(S02PacketLoginSuccess packetIn) {

    }

    @Override
    public void handleDisconnect(S00PacketDisconnect packetIn) { }

    @Override
    public void handleEnableCompression(S03PacketEnableCompression packetIn) {

    }
}
