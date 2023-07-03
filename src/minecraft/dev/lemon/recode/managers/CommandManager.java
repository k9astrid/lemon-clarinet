package dev.lemon.recode.managers;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.Lemon;
import dev.lemon.recode.command.Command;
import dev.lemon.recode.command.impl.Test;
import dev.lemon.recode.event.impl.EventChat;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<Command> commands = new ArrayList<>();

    public void initialize(){
        Lemon.INSTANCE.getEventBus().subscribe(this);
        commands.add(new Test());
    }

    public List<Command> getCommands() {
        return commands;
    }

    @EventHandler
    public Listener<EventChat> eventChatListener = e -> {
      if (!e.getMessage().startsWith("."))
          return;
      e.setCancelled(true);
      String message = e.getMessage();

      String commandName = message.substring(1).split(" ")[0];

      String[] args = message.substring(1+commandName.length()).split(" ");

      for (Command c : commands){
          if (c.getName().equalsIgnoreCase(commandName)){
              c.onExecute(args);
          }
      }
    };


}
