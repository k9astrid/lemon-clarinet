package dev.lemon.recode;

import org.lwjgl.opengl.Display;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ChatComponentText;
import dev.lemon.recode.command.CommandManager;
import dev.lemon.recode.module.ModuleManager;
import dev.lemon.recode.utils.Util;

public class Lemon implements Util {
	private static final Lemon instance = new Lemon();
	
	private static final String name = "Lemon", version = "0.1", authors = "Clpz and Eternadox (maybe)"; //name + version + author('s)
    private static final BuildType build = BuildType.Developer;
    
	private static ModuleManager moduleManager;
	private static CommandManager commandManager;
	
	private static final String formattedName = "L" + ChatFormatting.WHITE + "emon";

	public static void start() {
		
		System.out.println(getConsolePrefix() + "Loading Client");
		
		System.out.println("Loaded Custom FontRenderer Util"); // Haven't made a font renderer yet :sob:
		
		Display.setTitle(getFullName() + " | Minecraft 1.8.9" + " | " + build); // sets title
	}
		public static String getConsolePrefix() {
			return "[" + getFullName() + "] : ";
		}
		public static String getFullName() {
			return name + " " + version;
		}
		public static String getFullFormattedName() {
			return formattedName + " " + version;
		}
		public static void addChatMessage(String message) {
			message = ChatFormatting.DARK_BLUE + "[" + ChatFormatting.YELLOW + "Lemon" + ChatFormatting.DARK_BLUE + "]" + ChatFormatting.WHITE + " : " + message; // change this pls	
			mc.thePlayer.addChatMessage(new ChatComponentText(message));
		}
		public static ModuleManager getModuleManager() {
			return moduleManager;
		}
		public static CommandManager getCommandManager() {
			return commandManager;
		}
		
		
		
		private enum BuildType {
		    Release,Beta,Developer
		}
}
