package dev.lemon.recode.utils.player;

import dev.lemon.recode.Lemon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void addMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.YELLOW+ Lemon.INSTANCE.getName()+EnumChatFormatting.GRAY+" >> "+EnumChatFormatting.RESET+message);
        mc.thePlayer.addChatMessage(chatComponentText);
    }

    public static void addRawMessage(String message){
        ChatComponentText chatComponentText = new ChatComponentText(message);
        mc.thePlayer.addChatMessage(chatComponentText);
    }

}
