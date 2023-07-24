package dev.lemon.client.commands;

import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.api.utils.player.ChatUtil;

@CommandInfo(name = "Test", description = "Just a simple test command :D")
public class Test extends Command {

    @Override
    public void onExecute(String[] args){
        ChatUtil.send("the command system works!");
    }
}
