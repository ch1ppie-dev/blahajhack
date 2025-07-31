package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.impl.Client;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.world.ChatUtil;
import org.lwjgl.input.Keyboard;

@Info(name = "Bind", aliases = {"b"})
public class Bind extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length == 2){
            for (Module m : Client.INSTANCE.getModuleManager().getModuleList()){
                if (m.getDisplayName().replace(" ", "").equalsIgnoreCase(args[0])){
                    m.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                    ChatUtil.send("Bound module "+m.getDisplayName()+" to "+Keyboard.getKeyName(m.getKey()));
                }
            }
        }

    }


}
