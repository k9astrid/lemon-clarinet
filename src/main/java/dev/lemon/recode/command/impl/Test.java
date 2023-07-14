package dev.lemon.recode.command.impl;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.command.CommandInfo;
import dev.lemon.recode.utils.player.ChatUtil;

@CommandInfo(name = "Test", description = "Just a simple test command :D")
public class Test extends Command {

    @Override
    public void onExecute(String[] args){
        ChatUtil.addMessage("the command system works!");
    }
}
