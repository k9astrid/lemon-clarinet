package dev.lemon.client.commands;

import dev.lemon.client.main.Lemon;
import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.api.module.Module;
import dev.lemon.api.utils.player.ChatUtil;
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
           if (m.getName().equalsIgnoreCase(args[1])){
               m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
               ChatUtil.addMessage("Bound "+m.getName()+" to key "+Keyboard.getKeyName(m.getKey()));
           }
       }

    }
}
