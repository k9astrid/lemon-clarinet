package dev.lemon.api.utils.player;

import dev.lemon.client.main.Lemon;
import dev.lemon.api.utils.IMethods;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements IMethods {

    public static void send(String message){
        ChatComponentText chatComponentText = new ChatComponentText(getPrefix() + message);
        mc.player.addChatMessage(chatComponentText);
    }

    private static String getPrefix() {
        final String color = Lemon.INSTANCE.getColorManager().getColor().getAccent().toString();
        return EnumChatFormatting.BOLD + color + Lemon.INSTANCE.NAME
                + EnumChatFormatting.RESET + color + " » "
                + EnumChatFormatting.RESET;
    }
}
