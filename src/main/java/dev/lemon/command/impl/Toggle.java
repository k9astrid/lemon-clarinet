package dev.lemon.command.impl;

import dev.lemon.Lemon;
import dev.lemon.command.Command;
import dev.lemon.command.CommandInfo;
import dev.lemon.module.Module;
import dev.lemon.utils.player.ChatUtil;

@CommandInfo(name = "Toggle", description = "Enables or disables the specified module.")
public class Toggle extends Command {

    @Override
    public void onExecute(String[] args){
       if (args.length != 2){
           ChatUtil.addMessage(".toggle <module>");
           return;
       }
       for (Module m : Lemon.INSTANCE.getModuleManager().getModules()){
           if (m.getName().equalsIgnoreCase(args[1])){
               m.toggle();
               ChatUtil.addMessage("Toggled "+m.getName()+" "+(m.isToggled() ? "on." : "off."));
           }
       }

    }
}
