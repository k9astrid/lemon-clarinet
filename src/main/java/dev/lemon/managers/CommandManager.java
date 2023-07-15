package dev.lemon.managers;

import dev.lemon.Lemon;
import dev.lemon.command.Command;
import dev.lemon.command.impl.Bind;
import dev.lemon.command.impl.Test;
import dev.lemon.command.impl.Toggle;
import dev.lemon.recode.command.impl.*;
import dev.lemon.event.IEventListener;
import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private List<Command> commands = new ArrayList<>();

    public void initialize() {
        Lemon.INSTANCE.getEventBus().handle(this);
        commands.add(new Test());
        commands.add(new Bind());
        commands.add(new Toggle());
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Subscribe
    public final IEventListener<EventChat> eventChatListener = e -> {
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
