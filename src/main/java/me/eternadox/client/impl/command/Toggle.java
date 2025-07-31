package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.impl.Client;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.world.ChatUtil;

@Info(name = "Toggle", aliases = {"t"})
public class Toggle extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length == 1){
            for (Module m : Client.INSTANCE.getModuleManager().getModuleList()){
                if (m.getDisplayName().replace(" ", "").equalsIgnoreCase(args[0])){
                    m.toggle();
                    ChatUtil.send((m.isToggled() ? "Enabled" : "Disabled") + " module " + m.getDisplayName());

                }
            }
        }

    }
}
