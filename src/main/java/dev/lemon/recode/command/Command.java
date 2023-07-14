package dev.lemon.recode.command;

import dev.lemon.recode.Lemon;

public class Command {
    private final String name = this.getClass().getDeclaredAnnotation(CommandInfo.class).name();
    private final String description = this.getClass().getDeclaredAnnotation(CommandInfo.class).description();

    public void onExecute(String[] args) {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
