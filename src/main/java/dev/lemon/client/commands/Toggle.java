package dev.lemon.client.commands;

import dev.lemon.client.main.Lemon;
import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.api.module.Module;
import dev.lemon.api.utils.player.ChatUtil;

@CommandInfo(name = "Toggle", description = "Enables or disables the specified module.")
public class Toggle extends Command {

    @Override
    public void onExecute(String[] args){
       if (args.length != 2) {
           ChatUtil.addMessage(".toggle <module>");
           return;
       }

       for (Module m : Lemon.INSTANCE.getModuleManager().getModulesMap().values()) {
           if (m.getName().replace(" ", "").equalsIgnoreCase(args[1])){
               m.toggle();
               ChatUtil.addMessage("Toggled "+m.getName()+" "+(m.isToggled() ? "on." : "off."));
           }
       }

    }
}
