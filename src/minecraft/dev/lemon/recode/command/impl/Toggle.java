package dev.lemon.recode.command.impl;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.module.Module;

public class Toggle extends Command {
	/*
	 * eye bleeding code made by clpz.
	 * */
	
	public Toggle() {
		super("Toggle", "Toggles a module by name", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : Lemon.getModuleManager().getModules()) {
				if(module.getName().equalsIgnoreCase(moduleName)) {
					module.toggle();
					
					Lemon.addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.getName());
					
					foundModule = true;
					break;
				}
			}
			
			if(!foundModule) {
				Lemon.addChatMessage("Error : Could not find module " + moduleName + ".");
			}
			
		}
	}

}
