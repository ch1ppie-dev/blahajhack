package me.eternadox.client.api.util.world;

import me.eternadox.client.impl.Client;
import me.eternadox.client.api.util.Methods;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements Methods {

    public static void send(String message){
        String prefix = EnumChatFormatting.BLUE + Client.INSTANCE.getName().substring(0, 1) + EnumChatFormatting.GRAY + " > " + EnumChatFormatting.RESET;
        ChatComponentText chatComponent = new ChatComponentText(prefix + message);
        mc.thePlayer.addChatMessage(chatComponent);
    }
}
