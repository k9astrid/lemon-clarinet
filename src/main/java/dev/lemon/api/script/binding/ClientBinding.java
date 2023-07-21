package dev.lemon.api.script.binding;

import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.main.Lemon;

public class ClientBinding {

    public String getVersion() {
        return Lemon.INSTANCE.getVERSION();
    }

    public String getBuild() {
        return Lemon.INSTANCE.getCLIENT_ENUM().toString();
    }

    public String getAuthors() {
        return Lemon.INSTANCE.getAUTHORS();
    }


    public void printMsg(String text) {
        ChatUtil.addMessage(text);
    }
}
