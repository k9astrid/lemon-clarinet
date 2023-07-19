package dev.lemon.api.bot.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
@AllArgsConstructor
public class Proxy {
    private final ProxyType type;

    private final InetSocketAddress address;

    public enum ProxyType {
        SOCKS4, SOCKS5, HTTP
    }
}

