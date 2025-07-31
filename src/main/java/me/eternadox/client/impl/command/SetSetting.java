package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.setting.Setting;
import me.eternadox.client.api.util.world.ChatUtil;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.setting.BooleanSetting;
import me.eternadox.client.impl.setting.ModeSetting;
import me.eternadox.client.impl.setting.NumberSetting;

@Info(name = "setting", aliases = {"settingset", "s"})
public class SetSetting extends Command {

    @Override
    public void onExecute(String[] args){
        if (args.length == 3){
            Module m = Client.INSTANCE.getModuleManager().getModule(args[0]);

            if (m != null){
                for (Setting s : m.getSettings()){
                    if (s.getName().replace(" ", "").equalsIgnoreCase(args[1])){
                        try {
                            switch (s.getClass().getSimpleName()) {
                                case "BooleanSetting":
                                    ((BooleanSetting) s).setToggled(Boolean.parseBoolean(args[2]));
                                    break;
                                case "NumberSetting":
                                    ((NumberSetting) s).setValue(Double.parseDouble(args[2]));
                                    break;
                                case "ModeSetting":
                                    ((ModeSetting) s).setMode(args[2]);
                                    break;
                            }
                        } catch (NumberFormatException e){
                            ChatUtil.send("Invalid number!");
                        }
                    }
                }
            } else {
                ChatUtil.send("Module not found.");
            }

        }

    }
}
