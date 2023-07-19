package dev.lemon.api.bot;

import dev.lemon.api.bot.proxy.Proxy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Session {
    public String username;

    public Proxy proxy;

    public void join() {
        (new Thread(() -> {
            try {

            } catch (Exception ignored) { }
        })).start();
    }
}
