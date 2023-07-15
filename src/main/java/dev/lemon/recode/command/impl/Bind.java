package dev.lemon.recode.command.impl;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.command.CommandInfo;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.utils.player.ChatUtil;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", description = "Binds the specified module to the specified key.")
public class Bind extends Command {

    @Override
    public void onExecute(String[] args){
       if (args.length != 3){
           ChatUtil.addMessage(".bind <module> <key>");
           return;
       }
       for (Module m : Lemon.INSTANCE.getModuleManager().getSortedModules()){
           if (m.getName().replace(" ", "").equalsIgnoreCase(args[1])){
               m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
               ChatUtil.addMessage("Bound "+m.getName()+" to key "+Keyboard.getKeyName(m.getKey()));
           }
       }

    }
}
