package dev.lemon.recode.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.impl.Bind;
import dev.lemon.recode.command.impl.Config;
import dev.lemon.recode.command.impl.Say;
import dev.lemon.recode.command.impl.Toggle;
import dev.lemon.recode.event.impl.ChatEvent;

public class CommandManager {
	/*
	 * eye bleeding code made by clpz.
	 * */
	
	public List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		setup();
	}
	
	public void setup() {
		commands.add(new Toggle());
		commands.add(new Say());
		commands.add(new Config());
		commands.add(new Bind());
	}

	public void handleChat(ChatEvent e) {
		String message = e.getMessage();
		
		if(!message.startsWith(prefix)) {
			return;
		}
		
		e.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			Lemon.addChatMessage("Error : Could not find command.");
		}
		
	}
	
}
