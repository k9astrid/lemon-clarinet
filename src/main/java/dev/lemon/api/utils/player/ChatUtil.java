package dev.lemon.api.utils.player;

import dev.lemon.client.main.Lemon;
import dev.lemon.api.utils.IMethods;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements IMethods {

    public static void addMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.YELLOW+ Lemon.INSTANCE.getCHAT_PREFIX()+EnumChatFormatting.GRAY+" >> "+EnumChatFormatting.RESET+message);
        mc.player.addChatMessage(chatComponentText);
    }

    public static void addRawMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(message);
        mc.player.addChatMessage(chatComponentText);
    }

}
