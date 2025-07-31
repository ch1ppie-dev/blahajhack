package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.impl.Client;
import me.eternadox.client.api.util.world.ChatUtil;

@Info(name = "rename", aliases = {"clientname", "cname", "customname"})
public class Rename extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length > 0){
            String newName = String.join(" ", args);
            Client.INSTANCE.setName(newName);
            ChatUtil.send("Set client name to "+newName);

        }

    }
}
