package me.eternadox.client.impl.command;

import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.api.util.world.ChatUtil;
import net.minecraft.client.Minecraft;

@Info(name = "VClip", aliases = {"verticalclip", "vc"})
public class VClip extends Command {

    @Override
    public void onExecute(String[] args){
        try {
            if (args.length == 1 && Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Integer.parseInt(args[0]), Minecraft.getMinecraft().thePlayer.posZ);
                ChatUtil.send("Clipped you "+args[0]+" blocks");

            }
        } catch (Exception e){
            ChatUtil.send("Invalid number of blocks!");
        }

    }
}
