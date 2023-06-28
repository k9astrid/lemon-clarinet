package dev.lemon.recode;

import org.lwjgl.opengl.Display;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ChatComponentText;
import dev.lemon.recode.utils.Util;

public class Lemon implements Util {
	private static final Lemon instance = new Lemon();
	
	private static final String name = "Lemon"; //name
	private static final String version = "beta 0.1"; //version
	
	private static final String formattedName = "L" + ChatFormatting.WHITE + "emon"; // going to go on the hud and make it look cool.

	public static void start() {
		System.out.println(getConsolePrefix() + "Loading Client"); // pretty self explanatory
		
		System.out.println("Loaded Custom FontRenderer Util"); // Haven't made a font renderer yet :sob:
		
		Display.setTitle(getFullName() + " | Minecraft 1.8.9"); // sets title
	}
	
		public static String getConsolePrefix() {
			return "[" + getFullName() + "] : "; // prints in the console cool funny text
		}
		
		public static String getFullName() {
			return name + " " + version; //full name, example: Lemon beta 0.2
		}
		
		public static String getFullFormattedName() {
			return formattedName + " " + version; //good for the hud
		}
		
		public static void addChatMessage(String message) {
			message = ChatFormatting.YELLOW + "Lemon" + ChatFormatting.WHITE + " : " + message; // change this pls
			mc.thePlayer.addChatMessage(new ChatComponentText(message));
		}
}
