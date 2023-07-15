package dev.lemon.command.impl;

import dev.lemon.command.Command;
import dev.lemon.command.CommandInfo;
import dev.lemon.utils.player.ChatUtil;

@CommandInfo(name = "Test", description = "Just a simple test command :D")
public class Test extends Command {

    @Override
    public void onExecute(String[] args){
        ChatUtil.addMessage("the command system works!");
    }
}
