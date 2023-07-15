package dev.lemon.client.commands;

import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.client.main.Lemon;

@CommandInfo(name = "Reload", description = "Reload's scripts")
public class Reload extends Command {

    @Override
    public void onExecute(String[] args) {
        Lemon.INSTANCE.getScriptManager().reload(false);
    }

}
