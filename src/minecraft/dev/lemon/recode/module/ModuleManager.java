package dev.lemon.recode.module;

import java.util.ArrayList;

							//import categories later when u make a module or me LOL

public class ModuleManager {
	
	private final ArrayList<Module> modules = new ArrayList<>();
	
	public ModuleManager() {
		//Combat
		
		//Movement
		
		//Player
		
		//World
		
		//Render
		
		//Exploit
		
		//Ghost
		
		//Misc
		
		//Setting
	}

	public ArrayList<Module> getModules() {
		return modules;
	}
	
	public Module getModuleByName(String name) {
		for(Module m : modules) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}
	
}