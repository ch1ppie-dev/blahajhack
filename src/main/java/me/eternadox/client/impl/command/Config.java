package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.world.ChatUtil;
import me.eternadox.client.impl.Client;
import org.lwjgl.input.Keyboard;

@Info(name = "config", aliases = {"cfg", "c"})
public class Config extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length == 2){
            boolean task;

            switch (args[0].toLowerCase()){
                case "load":
                    task = Client.INSTANCE.getConfigReader().loadConfig(args[1] + ".json");
                    Client.INSTANCE.getConfigReader().createConfig("current.json");

                    if (task){
                        ChatUtil.send("Successfully loaded config '"+ args[1] + "'.");
                    } else {
                        ChatUtil.send("Config '"+ args[1] + "' does not exist.");
                    }
                    break;
                case "remove":
                case "delete":
                    task = Client.INSTANCE.getConfigReader().deleteConfig(args[1] + ".json");
                    if (task){
                        ChatUtil.send("Successfully deleted config '"+ args[1] + "'.");
                    } else {
                        ChatUtil.send("Config '"+ args[1] + "' does not exist.");

                    }
                    break;
                case "create":
                case "save":
                    task = Client.INSTANCE.getConfigReader().createConfig(args[1] + ".json");
                    if (task){
                        ChatUtil.send("Successfully saved config '"+ args[1] + "'.");
                    } else {
                        ChatUtil.send("Unable to save config '"+ args[1] + "'.");
                    }
                    break;
            }
        }

    }


}
