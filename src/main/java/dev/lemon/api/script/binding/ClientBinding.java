package dev.lemon.api.script.binding;

import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.main.Lemon;

public class ClientBinding {

    public String getVersion() {
        return Lemon.INSTANCE.getVersion();
    }

    public String getBuild() {
        return Lemon.INSTANCE.getClientEnum().toString();
    }

    public String getAuthors() {
        return Lemon.INSTANCE.getAuthors();
    }


    public void printMsg(String text) {
        ChatUtil.addMessage(text);
    }
}
