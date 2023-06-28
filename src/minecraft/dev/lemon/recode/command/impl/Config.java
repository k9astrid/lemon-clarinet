package dev.lemon.recode.command.impl;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.config.SaveLoad;

import java.io.IOException;

public class Config extends Command {
    public Config() {
        super("Config", "config issue", ".config <args>", "cfg");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 1) {
            String action = args[0];

            if(args[1] == null) return;

            if(action.equalsIgnoreCase("load")) {
                SaveLoad saveLoad1 = new SaveLoad(args[1], false);
                saveLoad1.load(false);
                Lemon.addChatMessage("Loaded config " + args[1]);
            }
            if(action.equalsIgnoreCase("save")) {
                SaveLoad saveLoad2 = new SaveLoad(args[1], false);
                saveLoad2.save();
                Lemon.addChatMessage("Saved config " + args[1]);
            }
            if(action.equalsIgnoreCase("")) {
                SaveLoad saveLoad2 = new SaveLoad("default", false);
                saveLoad2.save();
                Lemon.addChatMessage("Saved config default");
            }
        }
    }
}
