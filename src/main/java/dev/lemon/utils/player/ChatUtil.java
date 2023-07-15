package dev.lemon.utils.player;

import dev.lemon.Lemon;
import dev.lemon.utils.IMethods;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements IMethods {

    public static void addMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.YELLOW+ Lemon.INSTANCE.getChatName()+EnumChatFormatting.GRAY+" >> "+EnumChatFormatting.RESET+message);
        mc.thePlayer.addChatMessage(chatComponentText);
    }

    public static void addRawMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(message);
        mc.thePlayer.addChatMessage(chatComponentText);
    }

}
