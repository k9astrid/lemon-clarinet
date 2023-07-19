package dev.lemon.client.commands;

import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.main.Lemon;

@CommandInfo(name = "Config", description = "Interacts with configs.")
public class Config extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length != 3) {
            ChatUtil.addMessage(".config <subcommand> <argument>");
            return;
        }

        switch (args[1]){
            case "load":
                Lemon.INSTANCE.getConfigManager().loadConfig(args[2]);
                ChatUtil.addMessage("Loaded config "+args[2]);
                break;
            case "save":
                Lemon.INSTANCE.getConfigManager().saveConfig(args[2]);
                ChatUtil.addMessage("Saved config "+args[2]);
                break;
            default:
                ChatUtil.addMessage("Invalid subcommand!\nTry config load <config> or config save <config>.");
                break;

        }
    }
}
