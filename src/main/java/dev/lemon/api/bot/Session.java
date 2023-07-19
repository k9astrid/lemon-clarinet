package dev.lemon.api.bot;

import com.mojang.authlib.GameProfile;
import dev.lemon.api.bot.network.BotLoginClient;
import dev.lemon.api.bot.network.BotNetwork;
import dev.lemon.api.bot.proxy.Proxy;
import lombok.AllArgsConstructor;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import viamcp.ViaMCP;

import java.net.InetAddress;
import java.util.UUID;

@AllArgsConstructor
public class Session {
    public String username;

    public Proxy proxy;

    public void join() {
        (new Thread(() -> {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.username);
            try {
                BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, this.proxy);
                botNetwork.setNetHandler(new BotLoginClient(botNetwork));
                botNetwork.sendPacket(new C00Handshake(ViaMCP.getInstance().getVersion(), GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN)); //should work with ViaMCP
                botNetwork.sendPacket(new C00PacketLoginStart(gameProfile));
            } catch (Exception ignored) { }
        })).start();
    }
}
