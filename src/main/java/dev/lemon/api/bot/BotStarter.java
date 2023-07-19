package dev.lemon.api.bot;

import com.mojang.authlib.GameProfile;
import dev.lemon.api.bot.network.BotLoginClient;
import dev.lemon.api.bot.network.BotNetwork;
import dev.lemon.api.bot.proxy.Proxy;
import dev.lemon.api.bot.proxy.Scraper;
import dev.lemon.api.utils.IMethods;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import viamcp.ViaMCP;

import java.net.InetAddress;
import java.util.UUID;

public class BotStarter implements IMethods {
    public static void run(String name, boolean useProxy, String password) {
        (new Thread(() -> {
            Proxy proxy = useProxy ? Scraper.getProxy() : null;
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
            try {
                BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
                botNetwork.setNetHandler(new BotLoginClient(botNetwork));
                botNetwork.sendPacket(new C00Handshake(ViaMCP.getInstance().getVersion(), GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
                Thread.sleep(400L);
                botNetwork.sendPacket(new C00PacketLoginStart(gameProfile));
            } catch (Exception ignored) { }
        })).start();
    }
}
