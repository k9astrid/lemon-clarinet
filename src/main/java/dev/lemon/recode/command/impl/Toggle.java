package dev.lemon.recode.command.impl;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.command.CommandInfo;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.utils.player.ChatUtil;

@CommandInfo(name = "Toggle", description = "Enables or disables the specified module.")
public class Toggle extends Command {

    @Override
    public void onExecute(String[] args){
       if (args.length != 2){
           ChatUtil.addMessage(".toggle <module>");
           return;
       }
       for (Module m : Lemon.INSTANCE.getModuleManager().getModules()){
           if (m.getName().replace(" ", "").equalsIgnoreCase(args[1])){
               m.toggle();
               ChatUtil.addMessage("Toggled "+m.getName()+" "+(m.isToggled() ? "on." : "off."));
           }
       }

    }
}
