package me.eternadox.client.api.command;

import me.eternadox.client.api.command.annotations.Info;

public class Command {
    private final Info annotation = this.getClass().getDeclaredAnnotation(Info.class);
    private final String name = annotation.name();
    private final String[] aliases = annotation.aliases();

    public void onExecute(String[] args){

    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }
}
